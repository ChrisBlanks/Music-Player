/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author KuRi
 */
public class MediaDisplayPanel extends JPanel {
    final static int DEFAULT_WIDTH = 50;
    final static int DEFAULT_HEIGHT = 100;
    
    final static Color DEFAULT_PANE_BG_COLOR = new Color(0,0,0);
    final static Color DEFAULT_COMPONENT_BG_COLOR = new Color(255,255,255);
    
    final static String DEFAULT_ART_IMAGE_RESOURCE = "images/default_art.jpg";
    
    Map<String,String> songMap;
    DefaultListModel<String> songListCollection;
    JList songList;
    JScrollPane songScroller;
    JPanel detailsPanel;
    JLabel songDetailLabel;
    
    MediaDisplayPanel(){
        this.songMap = new HashMap<>();
        createGUIComponents(null);
    }
    
    MediaDisplayPanel(Color componentBGColor){
       createGUIComponents(componentBGColor); 
    }
    
    public static void main(String[] args){
        
        java.awt.EventQueue.invokeLater(new Runnable(){

            @Override
            public void run(){
                JFrame frame = new JFrame();
                frame.setSize(600, 600);
                MediaDisplayPanel test = new MediaDisplayPanel(); 
                test.addSongNameToList("1","Random");
                test.addSongNameToList("2","first");
                test.addSongNameToList("3","second");
                test.addSongNameToList("4","Random");
                test.addSongNameToList("5","first");
                test.addSongNameToList("6","second");
                test.addSongNameToList("7","Random");
                test.addSongNameToList("8","first");
                test.addSongNameToList("9","second");
                test.addSongNameToList("10","Random");
                test.addSongNameToList("11","first");
                test.addSongNameToList("12","second");
                
                frame.add(test);
                frame.setVisible(true);
            }
        });

    }
    
    private void createGUIComponents(Color newColor){
        Color bgColor = MediaControlPanel.DEFAULT_COMPONENT_BG_COLOR;
        if(newColor != null){
            bgColor = newColor;
        }
        
        this.songListCollection = new DefaultListModel<>();
        
        this.songList = new JList<>(this.songListCollection);
        this.songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.songList.setLayoutOrientation(JList.VERTICAL);
        
        this.songScroller = new JScrollPane();
        this.songScroller.setViewportView(this.songList);
        this.songScroller.setPreferredSize(new Dimension(MediaDisplayPanel.DEFAULT_WIDTH, MediaDisplayPanel.DEFAULT_HEIGHT));
        this.detailsPanel = new JPanel(new GridLayout(2,1));
        this.songDetailLabel = new JLabel("No song selected");
        
        this.detailsPanel.add(this.songDetailLabel);
        
        this.setLayout(new GridLayout(1,2));
        this.add(this.songScroller);
        this.add(this.detailsPanel);
    }
    
    //public API
    
    /**
     * Adds song name to JList to be displayed in GUI. Creates an entry in the HashMap
     * to keep track of file paths for each song name.
     * @param filePath File path of file of song
     * @param songName Name of song
     */
    public void addSongNameToList(String filePath, String songName){
        this.songMap.put(filePath, songName);
        this.songListCollection.addElement(songName);
        
        //set last added song as the selected index
        this.songList.setSelectedIndex(this.songListCollection.getSize() - 1);
    }
    
    public void addListSelectionListenerToList(ListSelectionListener listener){
        this.songList.addListSelectionListener(listener);
    }
    
    public void displaySongDetails(String details){
        this.songDetailLabel.setText(details);
    }
    
    public int getCurrentSelectedIndex(){
        return this.songList.getSelectedIndex();
    }
    
    /**
     * Retrieves file path of song based on song name argument. Returns null if
     * song name doesn't exist in the internal HashMap.
     * 
     * Note: Current implementation may return the wrong result if there are duplicate
     * song names.
     * 
     * @param songName
     * @return 
     */
    public String getSongFilePath(String songName){
        String result = null;
        
        if(this.songMap.containsValue(songName)){
            for(Entry<String,String> entry: this.songMap.entrySet()){
                if(songName.equals(entry.getValue())){
                    result = entry.getKey();
                }
            }
            
        }
        return result;
    }
    
    /**
     * Remove song name to JList
     * @param songName Name of song
     */
    public void removeSongFromList(String songName){
        
        if(this.songMap.containsValue(songName)){
            String key;
            for(Entry<String,String> entry: this.songMap.entrySet()){
                if(songName.equals(entry.getValue())){
                    key = entry.getKey();
                    this.songMap.remove(key);
                }
            }
            this.songListCollection.removeElement(songName);
        }
    }
    
    /**
     * Sorts list of song names displayed in JList
     * @param isAscendingOrder If true, sorts in ascending order. If false, descending order
     */
    public void sortSongNameList(boolean isAscendingOrder){

    }
    
    
}
