import java.util.*;

public class LyricSearchEngine {
    //Fields
    private TreeMap<String, TreeMap<String, Double>> TFMap;
    private Song[] songsArray; //Array of all the Queen songs
    
    public LyricSearchEngine(SongCollection sc) {
        TFMap = new TreeMap<>();
        songsArray = sc.getAllSongs();
    }
    
    //This is a example method just to test out using the songsArray.
    //There are 16 songs in the array and they are arranged alphabetically by title
    public void examlpleMethodPrint() {
        System.out.println(songsArray[0]);
        System.out.println(songsArray[0].getLyrics());
        
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
    
    public static void main(String[] args) {
        //make sure AllQueenSongs.txt is in the run configuration.
        SongCollection sc = new SongCollection(args[0]);
        LyricSearchEngine test = new LyricSearchEngine(sc);
        
        test.examlpleMethodPrint();
        
        
    }

}
