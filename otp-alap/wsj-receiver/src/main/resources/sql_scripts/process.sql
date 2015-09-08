select 
(select count(a.id) from article a) as all_articles,
(select count(a.id) from article a where a.processed = true) as processed_articles;

select count(aw.id) from article_word aw;

select 
	YEAR(a.date_time), 
	MONTH(a.date_time), 
	count(a.id) 
from article a 
where a.processed = true
group by 
	YEAR(a.date_time), 
	MONTH(a.date_time);

select * from article where processed = false;