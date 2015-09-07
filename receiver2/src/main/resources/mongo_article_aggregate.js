db.article.aggregate(
[
    {$project : {
        dateDay : {
            $add : ["$dateDay", 1000*60*60]
        },
        articleWords : 1
    }},
    {$unwind : "$articleWords"},
    {$group : 
        {
           _id : {year: { $year: "$dateDay" }, month: { $month: "$dateDay" }, day: { $dayOfMonth: "$dateDay" }, word: "$articleWords" },
	   differentDocuments : {$sum : 1}
        }
    },
    {$project : {
        _id : 0,
        word : "$_id.word",
	year : "$_id.year",
	month : "$_id.month",
	day : "$_id.day",
        differentDocuments : 1
    }},
    {$sort : {
        year : 1,
        month : 1,
        day : 1
    }},
    {$out : "result"}
], {
    allowDiskUse:true,
    cursor:{}}
);