package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Item;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    @Autowired
	private MailSender sender;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    	/**
	 * 登録時priceよりもバッチ処理後priceが安い商品一覧のメールを送信するメソッド item
	 * 
	 * @param email
	 */
	public void sendMail() {
		List<String> usernameList = userRepository.findAllUsername();
		List<Item> updateItemList = new ArrayList<>();
        int updateItemCount = 0;
		int mailCount = 0;
		for (String username : usernameList) {
			updateItemList = itemRepository.findUpdateItem(username);

			if (updateItemList.size() != 0) {

				try {
					SimpleMailMessage msg = new SimpleMailMessage();
					msg.setFrom("on99.matsunaga.dai@gmail.com");
					msg.setTo(username);
					msg.setSubject("価格変更がある商品のご案内");// タイトルの設定
					String template = "=======================================\n" +
					  "商品名:\n" +
					  "{itemName}\n\n" +
					  "URL:\n" +
					  "{url}\n\n" +
					  "登録時の価格:\n" +
					  "{priceOriginal}円\n\n" +
					  "現在の価格:\n" +
					  "{priceLatest}円\n\n" +
					  "=======================================";
	
					String message = updateItemList.stream()
					  .map(updateItem -> template.replace("{itemName}", updateItem.getItemName())
												 .replace("{url}", updateItem.getUrl())
												 .replace("{priceOriginal}", String.valueOf(updateItem.getPriceOriginal()))
												 .replace("{priceLatest}", String.valueOf(updateItem.getPriceLatest())))
					  .collect(Collectors.joining("\n"));
					msg.setText(message);
					sender.send(msg);
					mailCount++;
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
            updateItemCount += updateItemList.size();
		}
        logger.info("価格変更商品数:" + updateItemCount + "件");
		logger.info("メール送信数:" + mailCount + "件");
	}

	public void insert(Item item) {
		itemRepository.insert(item);
	}

	public void update(String url, Integer price) {
		itemRepository.update(url, price);
	}

	public List<String> findAllUrl() {
		List<String> urlList = itemRepository.findAllUrl();
		return urlList;
	}

	public Integer findPriceOriginal(String url) {
		Integer priceOriginal = itemRepository.findPriceOriginal(url);
		return priceOriginal;
	}

	public List<Item> findAll() {
		List<Item> itemList = itemRepository.findAll();
		return itemList;
	}

	public List<Item> findItemByUserId(Integer userId) {
		List<Item> itemList = itemRepository.findItemByUserId(userId);
		return itemList;
	}

    /**
     * 商品情報を削除するメソッド(物理削除)
     * 
     * @param id
     */
    public void delete(Integer id) {
        itemRepository.delete(id);
    }

}
