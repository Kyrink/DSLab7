import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

public class SearchEngine {
    private TreeMap<String, TreeMap<String, TreeMap<Double, Double>>> documents;

    public SearchEngine(String folderPath) throws IOException {
        if (folderPath == null || folderPath.isEmpty()) {
            throw new IllegalArgumentException("folderPath must not be null or empty");
        }
        
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("folderPath must be a directory");
        }
        
        this.documents = new TreeMap<>();

        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                String fileName = file.getName();
                String documentName = fileName.substring(0, fileName.lastIndexOf("."));
                BufferedReader reader = new BufferedReader(new FileReader(file));
                TreeMap<String, Integer> wordCounts = new TreeMap<>();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.replaceAll("[^a-zA-Z0-9'\\s-]", "").toLowerCase().split("\\s+");
                    for (String word : words) {
                        if (wordCounts.containsKey(word)) {
                            int count = wordCounts.get(word);
                            wordCounts.put(word, count + 1);
                        } else {
                            wordCounts.put(word, 1);
                        }
                    }
                }
                reader.close();
                TreeMap<String, TreeMap<Double, Double>> documentMap = new TreeMap<>();
                for (String word : wordCounts.keySet()) {
                    double tf = calculateTF(wordCounts, word);
                    double idf = calculateIDF(word, documents);
                    TreeMap<Double, Double> positionsMap = new TreeMap<>();
                    positionsMap.put(tf * idf, tf);
                    documentMap.put(word, positionsMap);
                }
                documents.put(documentName, documentMap);
            }
        }
    }

    public static double calculateTF(TreeMap<String, Integer> wordCounts, String word) {
        double totalTerms = 0.0;
        for (int count : wordCounts.values()) {
            totalTerms += count;
        }
        double frequency = (double) wordCounts.getOrDefault(word, 0);
        return frequency / totalTerms;
    }

    public static double calculateIDF(String word, TreeMap<String, TreeMap<String, TreeMap<Double, Double>>> documents) {
        int numDocs = documents.size();
        int docsWithWord = 0;
        for (String documentName : documents.keySet()) {
            TreeMap<String, TreeMap<Double, Double>> wordCounts = documents.get(documentName);
            if (wordCounts.containsKey(word)) {
                docsWithWord++;
            }
        }
        return Math.log10((double) numDocs / (double) docsWithWord);
    }
    
    public List<String> search(String query) {
        TreeMap<String, Double> scores = new TreeMap<>();
        TreeMap<String, Integer> queryWordCounts = getWordCounts(query);
        for (String documentName : documents.keySet()) {
            TreeMap<String, TreeMap<Double, Double>> documentMap = documents.get(documentName);
            double score = 0.0;
            for (String word : queryWordCounts.keySet()) {
                if (documentMap.containsKey(word)) {
                    double tf = documentMap.get(word).firstEntry().getValue();
                    double idf = calculateIDF(word, this.documents);
                    double tfIdf = tf * idf;
                    score += tfIdf * queryWordCounts.get(word);
                }
            }
            if (score > 0.0) {
                scores.put(documentName, score);
            }
        }
        List<String> top5 = new ArrayList<>();
        List<Entry<String, Double>> entries = new ArrayList<>(scores.entrySet());
        entries.sort(Entry.comparingByValue(Comparator.reverseOrder()));
        for (int i = 0; i < Math.min(5, entries.size()); i++) {
            top5.add(entries.get(i).getKey());
        }
        return top5;
    }

    private static TreeMap<String, Integer> getWordCounts(String text) {
        TreeMap<String, Integer> wordCounts = new TreeMap<>();
        String[] words = text.replaceAll("[^a-zA-Z0-9'\\s-]", "").toLowerCase().split("\\s+");
        for (String word : words) {
            if (wordCounts.containsKey(word)) {
                int count = wordCounts.get(word);
                wordCounts.put(word, count + 1);
            } else {
                wordCounts.put(word, 1);
            }
        }
        return wordCounts;
    }
    
    
    
    public static void main(String[] args) throws IOException {
        String folderPath = "/Users/kyrinkalonji/git/DSLab7/DSLab7/Queen";
        SearchEngine engine = new SearchEngine(folderPath);

        // Test the search engine with different queries
        String[] queries = {"love and life", "you know truth", "little champion", "Mama", "happy music bring power"};
        for (String query : queries) {
            List<String> results = engine.search(query);
            System.out.println("Query: " + query);
            for (String result : results) {
                TreeMap<String, TreeMap<Double, Double>> documentMap = engine.documents.get(result);
                double rankingScore = 0.0;
                for (String word : getWordCounts(query).keySet()) {
                    if (documentMap.containsKey(word)) { // Add this null check
                        double tf = documentMap.get(word).firstEntry().getValue();
                        double idf = calculateIDF(word, engine.documents);
                        double tfIdf = tf * idf;
                        rankingScore += tfIdf * getWordCounts(query).get(word);
                    }
                }
                System.out.println("Document: " + result + " Score: " + rankingScore);
            }
            System.out.println();
        }
    }
    
}