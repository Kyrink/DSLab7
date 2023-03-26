
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;

/*
 * SongCollection.java
 * Read the specified data file and build an array of songs.
 * 
 * Starting code by Prof. Boothe 2019
 * 
 * Ahmad Mouhsen, Program 1, 9/13/2022
 * 
 * This class is meant to take a text file
 * that is filled with songs specifically
 * the artist, the title and the lyrics
 * in that order and returns a array of
 * each element being a object of the song
 * class for each song.
 */
public class SongCollection {

    private Song[] songs;
    ArrayList<Song> SongsList = new ArrayList<Song>();

    public SongCollection(String newfilename) {
        
        // read in the song file and build the songs array
        String filename = newfilename;
        
        Scanner scan;
        try {
            scan = new Scanner(new File(filename));
            
            String Artist = "";
            String Title = "";
            String Lyrics = "";
            
            while(scan.hasNext()){
                String currentLine = scan.nextLine();
                
                if(currentLine.startsWith("ARTIST=")) {
                    Artist = currentLine.substring(8, 
                            currentLine.indexOf("\"", 8));
                }
                else if(currentLine.startsWith("TITLE=")) {
                    Title = currentLine.substring(7, 
                            currentLine.indexOf("\"", 7));
                }
                else if(currentLine.startsWith("LYRICS=")) {
                    Lyrics = currentLine.substring(8) + "\n";
                    currentLine = scan.nextLine();
                    
                    while(currentLine.equals("") || 
                            !currentLine.substring(0,1).equals("\"")) {
                        Lyrics = Lyrics + currentLine + "\n";
                        currentLine = scan.nextLine();
                    }
                    
                    SongsList.add( new Song (Artist, Title, Lyrics));
                }
                else {
                    System.out.println("Could not determine text has"
                            + " ARTIST=, TITLE=, or LYRICS=.");
                    break;
                }
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("Song file was not found.");
        }
        
        // Convert Songsarray list to plain array.
        int sizeArray = SongsList.size();
        songs = new Song[sizeArray];
        SongsList.toArray(songs);
        
        
        // sort the songs array
        Arrays.sort(songs);
        
    }
    
    /*
     * This method is meant to return a string of
     * of the total number of songs and the first 10
     * songs in the array. It returns the total of
     * songs first and then the first 10 songs by
     * artist then title. If there are less than
     * 10 songs it will return however many there
     * are.
     */
    public String toString() {
        /*
         * make a to string method to print out the 
         * songs when called by the main.
        */
        
        String totalSongs = "Total songs = " + 
        songs.length + ", first songs: \n";
        
        
        int runs;
        String printSongs = "";
        
        if(songs.length >= 10) {
            runs = 10;
        }
        else {
            runs = songs.length;
        }
        
        for(int i = 0; i < runs; i++) {
            printSongs = printSongs + songs[i].getArtist() + ", \"" +
                    songs[i].getTitle() + "\" \n";
        }
        
        
        return totalSongs + printSongs;

    }

    // returns the array of all Songs
    // this is used as the data source for building other data structures
    public Song[] getAllSongs() {
        return songs;
    }

    // testing method
    // it gets the name of the song file as a command line argument
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }
        
        SongCollection sc = new SongCollection(args[0]);
        
        // todo: show song count and first 10 songs
        //(name & title only, 1 per line)
        System.out.println(sc.toString());
    }
}
