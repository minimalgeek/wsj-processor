create or replace view wordcount_view as
select DATE(CONVERT_TZ(DATE_SUB(a.DATE_TIME, INTERVAL 1 HOUR), 'UTC', 'US/Eastern')) as DATE_TIME, aw.WORD as WORD, count(aw.word) as WORD_COUNT
from article a
join article_word aw on aw.ARTICLE_ID = a.ID
group by aw.WORD, DATE(CONVERT_TZ(DATE_SUB(a.DATE_TIME, INTERVAL 1 HOUR), 'UTC', 'US/Eastern'))
order by DATE(CONVERT_TZ(DATE_SUB(a.DATE_TIME, INTERVAL 1 HOUR), 'UTC', 'US/Eastern')) asc, count(aw.word) desc;

DROP TABLE IF EXISTS wordcount_view_table;

CREATE TABLE wordcount_view_table AS    
  SELECT
    *
  FROM wordcount_view;
  
DROP TABLE IF EXISTS wordcount_view_table_top1000;

CREATE TABLE wordcount_view_table_top1000 AS    
	select w.DATE_TIME, w.WORD, w.WORD_COUNT
	from wordcount_view_table as w
	where (
	   select count(*) from wordcount_view_table as w2
	   where w2.DATE_TIME = w.DATE_TIME and w2.WORD_COUNT >= w.WORD_COUNT
	) <= 1000;