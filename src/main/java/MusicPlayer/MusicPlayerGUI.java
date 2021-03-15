/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author KuRi
 */
public class MusicPlayerGUI extends JFrame {
    
    public static final int INITIAL_GUI_HEIGHT = 300;
    public static final int INITIAL_GUI_WIDTH = 300;
    
    public static final String GUI_TITLE = "Music Player";
    
    private static final String FILE_MENU_NAME = "File";
    private static final String OPEN_FILE_MENU_ITEM_TEXT = "Open a file";
    
    private static final String PLAY_BUTTON_TEXT = "Play";
    private static final String PAUSE_BUTTON_TEXT = "Pause" ;
    private static final String STOP_BUTTON_TEXT = "Stop" ;
    
    private static final String FILE_LABEL = "Audio File:";
    private static final String DEFAULT_LABEL_VALUE = "Null";
    
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem openFileMenuItem;
    
    private JFileChooser audioFileChooser;
    
    private JPanel dataPanel;
    private JPanel controlPanel;

    private JButton playButton;
    private JButton stopButton;
    private JButton pauseButton;
    private JLabel fileLabel;
    private JLabel fileName; 
    
    private JProgressBar timeBar;
    private MusicPlayerController mpc;
    
    MusicPlayerGUI(){
        this.configureGUIView(); 
        this.mpc = new MusicPlayerController();
    }
    
    private final void configureGUIView(){
        
        this.setTitle(MusicPlayerGUI.GUI_TITLE);
        this.setSize(new Dimension(MusicPlayerGUI.INITIAL_GUI_HEIGHT, MusicPlayerGUI.INITIAL_GUI_WIDTH));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.menuBar = new JMenuBar();
        this.menu = new JMenu(MusicPlayerGUI.FILE_MENU_NAME);
        this.openFileMenuItem = new JMenuItem(MusicPlayerGUI.OPEN_FILE_MENU_ITEM_TEXT);
        
        this.menu.add(this.openFileMenuItem);
        this.menuBar.add(this.menu);
        this.setJMenuBar(this.menuBar);
        
        this.audioFileChooser = new JFileChooser();
        
        this.dataPanel = new JPanel();
        this.controlPanel = new JPanel();
        
        this.playButton = new JButton(MusicPlayerGUI.PLAY_BUTTON_TEXT);
        this.stopButton = new JButton(MusicPlayerGUI.STOP_BUTTON_TEXT);
        this.pauseButton = new JButton(MusicPlayerGUI.PAUSE_BUTTON_TEXT);
        
        this.fileLabel = new JLabel(MusicPlayerGUI.FILE_LABEL);
        this.fileName = new JLabel(MusicPlayerGUI.DEFAULT_LABEL_VALUE);
        
        this.timeBar = new JProgressBar();
        
        //set actionlistener callbacks
        this.openFileMenuItem.addActionListener( new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e){
                int returnCode = audioFileChooser.showOpenDialog(menu);
                
                if(returnCode == JFileChooser.APPROVE_OPTION){
                    String selectedFile = audioFileChooser.getSelectedFile().getPath();
                    if(selectedFile.contains("wav")){
                        /*
                        if(clip != null){
                            discardAudio();
                        }
                        
                        playButton.setEnabled(true);
                        pauseButton.setEnabled(false);
                        stopButton.setEnabled(false);
                        
                        loadAudioFile(selectedFile);
                        */
                    } else {
                    
                        System.out.println("Unsupported file type selected: " + selectedFile);
                    }
                }
                
                
            }
        });
        
        playButton.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e){
                //playAduio();
                
                playButton.setEnabled(false);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
                
            }
        });
        
        pauseButton.addActionListener( new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e){
                //toggleAudio();
            }
        
        });
        
        stopButton.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e){
                //stopAudio();
                playButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(false);
            }
        });
        
        //set initial button states to false
        this.playButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        
        this.dataPanel.add(this.fileLabel);
        this.dataPanel.add(this.fileName);
        
        this.controlPanel.add(this.playButton);
        this.controlPanel.add(this.pauseButton);
        this.controlPanel.add(this.stopButton);
        this.controlPanel.add(this.timeBar);
        
        this.add(this.dataPanel);
        this.add(this.controlPanel);
        
        this.setLayout( new GridLayout(2,1) );
        
        this.setVisible(true);
    }
    
    private void setProgressBarInitialState(){
        /*
        long maxBound = this.clip.getMicrosecondLength();
        
        this.timeBar.setMinimum(0);
        this.timeBar.setMaximum((int)maxBound);
        
        this.timeBar.setValue(0);
        */
    }
    
}
