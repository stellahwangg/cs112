package music;

import java.util.*;

import javax.swing.text.PlainDocument;

/**
 * This class represents a library of song playlists.
 *
 * An ArrayList of Playlist objects represents the various playlists 
 * within one's library.
 * 
 * @author Jeremy Hui
 * @author Vian Miranda
 */
public class PlaylistLibrary {

    private ArrayList<Playlist> songLibrary; // contains various playlists

    /**
     * DO NOT EDIT!
     * Constructor for Library.
     * 
     * @param songLibrary passes in ArrayList of playlists
     */
    public PlaylistLibrary(ArrayList<Playlist> songLibrary) {
        this.songLibrary = songLibrary;
    }

    /**
     * DO NOT EDIT!
     * Default constructor for an empty library. 
     */
    public PlaylistLibrary() {
        this(null);
    }

    /**
     * This method reads the songs from an input csv file, and creates a 
     * playlist from it.
     * Each song is on a different line.
     * 
     * 1. Open the file using StdIn.setFile(filename);
     * 
     * 2. Declare a reference that will refer to the last song of the circular linked list.
     * 
     * 3. While there are still lines in the input file:
     *      1. read a song from the file
     *      2. create an object of type Song with the song information
     *      3. Create a SongNode object that holds the Song object from step 3.2.
     *      4. insert the Song object at the END of the circular linked list (use the reference from step 2)
     *      5. increase the count of the number of songs
     * 
     * 4. Create a Playlist object with the reference from step (2) and the number of songs in the playlist
     * 
     * 5. Return the Playlist object
     * 
     * Each line of the input file has the following format:
     *      songName,artist,year,popularity,link
     * 
     * To read a line, use StdIn.readline(), then use .split(",") to extract 
     * fields from the line.
     * 
     * If the playlist is empty, return a Playlist object with null for its last, 
     * and 0 for its size.
     * 
     * The input file has Songs in decreasing popularity order.
     * 
     * DO NOT implement a sorting method, PRACTICE add to end.
     * 
     * @param filename the playlist information input file
     * @return a Playlist object, which contains a reference to the LAST song 
     * in the ciruclar linkedlist playlist and the size of the playlist.
     */
    public Playlist createPlaylist(String filename) {
    Playlist playlist = new Playlist(null, 0); // Create a new playlist object

    StdIn.setFile(filename); // Open the file

    SongNode lastNode = null;
    int counter = 0;

    while (!StdIn.isEmpty()) {
        // Read each line from the file
        String[] data = StdIn.readLine().split(",");
        String name = data[0];
        String artist = data[1];
        int year = Integer.parseInt(data[2]);
        int pop = Integer.parseInt(data[3]);
        String link = data[4];

        Song song = new Song(name, artist, year, pop, link);
        SongNode newSongNode = new SongNode(song, lastNode);

        if (lastNode == null) {
            // If the list is empty, both the first and last node will point to the new node
            lastNode = newSongNode;
            newSongNode.setNext(newSongNode); // Circular reference
        } else {
            newSongNode.setNext(lastNode.getNext()); // Points the new node to the first node
            lastNode.setNext(newSongNode); // Updates the last node's reference
            lastNode = newSongNode; // New node becomes the last node
        }
        counter++;
    }

    playlist.setLast(lastNode);
    playlist.setSize(counter);

    return playlist;
}

    /**
     * ****DO NOT**** UPDATE THIS METHOD
     * This method is already implemented for you. 
     * 
     * Adds a new playlist into the song library at a certain index.
     * 
     * 1. Calls createPlayList() with a file containing song information.
     * 2. Adds the new playlist created by createPlayList() into the songLibrary.
     * 
     * Note: initialize the songLibrary if it is null
     * 
     * @param filename the playlist information input file
     * @param playlistIndex the index of the location where the playlist will 
     * be added 
     */
    public void addPlaylist(String filename, int playlistIndex) {
        
        /* DO NOT UPDATE THIS METHOD */

        if ( songLibrary == null ) {
            songLibrary = new ArrayList<Playlist>();
        }
        if ( playlistIndex >= songLibrary.size() ) {
            songLibrary.add(createPlaylist(filename));
        } 
        else {
         songLibrary.add(playlistIndex, createPlaylist(filename));
        }        
    }

