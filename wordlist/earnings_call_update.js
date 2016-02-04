db.earnings_call.aggregate(
   [
     {
       $project: {
          publishDateYMD: { $dateToString: { format: "%Y-%m-%d", date: "$publishDate" } },
          tradingSymbol: 1,
          tone: 1,
          h_tone: 1
       }
     }
   ]
)