/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import java.util.ArrayList;

/**
 *
 * @author KuRi
 */
public class MusicPlayerController {
    
    private ArrayList<AudioResource> resources;
    
    MusicPlayerController(){
        this.resources = new ArrayList<AudioResource>();
        
    }
    
    public void closeAudio(){
        //close/discard of audio resource
    }
    
    public void loadAudio(String audioFilePath){
        //use factory here & add return to arraylist if not null
    
    }
    
    public void playAudio(){
        //start audio playback of selected resource
    }
    
    public void pauseAudio(){
        //pause audio playback
    
    }
    
    public void stopAudio(){
        //stop audio playback
    }
    
}
