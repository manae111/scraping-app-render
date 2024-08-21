package com.example.demo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.demo.service.ItemService;

/**
 * バッチ処理を定時に呼び出すクラス
 * Sakai
 */
@Configuration
@EnableScheduling
public class ScheduledConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledConfiguration.class);


    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job myjob;

    @Autowired
    private ItemService itemService;

    //cron = "秒 分 時間 日 月 曜日"
    @Scheduled(cron = "0 0 11 * * ?")
    public void scheduledSendmail() {
        JobParameters jobParameters = new JobParametersBuilder()
            .addLong("time",System.currentTimeMillis())//実行時間を記録
            .toJobParameters();
        try {
            jobLauncher.run(myjob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
        //左からJobがすでに実行中、Jobが再実行された、Jobがすでに完了している、Jobのパラメータが不正
        logger.error("バッチ処理が失敗しました");
        e.printStackTrace();
        }

        // DB上に登録時priceよりもバッチ処理後priceが安い商品一覧をメールを送信するメソッドを呼び出す
        itemService.sendMail();

    }
}
