package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Item;

@Repository
public class ItemRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setUrl(rs.getString("url"));
        item.setItemName(rs.getString("item_name"));
        item.setPriceOriginal(rs.getInt("price_original"));
        Integer priceLatest = (Integer)rs.getObject("price_latest");
        item.setPriceLatest(priceLatest);
        item.setUserId(rs.getInt("user_id"));
        return item;
    };

    private static final RowMapper<String> URL_ROW_MAPPER = (rs, i) -> {
        String url = rs.getString("url");
        return url;
    };

    /**
     * 商品情報を挿入するメソッド item
     * 
     * @param item
     */
    public void insert(Item item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        String sql = """
                INSERT INTO items
                (url, item_name, price_original, price_latest, user_id)
                VALUES (:url, :itemName, :priceOriginal, :priceLatest, :userId);
                """;
        template.update(sql, param);
    }

    /**
     * 最新価格を更新するメソッド item
     * 
     * @param item
     * @return
     */
    public void update(String url, Integer price) {
        String sql = """
                UPDATE items
                SET price_latest = :priceLatest
                WHERE URL = :url;
                """;
        SqlParameterSource param = new MapSqlParameterSource().addValue("priceLatest", price).addValue("url", url);
        template.update(sql, param);
    }

    /**
     * バッチ処理のためにDBのURLを全件取得するメソッド item
     */
    public List<String> findAllUrl() {
        String sql = """
                SELECT url FROM items;
                """;
        List<String> urlList = template.query(sql, URL_ROW_MAPPER);
        return urlList;
    }

    /**
     * URLからpriceOriginalを取得するメソッド item
     */
    public Integer findPriceOriginal(String url) {
        String sql = """
                SELECT price_original FROM items
                WHERE url = :url;
                """;
        SqlParameterSource param = new MapSqlParameterSource().addValue("url", url);
        Integer priceOriginal = template.queryForObject(sql, param, Integer.class);
        return priceOriginal;
    }

    /**
     * 定時送信メールのためにitemsテーブルの情報を全件取得するメソッド item
     */
    public List<Item> findAll() {
        String sql = "SELECT * FROM items";
        List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
        return itemList;
    }

    /**
     * userIdからitemListを取得するメソッド item
     */
    public List<Item> findItemByUserId(Integer userId) {
        String sql = """
                    SELECT * FROM items
                    WHERE user_id = :userId
                    ORDER BY id DESC;
                    """;
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
        List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
        return itemList;
    }

   
    /**
     * usernameからOriginalPrice>LatestPriceのitemを取得するメソッド item
     */
    public List<Item> findUpdateItem(String username) {
        String sql = """
                SELECT * 
                FROM items i
                LEFT OUTER JOIN users u 
                ON i.user_id = u.id 
                WHERE i.price_original > i.price_latest AND u.username = :username;
                """;
                SqlParameterSource param = new MapSqlParameterSource().addValue("username", username);
        List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
        return itemList;
    }

    /**
     * 商品情報を削除するメソッド(物理削除)
     * 
     * @param id
     */
    public void delete(Integer id) {
        String sql = """
                    DELETE FROM items WHERE id = :id;
                    """;
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        template.update(sql, param);
    }

}
