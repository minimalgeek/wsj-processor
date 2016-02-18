use insider;
var docs=db.henry_words.find();
use insider_test;
docs.forEach(function(doc) { db.henry_words.insert(doc); });

use insider;
var docs=db.harvard_words.find();
use insider_test;
docs.forEach(function(doc) { db.harvard_words.insert(doc); });