    /**
     * ****DO NOT**** UPDATE THIS METHOD
     * This method is already implemented for you.
     * 
     * It takes a playlistIndex, and removes the playlist located at that index.
     * 
     * @param playlistIndex the index of the playlist to remove
     * @return true if the playlist has been deleted
     */
    public boolean removePlaylist(int playlistIndex) {
        /* DO NOT UPDATE THIS METHOD */

        if ( songLibrary == null || playlistIndex >= songLibrary.size() ) {
            return false;
        }

        songLibrary.remove(playlistIndex);
            
        return true;
    }
    
    /** 
     * 
     * Adds the playlists from many files into the songLibrary
     * 
     * 1. Initialize the songLibrary
     * 
     * 2. For each of the filenames
     *       add the playlist into songLibrary
     * 
     * The playlist will have the same index in songLibrary as it has in
     * the filenames array. For example if the playlist is being created
     * from the filename[i] it will be added to songLibrary[i]. 
     * Use the addPlaylist() method. 
     * 
     * @param filenames an array of the filenames of playlists that should be 
     * added to the library
     */
    public void addAllPlaylists(String[] filenames) {
        if (songLibrary == null) {
            songLibrary = new ArrayList<Playlist>();
        }
    
        for (String filename : filenames) {
            Playlist playlist = createPlaylist(filename);
    
            if (playlist == null) {
                // The createPlaylist method couldn't create a valid playlist
                playlist = new Playlist(); // Create an empty playlist
            }
    
            songLibrary.add(playlist); // Add the playlist to the end of the library
        }
    }

    /**
     * This method adds a song to a specified playlist at a given position.
     * 
     * The first node of the circular linked list is at position 1, the 
     * second node is at position 2 and so forth.
     * 
     * Return true if the song can be added at the given position within the 
     * specified playlist (and thus has been added to the playlist), false 
     * otherwise (and the song will not be added). 
     * 
     * Increment the size of the playlist if the song has been successfully
     * added to the playlist.
     * 
     * @param playlistIndex the index where the playlist will be added
     * @param position the position inthe playlist to which the song 
     * is to be added 
     * @param song the song to add
     * @return true if the song can be added and therefore has been added, 
     * false otherwise. 
     */
    public boolean insertSong(int playlistIndex, int position, Song song) {
        if (playlistIndex < 0 || playlistIndex >= songLibrary.size()) {
            return false; // Invalid playlist index
        }
    
        Playlist playlist = songLibrary.get(playlistIndex);
    
        if (playlist == null || position < 1 || position > playlist.getSize() + 1) {
            return false; // Invalid position or playlist does not exist
        }
    
        SongNode newNode = new SongNode(song, null);
    
        if (position == 1) {
            // Insert at the beginning
            if (playlist.getLast() == null) {
                // Inserting into an empty playlist
                playlist.setLast(newNode);
                newNode.setNext(newNode); // Make it circular
            } else {
                SongNode last = playlist.getLast();
                newNode.setNext(last.getNext());
                last.setNext(newNode);
            }
        } else if (position == playlist.getSize() + 1) {
            // Insert at the end
            SongNode last = playlist.getLast();
            newNode.setNext(last.getNext());
            last.setNext(newNode);
            playlist.setLast(newNode); // Update the last node
        } else {
            // Insert at a specific position
            SongNode ptr = playlist.getLast().getNext();
            int currentPos = 1;
    
            while (currentPos < position - 1) {
                if (ptr.getNext() == playlist.getLast().getNext()) {
                    return false; // Position is out of bounds
                }
                currentPos++;
                ptr = ptr.getNext();
            }
    
            newNode.setNext(ptr.getNext());
            ptr.setNext(newNode);
        }
    
        // Update the size of the playlist
        playlist.setSize(playlist.getSize() + 1);
    
        return true;
    }
    /**
     * This method removes a song at a specified playlist, if the song exists. 
     *
     * Use the .equals() method of the Song class to check if an element of 
     * the circular linkedlist matches the specified song.
     * 
     * Return true if the song is found in the playlist (and thus has been 
     * removed), false otherwise (and thus nothing is removed). 
     * 
     * Decrease the playlist size by one if the song has been successfully
     * removed from the playlist.
     * 
     * @param playlistIndex the playlist index within the songLibrary where 
     * the song is to be added.
     * @param song the song to remove.
     * @return true if the song is present in the playlist and therefore has 
     * been removed, false otherwise.
     */
    public boolean removeSong(int playlistIndex, Song song) {
    if (playlistIndex < 0 || playlistIndex >= songLibrary.size()) {
        return false; // Invalid playlistIndex
    }

    Playlist playlist = songLibrary.get(playlistIndex);

    if (playlist.getSize() == 0) {
        return false; // The playlist is empty
    }

    SongNode current = playlist.getLast().getNext();
    SongNode prev = playlist.getLast();
    SongNode last = playlist.getLast();
    SongNode first = current;

    int counter = playlist.getSize();
    boolean songRemoved = false;

    if (song.equals(first.getSong())) {
        // If the first song matches the one to remove
        if (first == last) {
            // The playlist only contains one song
            playlist.setLast(null); // Remove the last song
        } else {
            // The first song is not the only one
            prev.setNext(first.getNext()); // Update the previous node
            playlist.setLast(prev); // Update last to the previous node
        }
        counter--;
        playlist.setSize(counter);
        songRemoved = true;
    } else if (song.equals(last.getSong())) {
        // If the last song matches the one to remove
        while (current != last) {
            prev = current;
            current = current.getNext();
        }
        prev.setNext(first); // Make it circular by connecting last to first
        playlist.setLast(prev); // Update last to the previous node
        counter--;
        playlist.setSize(counter);
        songRemoved = true;
    } else {
        while (current != last) {
            if (song.equals(current.getSong())) {
                prev.setNext(current.getNext());
                counter--;
                playlist.setSize(counter);
                songRemoved = true;
                break;
            }
            prev = current;
            current = current.getNext();
        }
    }

    return songRemoved;
}
    

