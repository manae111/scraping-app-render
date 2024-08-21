# ベースイメージとしてNode.jsを使用
FROM node:20

# 作業ディレクトリを設定
WORKDIR /usr/src/app

# アプリケーションの依存関係をインストール
COPY ../src/main/resources/static/js/package*.json ./
RUN npm install

# アプリケーションのソースコードをコピー
COPY ../src/main/resources/static/js/server.js ./

# ポートを公開
EXPOSE 3000

# アプリケーションを起動
CMD ["node", "server.js"]