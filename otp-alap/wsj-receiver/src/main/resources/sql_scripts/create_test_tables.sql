INSERT wsj_01_test.article SELECT * FROM wsj_01.article where DATE_TIME BETWEEN '2011-12-31' AND '2012-02-01';

INSERT wsj_01_test.article_word 
(SELECT aw.* 
FROM wsj_01.article_word aw 
join wsj_01.article a on a.ID = aw.ARTICLE_ID
where a.DATE_TIME BETWEEN '2011-12-31' AND '2012-02-01');

