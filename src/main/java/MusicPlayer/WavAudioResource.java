/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author KuRi
 */
public class WavAudioResource extends AudioResource {
    
    private boolean isInitialized;
    
    private Clip clip;
    private AudioInputStream audioIn;
    private AudioFormat AudioFmt;
    private AudioFileFormat audioFileFormat;
    
    final String AudioProperties[] = {"duration","author","title","date"};
    
    /**
     * Creates an object with all fields
     */
    public WavAudioResource(){
        this.audioResourceFilePath = "";
        
        this.author = "";
        this.title = "";
        this.album = "";
        this.genre= "";
        this.dateReleased = "";
        this.fileTypeExtension = ".wav";
        this.trackNumber = 0;

        this.chanNum = 0;
        this.frameSize = 0;
        this.sampleSize = 0;
        this.sampleRate = 0; 
        this.frameRate = 0;
        this.numFrames = 0;
        this.playTime = 0;
        this.encodingType = "";
        
        this.isInitialized = false;
    }
    
    @Override
    public void closeResource(){
        if(this.isIniatialized() == false){
            throw new IllegalStateException("Object is not initialized.");
        }  
        
        this.clip.close();
        this.isInitialized = false; 
    }
    
    
    @Override
    public void displayAudioDetails(){
        if(this.isIniatialized() == false){
            throw new IllegalStateException("Object is not initialized.");
        } 
        
        String generalInfo = String.format("Number of Channels: %d\nEncoding Type: %s",
                                           this.chanNum,
                                           this.encodingType
                );

        String sampleInfo = String.format("Sample Size: %d Bytes\nSample Rate: %f Hz",
                                         this.sampleSize,
                                         this.sampleRate                              
        );

        String frameInfo = String.format("Frame Size:%d Bytes\nFrame Rate: %f Hz\nNumber of Frames: %d\n\n",
                                         this.frameSize,
                                         this.frameRate,
                                         this.numFrames
        );  


        System.out.println(generalInfo); 
        System.out.println(sampleInfo); 
        System.out.println(frameInfo);
    }
    
    
    @Override
    public void loadNewResource(String filePath){
    
        File audioFile;
        
        try{
            audioFile = new File(filePath);
            
            if(audioFile.exists()){
                this.audioIn = AudioSystem.getAudioInputStream(audioFile);
                this.audioFileFormat = AudioSystem.getAudioFileFormat(audioFile);

                System.out.println(String.format("Audio file: %s",filePath));

                this.clip = AudioSystem.getClip();

                this.clip.open(this.audioIn);
                this.isInitialized = true;
                
                this.populateAudioDataFields();
                
            } else{
                System.out.println("File does not exist: "+ filePath + "\n");
            }
            
        } catch(UnsupportedAudioFileException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(LineUnavailableException e){
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void playAudio(){
        if(this.isIniatialized() == false){
            throw new IllegalStateException("Object is not initialized.");
        } 
        
        clip.start();
        System.out.println("Audio was started."); 
        
    }
    
    /**
     * Stop audio playback and return to initial position.
     */
    @Override
    public void stopAudio(){
        if(this.isIniatialized() == false){
            throw new IllegalStateException("Object is not initialized.");
        } 
        
        if(this.clip.isOpen() && this.clip.isRunning() ){
            this.clip.stop();
            this.clip.setMicrosecondPosition(0); //set to beginning of audio data

            System.out.println("Audio was stopped.");
        }
        
    }
    
    /**
     * Stop audio playback at current position.
     */
    @Override
    public void pauseAudio(){
        if(this.isIniatialized() == false){
            throw new IllegalStateException("Object is not initialized.");
        } 
        
        if(this.clip.isOpen() && this.clip.isRunning() ){  //pause audio if open and running
            this.clip.stop(); 
            System.out.println("Audio was paused.");
        }
        
    }
    
    /**
     * Resume audio playback at current position.
     */
    @Override
    public void resumeAudio(){
        if(this.isIniatialized() == false){
            throw new IllegalStateException("Object is not initialized.");
        } 
        
        if(this.clip.isOpen() && this.clip.isRunning() == false) {  //resume audio if open, but not running 
            this.clip.start(); 
            System.out.println("Audio was resumed.");
        } 
        
    }
    
    /**
     * Return a boolean that indicates whether the WavAudioResource is fully initialized
     * @return boolean
     */
    public boolean isIniatialized(){
        return this.isInitialized;
    }
    
    //private functions
    
    private void populateAudioDataFields(){
    
        this.AudioFmt = this.audioIn.getFormat();
        
        this.chanNum = this.AudioFmt.getChannels();
        this.frameSize = this.AudioFmt.getFrameSize();
        this.sampleSize = this.AudioFmt.getSampleSizeInBits();
        this.sampleRate = this.AudioFmt.getSampleRate(); 
        this.frameRate = this.AudioFmt.getFrameRate();
        this.numFrames = this.audioIn.getFrameLength();
        
        AudioFormat.Encoding codeType = this.AudioFmt.getEncoding();
        this.encodingType = codeType.toString();
        
        //To-Do: Populate "media" fields
        Map<String,Object> props = this.audioFileFormat.properties();

        this.playTime = (long) props.getOrDefault(this.AudioProperties[0],0);
        this.author = (String) props.getOrDefault(this.AudioProperties[1], "");
        this.title = (String) props.getOrDefault(this.AudioProperties[2], "");
        this.dateReleased = (String) props.getOrDefault(this.AudioProperties[3], "") ;
    }
    
}