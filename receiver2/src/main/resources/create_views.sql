/*
CREATE OR REPLACE VIEW article_day_view AS
SELECT a.*, DATE(DATE_ADD(CONVERT_TZ(DATE_SUB(a.DATE_TIME, INTERVAL 1 HOUR), 'UTC', 'US/Eastern'),INTERVAL '14:30' HOUR_MINUTE)) AS DATE_DAY
FROM article a;

DROP TABLE IF EXISTS article_day_view_table;

CREATE TABLE article_day_view_table AS    
SELECT * FROM article_day_view;
*/

ALTER TABLE article
ADD DATE_DAY DATETIME NULL;
ALTER TABLE article
ADD INDEX DATE_DAY_IDX (DATE_DAY);

UPDATE article SET DATE_DAY = DATE(DATE_ADD(CONVERT_TZ(DATE_SUB(DATE_TIME, INTERVAL 1 HOUR), 'UTC', 'US/Eastern'),INTERVAL '14:30' HOUR_MINUTE));

CREATE OR REPLACE VIEW wordcount_view AS
SELECT a.DATE_DAY AS DATE_TIME, aw.WORD AS WORD, COUNT(aw.WORD) AS WORD_COUNT, COUNT(distinct A.ID) AS DOCUMENT_COUNT
FROM article AS a
JOIN article_word aw ON aw.ARTICLE_ID = a.ID
GROUP BY aw.WORD, a.DATE_DAY
ORDER BY a.DATE_DAY ASC, COUNT(distinct a.ID) DESC;

DROP TABLE IF EXISTS wordcount_view_table;

CREATE TABLE wordcount_view_table AS    
SELECT * FROM wordcount_view;