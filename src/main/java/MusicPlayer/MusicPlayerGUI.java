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
    
    public JMenuBar menuBar;
    public JMenu menu;
    public JMenuItem openFileMenuItem;
    
    public JFileChooser audioFileChooser;
    
    public JPanel dataPanel;
    public JPanel controlPanel;

    public JButton playButton;
    public JButton stopButton;
    public JButton pauseButton;
    public JLabel fileLabel;
    public JLabel fileName; 
    
    public JProgressBar timeBar;
    public MusicPlayerController mpc;
    
    
    MusicPlayerGUI(){
        this.configureGUIView(); 
        this.mpc = new MusicPlayerController();
        this.mpc.attachGUIInstance(this);
    }
    
    public static void main(String[] args){
        new MusicPlayerGUI();
    }
    
    private final void configureGUIView(){
        
        this.setTitle(MusicPlayerGUI.GUI_TITLE);
        this.setSize(new Dimension(MusicPlayerGUI.INITIAL_GUI_HEIGHT, MusicPlayerGUI.INITIAL_GUI_WIDTH));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.createGUIElements();
        this.setActionListeners();
        
        //set initial button states to false
        this.playButton.setEnabled(false);
        this.pauseButton.setEnabled(false);
        this.stopButton.setEnabled(false);
        
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
    
    private void createGUIElements(){
        this.menuBar = new JMenuBar();
        this.menu = new JMenu(MusicPlayerGUI.FILE_MENU_NAME);
        this.openFileMenuItem = new JMenuItem(MusicPlayerGUI.OPEN_FILE_MENU_ITEM_TEXT);
        
        this.menu.add(this.openFileMenuItem);
        this.menuBar.add(this.menu);
        this.setJMenuBar(this.menuBar);
        
        this.audioFileChooser = new JFileChooser(System.getProperty("user.home"));
        
        this.dataPanel = new JPanel();
        this.controlPanel = new JPanel();
        
        this.playButton = new JButton(MusicPlayerGUI.PLAY_BUTTON_TEXT);
        this.stopButton = new JButton(MusicPlayerGUI.STOP_BUTTON_TEXT);
        this.pauseButton = new JButton(MusicPlayerGUI.PAUSE_BUTTON_TEXT);
        
        this.fileLabel = new JLabel(MusicPlayerGUI.FILE_LABEL);
        this.fileName = new JLabel(MusicPlayerGUI.DEFAULT_LABEL_VALUE);
        
        this.timeBar = new JProgressBar();
    }
    
    private void setActionListeners(){
    
        //set actionlistener callbacks
        this.openFileMenuItem.addActionListener( new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e){
                int returnCode = audioFileChooser.showOpenDialog(menu);
                
                if(returnCode == JFileChooser.APPROVE_OPTION){
                    String selectedFile = audioFileChooser.getSelectedFile().getPath();
                    if(selectedFile.contains("wav")){
                        
                        playButton.setEnabled(true);
                        pauseButton.setEnabled(false);
                        stopButton.setEnabled(false);
                        
                        mpc.loadAudio(selectedFile);
                        fileName.setText(mpc.getAudioFileName());
                        setProgressBarInitialState();

                    } else {
                        System.out.println("Unsupported file type selected: " + selectedFile);
                    }
                }
                
                
            }
        });
        
        this.playButton.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e){
                mpc.playAudio();
                
                playButton.setEnabled(false);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
                
            }
        });
        
        this.pauseButton.addActionListener( new ActionListener(){
            boolean playState = true;
            
            @Override
            public void actionPerformed(ActionEvent e){
                if(playState == true){
                    mpc.pauseAudio();
                    playState = false;
                } else{
                    mpc.resumeAudio();
                    playState = true;
                }
            }
        
        });
        
        this.stopButton.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e){

                mpc.stopAudio();
                
                playButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(false);
            }
        });
    }
    
    private void setProgressBarInitialState(){
        long maxBound = this.mpc.getAudioPlayTime();
        
        this.timeBar.setMinimum(0);
        this.timeBar.setMaximum((int)maxBound);
        
        this.timeBar.setValue(0);
    }
    
}
