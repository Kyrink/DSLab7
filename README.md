# DSLab7
building a TF-IDF model for a search engine to rank lyrics.



Tf method(“term”, filename) :  calls on the search method and return the value for TF

Idf method(“term”):
This method will loop through all the files using the search method and return the idf value

Search file method (“term”, file name): 
This method will return an int value of the amount of times the term given is in the file.

Ranking method (“entire term”) : This method will calculate the ranking for each document using the values from TF and IDF. maybe use a while loop to go through all the files to calculate the TF

Query search: the main method will have a scanner and then use the ranking method.


