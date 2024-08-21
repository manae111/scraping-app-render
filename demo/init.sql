-- 商品テーブル
drop table if exists items cascade;
create table items (
 id serial primary key
 , url text not null
 , item_name text
 , price_original integer not null
 , price_latest integer
 , user_id integer
) ;

-- ユーザーテーブル
drop table if exists users cascade;
create table users (
 id serial primary key
 , username text not null unique
 , password text not null
) ;