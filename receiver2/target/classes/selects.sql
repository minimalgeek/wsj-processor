select w.DATE_TIME, w.WORD, w.WORD_COUNT
from wordcount_view_table as w
where (
   select count(*) from wordcount_view_table as w2
   where w2.DATE_TIME = w.DATE_TIME and w2.WORD_COUNT >= w.WORD_COUNT
) <= 1000;