import java.util.*;
import java.io.IOException;

public class LyricSearchEngine {
    //Fields
    private TreeMap<String, TreeMap<String, Integer>> TFMap;
    
    public LyricSearchEngine() {
        TFMap = new TreeMap<>();
    }
    
    //Method to construct the TreeMap of Songs TFMap
    public void processSongs(String folderPath) throws IOException {
        TFMap = FilesReader.readDocumentsFromFolder(folderPath);
    }
    
    //Searches tree map for given song name and word 
    //then returns number of times word appeared. If 
    //the word is not found it returns 0.
    public int searchWord(String SongFileName, String word) {
        if (TFMap.containsKey(SongFileName)) {
            word = word.toLowerCase();
            TreeMap<String, Integer> wordCounts = TFMap.get(SongFileName);
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
        if (TFMap.containsKey(SongFileName)) {
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
        
        //TFMap.put(docName, docTFValues);
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
