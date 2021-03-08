package MusicPlayer;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.swing.*;

/**
 *
 * @author KuRi
 */
public class MusicPlayer extends JFrame{
    
    public static final int GUI_HEIGHT = 300;
    public static final int GUI_WIDTH = 300;
    
    public static final String GUI_TITLE = "Music Player";
    public static final String DEMO_FILE = "C:/Users/KuRi/Downloads/StarWars60.wav";
    
    private static final String FILE_MENU_NAME = "File";
    private static final String OPEN_FILE_MENU_ITEM_TEXT = "Open a file";
    
    private static final String PLAY_BUTTON_TEXT = "Play";
    private static final String PAUSE_BUTTON_TEXT = "Pause" ;
    private static final String STOP_BUTTON_TEXT = "Stop" ;
    
    private static final String FILE_LABEL = "Audio File:";
    private static final String DEFAULT_LABEL_VALUE = "Null";
    
    private Clip clip;
    
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
    
    public MusicPlayer(){
       this.configureGUIView(); 
       this.loadAudioFile(DEMO_FILE);
    }
    
    public MusicPlayer(String audioFile){
       this.configureGUIView(); 
       this.loadAudioFile(audioFile);
    }
    
    /**
     * Configures swing GUI
     *
     */
    
    public final void configureGUIView(){
        
        this.setTitle(GUI_TITLE);
        this.setSize(new Dimension(GUI_HEIGHT,GUI_WIDTH));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.menuBar = new JMenuBar();
        this.menu = new JMenu(this.FILE_MENU_NAME);
        this.openFileMenuItem = new JMenuItem(this.OPEN_FILE_MENU_ITEM_TEXT);
        
        this.menu.add(this.openFileMenuItem);
        this.menuBar.add(this.menu);
        this.setJMenuBar(this.menuBar);
        
        this.audioFileChooser = new JFileChooser();
        
        this.dataPanel = new JPanel();
        this.controlPanel = new JPanel();
        
        this.playButton = new JButton(MusicPlayer.PLAY_BUTTON_TEXT);
        this.stopButton = new JButton(MusicPlayer.STOP_BUTTON_TEXT);
        this.pauseButton = new JButton(MusicPlayer.PAUSE_BUTTON_TEXT);
        
        this.fileLabel = new JLabel(MusicPlayer.FILE_LABEL);
        this.fileName = new JLabel(MusicPlayer.DEFAULT_LABEL_VALUE);
        
        
        this.openFileMenuItem.addActionListener( new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e){
                int returnCode = audioFileChooser.showOpenDialog(menu);
                
                if(returnCode == JFileChooser.APPROVE_OPTION){
                    String selectedFile = audioFileChooser.getSelectedFile().getPath();
                    System.out.println(selectedFile);
                    if(selectedFile.contains("wav")){
                        discardAudio();
                        
                        playButton.setEnabled(true);
                        pauseButton.setEnabled(false);
                        stopButton.setEnabled(false);
                        
                        loadAudioFile(selectedFile);
                    }
                }
                
                
            }
        });
        
        playButton.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e){
                playAduio();
                playButton.setEnabled(false);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
                
            }
        });
        
        pauseButton.addActionListener( new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e){
                toggleAudio();
            }
        
        });
        
        stopButton.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e){
                stopAudio();
                playButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(false);
            }
        });
        
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        
        this.dataPanel.add(this.fileLabel);
        this.dataPanel.add(this.fileName);
        
        this.controlPanel.add(this.playButton);
        this.controlPanel.add(this.pauseButton);
        this.controlPanel.add(this.stopButton);
        
        this.add(this.dataPanel);
        this.add(this.controlPanel);
        
        this.setLayout( new GridLayout(2,1) );
        
        this.setVisible(true);
    }
    
    /**
     * Extracts audio format information and writes info to display.
     * @param audio Object to unpack audio format information from
     */
    public void displayAudioDetails(AudioInputStream audio){
        AudioFormat fmt = audio.getFormat();
        
        int chanNum = fmt.getChannels();
        int frameSize = fmt.getFrameSize();
        int sampleSize = fmt.getSampleSizeInBits();
        float sampleRate = fmt.getSampleRate(); 
        float frameRate = fmt.getFrameRate();
        long numFrames = audio.getFrameLength();
        
        Encoding codeType = fmt.getEncoding();
        
        String generalInfo = String.format("Number of Channels: %d\nEncoding Type: %s",
                                           chanNum,
                                           codeType.toString()
                );

        String sampleInfo = String.format("Sample Size: %d Bytes\nSample Rate: %f Hz",
                                         sampleSize,
                                         sampleRate                              
        );

        String frameInfo = String.format("Frame Size:%d Bytes\nFrame Rate: %f Hz\nNumber of Frames: %d\n\n",
                                         frameSize,
                                         frameRate,
                                         numFrames
        );  


        System.out.println(generalInfo); 
        System.out.println(sampleInfo); 
        System.out.println(frameInfo);
       
    }
    
    /**
     * Loads audio file for later playback
     * @param audioPath Path to audio file resource
     */
    public final void loadAudioFile(String audioPath){
        File audioFile;
        
        try{
            audioFile = new File(audioPath);
            if(audioFile.exists()){
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile);
                AudioSystem.getAudioFileFormat(audioFile);

                System.out.println(String.format("Audio file: %s",audioPath));
                this.fileName.setText(audioPath);
                displayAudioDetails(audioIn);

                if(this.clip == null){
                    this.clip = AudioSystem.getClip();
                }

                clip.open(audioIn);
            } else{
                System.out.println("File does not exist: "+ audioPath + "\n");
            }
            
        } catch(UnsupportedAudioFileException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(LineUnavailableException e){
            e.printStackTrace();
        }
        
    }
    
    
    public static void main(String[] args){
        new MusicPlayer();
    }
   
    public void discardAudio(){
        if(this.clip == null){
            System.out.println("Audio hasn't been loaded.");
            return;
        }  
        
        this.clip.close();
        this.clip = null;
    }
    
    public void toggleAudio(){
        if(this.clip == null){
            System.out.println("Audio hasn't been started");
            return;
        }
        
        if(this.clip.isRunning() ){
            this.clip.stop(); //pause if audio is running
            System.out.println("Audio was paused.");
        } else if(this.clip.isOpen()) {
            this.clip.start(); //resume audio if not running and open 
            System.out.println("Audio was resumed.");
        } 
        
    }
    
    /**
     * Starts playing specified audio file
     */
    public void playAduio(){
        
        if(this.clip == null || this.clip.isOpen() == false){
            System.out.println("Audio not open.");
            return;
        }
        
        clip.start();
        System.out.println("Audio was started."); 
    }
    
    public void stopAudio(){
        if(this.clip == null){
            System.out.println("Audio hasn't been started");
            return;
        }
        
        this.clip.stop();
        this.clip.setMicrosecondPosition(0); //set to beginning of audio data
        
        System.out.println("Audio was stopped.");
    }
    
    
}
