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
        AudioResource temp = resources.get(currentResourceIndex);
        
        if(temp.fileTypeExtension.equalsIgnoreCase(".wav") || temp.fileTypeExtension.equalsIgnoreCase(".wave") ){
            WavAudioResource wavTemp = (WavAudioResource)temp;
            Clip clipTemp = wavTemp.getClipObj();
            clipTemp.addLineListener(listener);
        } else{
            System.out.println("Unsupported file type. Line listener was not attached.");
        }
    }
    
    public void closeAudio(){
        //close/discard of audio resource
        resources.get(currentResourceIndex).closeResource();
    }
    
    public long getAudioPlayTime(){
        return resources.get(currentResourceIndex).playTime;
    }
    
    public String getAudioFileName(){
        return resources.get(currentResourceIndex).fileName;
    }
    
    public String getAudioFilePath(){
        return resources.get(currentResourceIndex).audioResourceFilePath;
    }
    
    public void loadAudio(String audioFilePath){
        //use factory here & add return to arraylist if not null
        AudioResource newResource = AudioResourceFactory.getAudioResource(audioFilePath);
        if(newResource != null){
            newResource.displayAudioDetails();
            
            resources.add(newResource);
            ++currentResourceIndex;
        }
    }
    
    public void playAudio(){
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
                        mpgObj.timeBar.setValue(0);
                        System.out.println("Audio finished playing.");
                    }


                }
            };
            this.attachLineListener(listener);
            
            TimerTask intervalTask = new ProgressUpdater(clip,mpgObj.timeBar);
            Timer timer = new Timer();
            timer.schedule(intervalTask, 0, 250); 
        }
        
    }
    
    public void pauseAudio(){
        //pause audio playback
        resources.get(currentResourceIndex).pauseAudio();
    }
    
    public void resumeAudio(){
        resources.get(currentResourceIndex).resumeAudio();
    }
    
    public void setMicrosecondPosition(long timePos){
        resources.get(currentResourceIndex).setMicrosecondPosition(timePos);
    }
    
    public void stopAudio(){
        //stop audio playback
        resources.get(currentResourceIndex).stopAudio();
        setMicrosecondPosition(0);
        this.mpgObj.timeBar.setValue(0);
    }
    
}