    /**
     * This method reverses the playlist located at playlistIndex
     * 
     * Each node in the circular linked list will point to the element that 
     * came before it.
     * 
     * After the list is reversed, the playlist located at playlistIndex will 
     * reference the first SongNode in the original playlist (new last).
     * 
     * @param playlistIndex the playlist to reverse
     */
    public void reversePlaylist(int playlistIndex) {
        if (playlistIndex < 0 || playlistIndex >= songLibrary.size()) {
            return;
        }
    
        Playlist playlist = songLibrary.get(playlistIndex);
    
        if (playlist.getSize() == 0 || playlist.getSize() == 1) {
            return;
        }
    
        SongNode ptr = playlist.getLast();
        SongNode prev = null;
        SongNode next;
        SongNode newLast = playlist.getLast().getNext();
    
        do {
            next = ptr.getNext();
            ptr.setNext(prev);

            prev = ptr;
            ptr = next;

        } while (ptr != playlist.getLast());

        playlist.getLast().setNext(prev);
        playlist.setLast(newLast);
    }

    /**
     * This method merges two playlists.
     * 
     * Both playlists have songs in decreasing popularity order. The resulting 
     * playlist will also be in decreasing popularity order.
     * 
     * You may assume both playlists are already in decreasing popularity 
     * order. If the songs have the same popularity, add the song from the 
     * playlist with the lower playlistIndex first.
     * 
     * After the lists have been merged:
     *  - store the merged playlist at the lower playlistIndex
     *  - remove playlist at the higher playlistIndex 
     * 
     * 
     * @param playlistIndex1 the first playlist to merge into one playlist
     * @param playlistIndex2 the second playlist to merge into one playlist
     */
    public void mergePlaylists(int playlistIndex1, int playlistIndex2) {
        if (playlistIndex1 < 0 || playlistIndex2 < 0 || playlistIndex1 >= songLibrary.size() || playlistIndex2 >= songLibrary.size()) {
            // Invalid playlist indices, return nothing
            return;
        }
    
        Playlist lowerPlaylist = songLibrary.get(playlistIndex1);
        Playlist higherPlaylist = songLibrary.get(playlistIndex2);
    
        if (lowerPlaylist == null && higherPlaylist == null) {
            // Both playlists are empty, nothing to merge
            return;
        } else if (lowerPlaylist == null) {
            songLibrary.set(playlistIndex1, higherPlaylist);
            songLibrary.remove(playlistIndex2);
            return;
        } else if (higherPlaylist == null) {
            return;
        }
    
        // Get the first songs from both playlists
        SongNode head1 = lowerPlaylist.getSize() > 0 ? lowerPlaylist.getLast().getNext() : null;
        SongNode head2 = higherPlaylist.getSize() > 0 ? higherPlaylist.getLast().getNext() : null;
    
        // Initialize the merged playlist
        SongNode mergedHead = null;
        SongNode mergedTail = null;
    
        while (head1 != lowerPlaylist.getLast() || head2 != higherPlaylist.getLast()) {
            if (mergedHead == null) {
                // Determine if the songs have the same popularity and which playlist to choose
                boolean samePopularity = head1 != null && head2 != null && head1.getSong().getPopularity() == head2.getSong().getPopularity();
                boolean selectFromLower = head1 != null && (head2 == null || (samePopularity && playlistIndex1 < playlistIndex2));
    
                if (selectFromLower) {
                    mergedHead = head1;
                    mergedTail = head1;
                    head1 = head1.getNext();
                } else {
                    mergedHead = head2;
                    mergedTail = head2;
                    head2 = head2.getNext();
                }
            } else {
                // Separate conditional for cases with the same popularity
                boolean samePopularity = head1 != null && head2 != null && head1.getSong().getPopularity() == head2.getSong().getPopularity();
                if (samePopularity) {
                    if (playlistIndex1 < playlistIndex2) {
                        mergedTail.setNext(head1);
                        mergedTail = head1;
                        head1 = head1.getNext();
                    } else {
                        mergedTail.setNext(head2);
                        mergedTail = head2;
                        head2 = head2.getNext();
                    }
                } else {
                    boolean selectFromLower = head1 != null && (head2 == null || 
                        head1.getSong().getPopularity() >= head2.getSong().getPopularity());
    
                    if (selectFromLower) {
                        mergedTail.setNext(head1);
                        mergedTail = head1;
                        head1 = head1.getNext();
                    } else {
                        mergedTail.setNext(head2);
                        mergedTail = head2;
                        head2 = head2.getNext();
                    }
                }
            }
    
            if (head1 == lowerPlaylist.getLast() && head2 == higherPlaylist.getLast()) {
                // Both playlists are exhausted, add the last songs in decreasing popularity order
                if (head1.getSong().getPopularity() >= head2.getSong().getPopularity()) {
                    mergedTail.setNext(head1);
                    mergedTail = head1;
                    mergedTail.setNext(head2);
                    mergedTail = head2;
                } else {
                    mergedTail.setNext(head2);
                    mergedTail = head2;
                    mergedTail.setNext(head1);
                    mergedTail = head1;
                }
                break;
            }
        }
    
        // Connect the remaining songs from both playlists
        mergedTail.setNext(lowerPlaylist.getLast().getNext());
        lowerPlaylist.setLast(mergedTail);
        lowerPlaylist.setSize(lowerPlaylist.getSize() + higherPlaylist.getSize());
    
        songLibrary.set(playlistIndex1, lowerPlaylist);
        songLibrary.remove(playlistIndex2);
    }
    
    
    
    
    
    
    
