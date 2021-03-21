/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import java.util.ArrayList;
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
    
    private ArrayList<AudioResource> resources;
    private MusicPlayerGUI mpgObj;
    
    int currentResourceIndex = -1;
    
    MusicPlayerController(){
        this.resources = new ArrayList<AudioResource>();
        
    }
    
    public void attachGUIInstance(MusicPlayerGUI mpg){
        this.mpgObj = mpg;
    }
    
    public void attachLineListener(LineListener listener){
        if(resources.isEmpty() == false){
            AudioResource temp = resources.get(currentResourceIndex);

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

        if(resources.isEmpty() == false){
            //close/discard of audio resource
            resources.get(currentResourceIndex).closeResource();
        }
    }
    
    public long getAudioPlayTime(){
        
        if(resources.isEmpty() == false){
            return resources.get(currentResourceIndex).playTime;
        } else{
            return 0;
        }
    }
    
    public String getAudioFileName(){
        if(resources.isEmpty() == false){
            return resources.get(currentResourceIndex).fileName;
        } else{
            return null;
        }
        
    }
    
    public String getAudioFilePath(){
        if(resources.isEmpty() == false){
            return resources.get(currentResourceIndex).audioResourceFilePath;
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
            
            resources.add(newResource);
            ++currentResourceIndex;
            isSuccessful = true;
        } else{
            this.mpgObj.displayMessage("Could not load: " + audioFilePath);
        }
        
        return isSuccessful;
    }
    
    public void playAudio(){
        if(resources.isEmpty() == false){
            //start audio playback of selected resource
            AudioResource temp = resources.get(currentResourceIndex);
            temp.playAudio();

            if(temp.fileTypeExtension.equalsIgnoreCase(".wav") || temp.fileTypeExtension.equalsIgnoreCase(".wave") ){
                WavAudioResource wavTemp = (WavAudioResource)temp;
                Clip clip = wavTemp.getClipObj();

                LineListener listener = new LineListener(){
                    @Override
                    public void update(LineEvent event){

                        if(event.getType() == LineEvent.Type.STOP && clip.getMicrosecondPosition() >= clip.getMicrosecondLength() ){
                            mpgObj.playButton.setEnabled(true);
                            mpgObj.pauseButton.setEnabled(false);
                            mpgObj.stopButton.setEnabled(false);

                            clip.setMicrosecondPosition(0); //set to beginning of audio data
                            mpgObj.timeSlider.setValue(0);
                            System.out.println("Audio finished playing.");
                        }


                    }
                };
                this.attachLineListener(listener);

                TimerTask intervalTask = new ProgressUpdater(clip,mpgObj.timeSlider);
                Timer timer = new Timer();
                timer.schedule(intervalTask, 0, 250); 
            }
        }
        
    }
    
    public void pauseAudio(){
        if(resources.isEmpty() == false){
            //pause audio playback
            resources.get(currentResourceIndex).pauseAudio();
        }
    }
    
    public void resumeAudio(){
        if(resources.isEmpty() == false){
            resources.get(currentResourceIndex).resumeAudio();
        }
        
    }
    
    public void setMicrosecondPosition(long timePos){
        if(resources.isEmpty() == false){
            resources.get(currentResourceIndex).setMicrosecondPosition(timePos);
        }
        
    }
    
    public void setAudioVolumeLevel(float volumeLevel){
        if(resources.isEmpty() == false){
            resources.get(currentResourceIndex).setVolume(volumeLevel);
        }
        
    }
    
    public void stopAudio(){
        
        if(resources.isEmpty() == false){
            //stop audio playback
            resources.get(currentResourceIndex).stopAudio();
            setMicrosecondPosition(0);
            this.mpgObj.timeSlider.setValue(0);
        }

    }
    
}
