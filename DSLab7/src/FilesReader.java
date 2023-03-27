import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

public class FilesReader {
        
        public static TreeMap<String, TreeMap<String, Integer>> readDocumentsFromFolder(String folderPath) throws IOException {
            File folder = new File(folderPath);
            TreeMap<String, TreeMap<String, Integer>> documents = new TreeMap<>();
            
            if (folder.isDirectory()) {
                File[] files = folder.listFiles();
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        String documentName = file.getName();
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        TreeMap<String, Integer> wordCounts = new TreeMap<>();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            line = line.toLowerCase();
                            line = line.replace("(", " ");
                            line = line.replace(")", " ");
                            line = line.replace("!", " ");
                            line = line.replace("?", " ");
                            line = line.replace(",", " ");
                            line = line.replace("...", " ");
                            line = line.replace("\"", " ");
                            String[] words = line.split("\\s+");
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
                        documents.put(documentName, wordCounts);
                    }
                }
            }
            
            return documents;
        }
        
        public static void main(String[] args) throws IOException {
            String folderPath = "/Users/wildg/git/DSLab7/DSLab7/src/Queen";
            TreeMap<String, TreeMap<String, Integer>> documents = readDocumentsFromFolder(folderPath);
            for (String documentName : documents.keySet()) {
                System.out.println("Document name: " + documentName);
                TreeMap<String, Integer> wordCounts = documents.get(documentName);
                for (String word : wordCounts.keySet()) {
                    System.out.println("Word: " + word + " Count: " + wordCounts.get(word));
                }
                System.out.println();
            }
        }
        
    }

