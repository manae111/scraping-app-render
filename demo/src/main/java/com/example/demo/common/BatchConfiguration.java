package com.example.demo.common;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.service.ItemService;

import reactor.core.publisher.Mono;

/**
 * バッチ処理をするクラス
 * タスクレットモデル
 * Sakai
 */
@Configuration
public class BatchConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);
    private final WebClient webClient;
    private final ItemService itemService;

    public BatchConfiguration(WebClient.Builder webClientBuilder, ItemService itemService) {
        this.webClient = webClientBuilder.baseUrl("http://server:3000").build();
        this.itemService = itemService;
    }

    @Bean
    Step myStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("myStep", jobRepository)
                .tasklet(myTasklet(), transactionManager)
                .build();
    }

    /**
     * DB上のURLを取得し、それぞれのURLに対してスクレイピングを行う
     * スクレイピング結果をDBに保存する
     * 
     * @return
     */
    @Bean
    Tasklet myTasklet() {
        return (contribution, chunkContext) -> {
            List<String> urlList = itemService.findAllUrl();
            int updateItemCount = 0;
            for (String url : urlList) {
                JSONObject json = new JSONObject();
                json.put("url", url);
                Mono<String> response = webClient.post()
                        .uri("/scrape-batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(json.toString()))
                        .retrieve()
                        .bodyToMono(String.class);

                String result = response.block();
                logger.info("Result: {}", result);

                // レスポンスをJSONオブジェクトとして解析
                JSONObject jsonResponse = new JSONObject(result);

                // "price"フィールドを取得
                String priceStr = jsonResponse.getString("price");
                if (priceStr != null && !priceStr.isEmpty()) {
                    priceStr = priceStr.replace(",", "");
                    int price = Integer.parseInt(priceStr);
                    itemService.update(url, price);
                    updateItemCount++;
                } else {
                    logger.error("最新価格の取得に失敗しました　URL:" + url);
                }
                
            }
            logger.info("取得件数:" + updateItemCount);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    Job myJob(JobRepository jobRepository, Step myStep) {
        return new JobBuilder("myJob", jobRepository)
                .start(myStep)
                .build();
    }
}
