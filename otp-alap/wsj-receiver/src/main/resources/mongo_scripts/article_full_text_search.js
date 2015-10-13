db.article.find( 
    { $text: { $search: "\"APPLE INC\" AAPL" } },
    { score: { $meta: "textScore" } }
);

db.article.find( 
    { $text: { $search: "\"APPLE INC\" AAPL" } },
    { score: { $meta: "textScore" } }
).count();
    
// 3401

db.article.find( 
    { $text: { $search: "\"APPLE INC\"" } },
    { score: { $meta: "textScore" } }
).count();
    
// 3401
    
db.article.find( 
    { $text: { $search: "AAPL" } },
    { score: { $meta: "textScore" } }
).count();

// 2634
    
db.article.aggregate(
[
    { $match: { $text: { $search: "\"APPLE INC\" AAPL" } } },
    { $project: { _id: 0, url: 1, plainText: 1, dateTime: 1 } }
]
);