    /**
     * This method shuffles a specified playlist using the following procedure:
     * 
     * 1. Create a new playlist to store the shuffled playlist in.
     * 
     * 2. While the size of the original playlist is not 0, randomly generate a number 
     * using StdRandom.uniformInt(1, size+1). Size contains the current number
     * of items in the original playlist.
     * 
     * 3. Remove the corresponding node from the original playlist and insert 
     * it into the END of the new playlist (1 being the first node, 2 being the 
     * second, etc). 
     * 
     * 4. Update the old playlist with the new shuffled playlist.
     *    
     * @param index the playlist to shuffle in songLibrary
     */
    
     
     public void shufflePlaylist(int playlistIndex) {
        if (playlistIndex < 0 || playlistIndex >= songLibrary.size()) {
            return; // Invalid playlist index
        }
    
        Playlist original = songLibrary.get(playlistIndex);
        int size = original.getSize();
    
        // Create a new playlist to store the shuffled playlist
        Playlist shuffled = new Playlist();
        int counter = 0;
    
        // Shuffle the playlist
        while (size != 0) {
            int randomIndex = StdRandom.uniformInt(size + 1);
    
            // Find the node at the random index
            SongNode selectedNode = original.getLast().getNext();
            SongNode previousNode = null;
            
            for (int i = 0; i < randomIndex; i++) {
                previousNode = selectedNode;
                selectedNode = selectedNode.getNext();
            }
    
            if (selectedNode == original.getLast()) {
                original.setLast(previousNode);
            }
    
            if (previousNode != null) {
                previousNode.setNext(selectedNode.getNext());
            } else {
                original.setLast(selectedNode.getNext());
            }
    
            // Insert the selected node into the end of the shuffled playlist
            if (shuffled.getLast() == null) {
                shuffled.setLast(selectedNode);
                selectedNode.setNext(selectedNode);
            } else {
                selectedNode.setNext(shuffled.getLast().getNext());
                shuffled.getLast().setNext(selectedNode);
                shuffled.setLast(selectedNode);
                
            }
            counter++;
            size--;
        }
        shuffled.setSize(counter);
    
        // Update the original playlist
        songLibrary.set(playlistIndex, shuffled);
    }

