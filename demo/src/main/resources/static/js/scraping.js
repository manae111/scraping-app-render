'use strict';

document.getElementById('scrapeForm').addEventListener('submit', function(event) {
    event.preventDefault();//ページ遷移キャンセル

    //ここでScrapeボタンを無効化
    document.getElementById('Scrape').disabled = true;
    //ここでScrapeボタンのvalueを「解析中...」に変更
    document.getElementById('Scrape').value = '解析中...';

    var url = this.querySelector('input[name="inputUrl"]').value;
  
    fetch(this.action, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: 'url=' + encodeURIComponent(url),
    })
      .then(response => response.json())
      .then(data => {
        document.getElementById('scrapedItemName').value = data.name;
        if (data.price === "0") {
          document.getElementById('scrapedItemPrice').value = "";
        } else {
          document.getElementById('scrapedItemPrice').value = data.price;
        }
        document.getElementById('scrapedUrl').value = data.url;
        document.getElementById('submitForm').submit();
      })
      .catch(error => console.error('商品登録時にJavaScript上でエラーが発生しました:', error));
  });