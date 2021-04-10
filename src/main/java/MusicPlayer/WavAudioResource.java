/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author KuRi
 */
public class WavAudioResource extends AudioResource {
    private final static long SEC_TO_MICROSEC_CONV = 1000000;
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
    
    public Clip getClipObj(){
        return this.clip;
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
        
        long hours = (this.playTime/ SEC_TO_MICROSEC_CONV) / 3600;
        long mins = ((this.playTime/ SEC_TO_MICROSEC_CONV) /60) % 60;
        long secs = (this.playTime / SEC_TO_MICROSEC_CONV) % 60;
        String playTimeStr = String.format("%d seconds (%d hrs %d min %d sec)",(long)this.playTime/SEC_TO_MICROSEC_CONV,hours,mins,secs);
        
        String generalInfo = String.format("File Name: %s\nFile Path: %s\nPlay time: %s\nEncoding Type: %s\nNumber of Channels: %d",
                                           this.fileName,
                                           this.audioResourceFilePath,
                                           playTimeStr,
                                           this.encodingType,
                                           this.chanNum
                );

        String sampleInfo = String.format("Sample Size: %d Bytes\nSample Rate: %f Hz",
                                         this.sampleSize,
                                         this.sampleRate                              
        );

        String frameInfo = String.format("Frame Size:%d Bytes\nFrame Rate: %f Hz\nNumber of Frames: %d",
                                         this.frameSize,
                                         this.frameRate,
                                         this.numFrames
        );  
        
        String mediaInfo = String.format("Author: %s\nTitle: %s\nDate Released: %s\n\n",
                                        this.author,
                                        this.title,
                                        this.dateReleased
        );


        System.out.println(generalInfo); 
        System.out.println(sampleInfo); 
        System.out.println(frameInfo);
        System.out.println(mediaInfo);
    }
    
    @Override
    public MediaDetails getMediaDetails(){
        return new MediaDetails.MediaDetailsBuilder(this.audioResourceFilePath,this.fileName,this.fileTypeExtension).build();
    }
    
    @Override
    public long getMicrosecondPosition(){
        if(this.isIniatialized() == false){
            throw new IllegalStateException("Object is not initialized.");
        } 
        
        return this.clip.getMicrosecondPosition();
    }
    
    
    @Override
    public void loadNewResource(String filePath){
    
        File audioFile;
        
        try{
            audioFile = new File(filePath);
            
            if(audioFile.exists()){
                this.audioResourceFilePath = filePath;
                
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
     * Set the media position to specified value in microseconds
     * @param timePos Time position in media
     */
    @Override
    public void setMicrosecondPosition(long timePos){
        if(this.isIniatialized() == false){
            throw new IllegalStateException("Object is not initialized.");
        } 
        
        this.clip.setMicrosecondPosition(timePos); //set to beginning of audio data
    }
    
    @Override
    public void setVolume(float newVolumeLevel){
        if(this.isIniatialized() == false){
            throw new IllegalStateException("Object is not initialized.");
        } 
        
        FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        
        float newGain = 20* (float)Math.log10(newVolumeLevel/100);
        gainControl.setValue(newGain);
    }
    
    /**
     * Return a boolean that indicates whether the WavAudioResource is fully initialized
     * @return boolean
     */
    @Override
    public boolean isIniatialized(){
        return this.isInitialized;
    }
    
    @Override
    public boolean isPlaying(){
        if(this.isIniatialized() == false){
            throw new IllegalStateException("Object is not initialized.");
        } 
        
        return this.clip.isRunning();
    }
    
    //private functions
    
    private void populateAudioDataFields(){
        this.fileName = this.audioResourceFilePath.replaceAll("^.*[\\/\\\\]", "");
        
        this.AudioFmt = this.audioIn.getFormat();
        
        this.chanNum = this.AudioFmt.getChannels();
        this.frameSize = this.AudioFmt.getFrameSize();
        this.sampleSize = this.AudioFmt.getSampleSizeInBits();
        this.sampleRate = this.AudioFmt.getSampleRate(); 
        this.frameRate = this.AudioFmt.getFrameRate();
        this.numFrames = this.audioIn.getFrameLength();
        
        this.playTime = this.clip.getMicrosecondLength();
        
        AudioFormat.Encoding codeType = this.AudioFmt.getEncoding();
        this.encodingType = codeType.toString();
        
        Map<String,Object> props = this.audioFileFormat.properties();

        this.author = (String) props.getOrDefault(this.AudioProperties[1], "");
        this.title = (String) props.getOrDefault(this.AudioProperties[2], "");
        this.dateReleased = (String) props.getOrDefault(this.AudioProperties[3], "") ;
    }
    
}
