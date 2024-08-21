// インポート
const express = require('express');//Webサーバー構築のためのフレームワーク
const axios = require('axios');//ライブラリ（httpクライアントを送信する）
const cheerio = require('cheerio');//ライブラリ（htmlの解析）
const cors = require('cors');//ミドルウェア

const app = express();
app.use(cors());
app.use(express.urlencoded({ extended: true }));//URLエンコードされたデータを解析する
app.use(express.json());//JSONデータを解析する

app.post('/scrape', (req, res) => {
  const url = req.body.url;

  axios.get(url)
    .then(response => {
      const html = response.data;
      const $ = cheerio.load(html);
      const name = $('#productTitle').text(); // htmlからidがproductTitleのテキストを取得
      const price = $('#corePriceDisplay_desktop_feature_div .a-price-whole:first').text();
      console.log('取得した価格:', price);
      res.send({name: name, price: price, url: url});
    })
    .catch(error => {
      console.error('商品登録時にサーバーでエラーが発生しました:', error);
      res.status(500).send(error);
    });
});

app.post('/scrape-batch', (req, res) => {
  const url = req.body.url;

  axios.get(url)
    .then(response => {
      const html = response.data;
      const $ = cheerio.load(html);
      const price = $('#corePriceDisplay_desktop_feature_div .a-price-whole:first').text();
      res.send({price: price});
    })
    .catch(error => {
      console.error('バッチ処理中にサーバーでエラーが発生しました', error);
      res.status(500).send(error);
    });
});

app.get('/', (req,res) => {
  res.send('サーバーは正常に動作しています');
});

app.listen(3000, () => console.log('サーバーが作成されました:port 3000'));