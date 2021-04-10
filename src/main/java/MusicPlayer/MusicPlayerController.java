/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

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
    public static int SLIDER_UPDATE_INTERVAL_MILLISECONDS = 50;
    public static int MILLISECOND_TO_MICROSECOND = 1000;
    public static int TIME_SLIDER_THRESHOLD_MILLISECOND = 1050; //Note: Seems to be a 1 second delay to start clip
    
    private final Map<String,AudioResource> audioResourceMap;
    private MusicPlayerGUI mpgObj;
    
    private Timer timer;
    
    public String selectedSongKey = MusicPlayerController.NO_SONG_SELECTION;
    
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
    
    public void cancelTimerThread(){
        if(this.timer != null){
            this.timer.cancel();
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
    
    
    public String buildAudioDetailsMessage(){

        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null &&  this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            String message;
            MediaDetails details = this.audioResourceMap.get(this.selectedSongKey).getMediaDetails();
            message = String.format("<html>Song: %s<br/>Artist: %s<br/>Album: %s</html>",
                                    details.getFileName().replace(details.getFileTypeExtension(),""),
                                    details.getAuthor(),
                                    details.getAlbum()
            );
            
            return message;
        } else{
            return null;
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
    
    public long getAudioMicrosecondPosition(){
        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            return this.audioResourceMap.get(this.selectedSongKey).getMicrosecondPosition();
        } else{
            return 0;
        }   
    }
    
    public boolean getPlayingState(){
        if(this.audioResourceMap.isEmpty() == false && this.selectedSongKey != null && this.selectedSongKey.equals(MusicPlayerController.NO_SONG_SELECTION) == false){
            return this.audioResourceMap.get(this.selectedSongKey).isPlaying();
        } else{
            return false;
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
            
            int playTime = (int)getAudioPlayTime();
            this.mpgObj.mpp.setMaxPlayTimeLabel(playTime);
            
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
                            
                            setAudioMicrosecondPosition(0);
                            mpgObj.mpp.setTimeSliderBounds((int)getAudioPlayTime());
                            mpgObj.mpp.setCurrentTimeLabel(0);
                            
                            System.out.println("Audio finished playing.");
                        }


                    }
                };
                this.attachLineListener(listener);
                
                
                this.timer = new Timer();
                TimerTask intervalTask = new ProgressUpdater(this,mpgObj.mpp);
                timer.schedule(intervalTask, 0, MusicPlayerController.SLIDER_UPDATE_INTERVAL_MILLISECONDS);
                
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
    
    /**
     * Set time position in audio playback.
     * @param timePos Time position in microseconds.
     */
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
            if( currentResource != null){
                this.audioResourceMap.get(this.selectedSongKey).stopAudio();
                
                this.setAudioMicrosecondPosition(0);
                mpgObj.mpp.setCurrentPostionOnTimeSlider(0);
                mpgObj.mpp.setCurrentTimeLabel(0);
                
                this.cancelTimerThread();
            }
        }

    }
    
    public void updateTimeSlider(long timePos){
        long previousTime = this.mpgObj.mpp.getPreviousTime();
        String logMsg = String.format("Old time: %f ms New time: %f ms\nDelta: %f ms"
                                    ,(float)previousTime / 1000
                                    ,(float)timePos / 1000
                                    ,( (float)timePos - (float)previousTime) /1000
        );
        System.out.println(logMsg);
        
        long timeDiff = Math.abs((timePos - previousTime)/ MusicPlayerController.MILLISECOND_TO_MICROSECOND);
        
        if(timeDiff > MusicPlayerController.TIME_SLIDER_THRESHOLD_MILLISECOND){
            this.setAudioMicrosecondPosition(timePos);
            this.mpgObj.mpp.setCurrentPostionOnTimeSlider((int)timePos);
            this.mpgObj.mpp.setCurrentTimeLabel((int)timePos);
            System.out.println("User manually changed time position");
        }
        
    }
    
}
