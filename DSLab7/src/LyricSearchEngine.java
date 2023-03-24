import java.util.*;

public class LyricSearchEngine {
    private TreeMap<String, TreeMap<String, Double>> TFMap;
    
    public LyricSearchEngine() {
        TFMap = new TreeMap<>();
        
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
    
    

}