     /**
     * This method sorts a specified playlist using linearithmic sort.
     * 
     * Set the playlist located at the corresponding playlistIndex
     * in decreasing popularity index order.
     * 
     * This method should  use a sort that has O(nlogn), such as with merge sort.
     * 
     * @param playlistIndex the playlist to shuffle
     */
    public void sortPlaylist ( int playlistIndex ) {
        // WRITE YOUR CODE HERE
        
    }

    /**
     * ****DO NOT**** UPDATE THIS METHOD
     * Plays playlist by index; can use this method to debug.
     * 
     * @param playlistIndex the playlist to print
     * @param repeats number of times to repeat playlist
     * @throws InterruptedException
     */
    public void playPlaylist(int playlistIndex, int repeats) {
        /* DO NOT UPDATE THIS METHOD */

        final String NO_SONG_MSG = " has no link to a song! Playing next...";
        if (songLibrary.get(playlistIndex).getLast() == null) {
            StdOut.println("Nothing to play.");
            return;
        }

        SongNode ptr = songLibrary.get(playlistIndex).getLast().getNext(), first = ptr;

        do {
            StdOut.print("\r" + ptr.getSong().toString());
            if (ptr.getSong().getLink() != null) {
                StdAudio.play(ptr.getSong().getLink());
                for (int ii = 0; ii < ptr.getSong().toString().length(); ii++)
                    StdOut.print("\b \b");
            }
            else {
                StdOut.print(NO_SONG_MSG);
                try {
                    Thread.sleep(2000);
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
                for (int ii = 0; ii < NO_SONG_MSG.length(); ii++)
                    StdOut.print("\b \b");
            }

            ptr = ptr.getNext();
            if (ptr == first) repeats--;
        } while (ptr != first || repeats > 0);
    }

    /**
     * ****DO NOT**** UPDATE THIS METHOD
     * Prints playlist by index; can use this method to debug.
     * 
     * @param playlistIndex the playlist to print
     */
    public void printPlaylist(int playlistIndex) {
        StdOut.printf("%nPlaylist at index %d (%d song(s)):%n", playlistIndex, songLibrary.get(playlistIndex).getSize());
        if (songLibrary.get(playlistIndex).getLast() == null) {
            StdOut.println("EMPTY");
            return;
        }
        SongNode ptr;
        for (ptr = songLibrary.get(playlistIndex).getLast().getNext(); ptr != songLibrary.get(playlistIndex).getLast(); ptr = ptr.getNext() ) {
            StdOut.print(ptr.getSong().toString() + " -> ");
        }
        if (ptr == songLibrary.get(playlistIndex).getLast()) {
            StdOut.print(songLibrary.get(playlistIndex).getLast().getSong().toString() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    public void printLibrary() {
        if (songLibrary.size() == 0) {
            StdOut.println("\nYour library is empty!");
        } else {
                for (int ii = 0; ii < songLibrary.size(); ii++) {
                printPlaylist(ii);
            }
        }
    }

    /*
     * Used to get and set objects.
     * DO NOT edit.
     */
     public ArrayList<Playlist> getPlaylists() { return songLibrary; }
     public void setPlaylists(ArrayList<Playlist> p) { songLibrary = p; }
}
