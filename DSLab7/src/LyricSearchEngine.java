import java.util.*;
import java.io.IOException;

public class LyricSearchEngine {
    //Fields
    private TreeMap<String, TreeMap<String, Double>> TFMap;
    private TreeMap<String, TreeMap<String, Integer>> SongsMap;
    
    public LyricSearchEngine() {
        SongsMap = new TreeMap<>();
    }
    
    //Method to construct the TreeMap of Songs TFMap
    public void processSongs(String folderPath) throws IOException {
        SongsMap = FilesReader.readDocumentsFromFolder(folderPath);
    }
    
    //Searches tree map for given song name and word 
    //then returns number of times word appeared. If 
    //the word is not found it returns 0.
    public int searchWord(String SongFileName, String word) {
        if (SongsMap.containsKey(SongFileName)) {
            word = word.toLowerCase();
            TreeMap<String, Integer> wordCounts = SongsMap.get(SongFileName);
            if(wordCounts.containsKey(word)) {
                return wordCounts.get(word);
            }
            else {
                return 0;
            }
        }
        System.out.println(SongFileName +": Song File Name Does Not Exist.");
        return 0;
    }
    
    //returns the number of times each word in a phrase appeared in a song.
    //Example, "I want it all" will return the number of times each word
    //appeared in the song separately. I + want + it + all
    public int searchPhrase(String SongFileName, String Phrase) {
        if (SongsMap.containsKey(SongFileName)) {
            int count = 0;
            Phrase = Phrase.toLowerCase();
            String[] phraseWords = Phrase.split("\\s+");
            for(int i = 0; i < phraseWords.length; i++) {
                count += searchWord(SongFileName, phraseWords[i]);
            }
            return count;
        }
        System.out.println(SongFileName +": Song File Name Does Not Exist.");
        return 0;
    }
    
    
    /**
     * Abbas: Calculate idf
     * This is the best method i could come up with to calculate the idf value using 
     * the FileReaderExample.java class that Kyrin provided
     * @param documents the documents
     * @return an idf map with the idf value for all words in all documents/txt files
     */
    public static TreeMap<String, Double> calculateIDF(TreeMap<String, TreeMap<String, Integer>> documents) {
		TreeMap<String, Double> idfMap = new TreeMap<>();
		int numDocs = documents.size();

		// Iterate over all words in all documents
		for (String documentName : documents.keySet()) {
			TreeMap<String, Integer> wordCounts = documents.get(documentName);
			for (String word : wordCounts.keySet()) {
				if (!idfMap.containsKey(word)) {
					// Calculate IDF value for word
					int docsWithWord = 0;
					for (String otherDocumentName : documents.keySet()) {
						if (documents.get(otherDocumentName).containsKey(word)) {
							docsWithWord++;
						}
					}
					double idf = Math.log10((double) numDocs / docsWithWord);
					idfMap.put(word, idf);
				}
			}
		}

		return idfMap;
	}
    
    public void calculateTF(String docName, String[] words) {
        TreeMap<String, Integer> wordFrequencyMap = new TreeMap<>();
        
        int totalTerms = words.length;
        
        for(String word : words) {
            word = word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            if(word.isEmpty()) {
                continue;
            }
            
            int frequency = wordFrequencyMap.getOrDefault(wordFrequencyMap, 0);
            wordFrequencyMap.put(word, frequency + 1);
        }
        
        TreeMap<String, Double> docTFValues = new TreeMap<>();
        for(Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
            String word = entry.getKey();
            int frequency = entry.getValue();
            double TF = (double) frequency/totalTerms;
            docTFValues.put(word, TF);
        }
        
        TFMap.put(docName, docTFValues);
    }
	public TreeMap<String, Double> rankingDocuments(String query) {
    // Calculate IDF values for all terms in the collection
    TreeMap<String, Double> idfMap = calculateIDF(SongsMap);

    // Split query into terms and count their frequency
    String[] queryTerms = query.toLowerCase().split("\\s+");
    TreeMap<String, Integer> queryTermCounts = new TreeMap<>();
    for (String term : queryTerms) {
        queryTermCounts.put(term, queryTermCounts.getOrDefault(term, 0) + 1);
    }

    // Sort documents by score in descending order
    Comparator<String> scoreComparator = new Comparator<String>() {
        public int compare(String doc1, String doc2) {
            return Double.compare(documentScores.get(doc2), documentScores.get(doc1));
        }
    };
    TreeMap<String, Double> rankedDocuments = new TreeMap<>(scoreComparator);
    rankedDocuments.putAll(documentScores);

    return rankedDocuments;
}

    
    public static void main(String[] args) throws IOException {
        LyricSearchEngine test = new LyricSearchEngine();
        //"/path/to/my/Queen Songs Folder"
        String folderPath = "/Users/wildg/git/DSLab7/DSLab7/src/Queen";
        test.processSongs(folderPath);
        
        int count = test.searchWord("I Want It All.txt", "I");
        System.out.println(count);
        
        int count2 = test.searchPhrase("I Want It All.txt", "I want it all");
        System.out.println(count2);
        
    }

}
