/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 *
 * @author KuRi
 */
public class MusicPlayerController {
    private static final String NO_SONG_SELECTION = "";
    
    private Map<String,AudioResource> audioResourceMap;
    private MusicPlayerGUI mpgObj;
    
    public String selectedSongKey = MusicPlayerController.NO_SONG_SELECTION;
    
    int currentResourceIndex = -1;
    
    MusicPlayerController(){
        this.audioResourceMap = new HashMap<>();
    }
    
    public void attachGUIInstance(MusicPlayerGUI mpg){
        this.mpgObj = mpg;
    }
    
    public void attachLineListener(LineListener listener){
        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null  && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            AudioResource temp = this.audioResourceMap.get(this.selectedSongKey);

            if(temp.fileTypeExtension.equalsIgnoreCase(".wav") || temp.fileTypeExtension.equalsIgnoreCase(".wave") ){
                WavAudioResource wavTemp = (WavAudioResource)temp;
                Clip clipTemp = wavTemp.getClipObj();
                clipTemp.addLineListener(listener);
            } else{
                System.out.println("Unsupported file type. Line listener was not attached.");
            }
        }
    }
    
    public void closeAudio(){

        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            //close/discard of audio resource
            this.audioResourceMap.get(this.selectedSongKey).closeResource();
        }
    }
    
    public long getAudioPlayTime(){
        
        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            return this.audioResourceMap.get(this.selectedSongKey).playTime;
        } else{
            return 0;
        }
    }
    
    public String getAudioFileName(){
        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null &&  this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            return this.audioResourceMap.get(this.selectedSongKey).fileName;
        } else{
            return null;
        }
        
    }
    
    public String getAudioFilePath(){
        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            return this.audioResourceMap.get(this.selectedSongKey).audioResourceFilePath;
        } else{
            return null;
        }
    }
    
    public boolean loadAudio(String audioFilePath){
        boolean isSuccessful = false;
        
        //use factory here & add return to arraylist if not null
        AudioResource newResource = AudioResourceFactory.getAudioResource(audioFilePath);
        if(newResource != null){
            newResource.displayAudioDetails();
            this.audioResourceMap.put(audioFilePath, newResource);
            
            this.selectedSongKey = audioFilePath;
            this.mpgObj.mdp.addSongNameToList(audioFilePath, newResource.fileName);
            
            
            isSuccessful = true;
        } else{
            this.mpgObj.displayMessage("Could not load: " + audioFilePath);
        }
        
        return isSuccessful;
    }
    
    public void playAudio(){
        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            //start audio playback of selected resource
            AudioResource temp = this.audioResourceMap.get(this.selectedSongKey);

            if(temp.fileTypeExtension.equalsIgnoreCase(".wav") || temp.fileTypeExtension.equalsIgnoreCase(".wave") ){
                WavAudioResource wavTemp = (WavAudioResource)temp;
                
                wavTemp.setVolume(this.mpgObj.mcp.getVolumeSliderPosition());
                
                
                Clip clip = wavTemp.getClipObj();

                LineListener listener = new LineListener(){
                    @Override
                    public void update(LineEvent event){

                        if(event.getType() == LineEvent.Type.STOP && clip.getMicrosecondPosition() >= clip.getMicrosecondLength() ){
                            mpgObj.playButton.setEnabled(true);
                            mpgObj.pauseButton.setEnabled(false);
                            mpgObj.stopButton.setEnabled(false);

                            clip.setMicrosecondPosition(0); //set to beginning of audio data
                            mpgObj.mpp.setTimeSliderBounds((int)getAudioPlayTime());
                            System.out.println("Audio finished playing.");
                        }


                    }
                };
                this.attachLineListener(listener);

                TimerTask intervalTask = new ProgressUpdater(clip,mpgObj.mpp.timeSlider);
                Timer timer = new Timer();
                timer.schedule(intervalTask, 0, 250); 
                
                wavTemp.playAudio();
            }
        }
        
    }
    
    public void pauseAudio(){
        if(this.audioResourceMap.isEmpty() == false &&  this.selectedSongKey != null && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            //pause audio playback
            this.audioResourceMap.get(this.selectedSongKey).pauseAudio();
        }
    }
    
    public void removeAudio(){
        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            //pause audio playback
            
            this.audioResourceMap.get(this.selectedSongKey).closeResource();
            this.audioResourceMap.remove(this.selectedSongKey);
            
            this.selectedSongKey = ""; //no selection after removal
        }
    }
    
    public void resumeAudio(){
        if(this.audioResourceMap.isEmpty() == false &&  this.selectedSongKey != null && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            this.audioResourceMap.get(this.selectedSongKey).resumeAudio();
        }
        
    }
    
    public void setAudioMicrosecondPosition(long timePos){
        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            this.audioResourceMap.get(this.selectedSongKey).setMicrosecondPosition(timePos);
        }
        
    }
    
    public void setAudioVolumeLevel(float volumeLevel){
        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            this.audioResourceMap.get(this.selectedSongKey).setVolume(volumeLevel);
        }
        
    }
    
    public void stopAudio(){
        
        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            //stop audio playback
            AudioResource currentResource = this.audioResourceMap.get(this.selectedSongKey);
            if( currentResource != null && currentResource.isPlaying()){
                this.audioResourceMap.get(this.selectedSongKey).stopAudio();
                setAudioMicrosecondPosition(0);
                mpgObj.mpp.setCurrentPostionOnTimeSlider(0);
            }
        }

    }
    
}
