/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

/**
 *
 * @author KuRi
 */
public class AudioResourceFactory {
    
    /**
     * Returns an instance of the requested audio resource type.
     * @param pathToResource Identifier for resource to create an object of. Possible types: "WAV"
     * @return 
     */
    public static AudioResource getAudioResource(String pathToResource){
       if(pathToResource == null){
           System.out.println("Null argument provided");
           return null;
       } 
       
       AudioResource audio = null;
       
       if(pathToResource.contains(".wav") || pathToResource.contains(".wave") ){
           audio = new WavAudioResource();
           audio.loadNewResource(pathToResource);
       } else{
           System.out.println("Unsupported file type provided: " + pathToResource); 
       }
       
       return audio;
    }
    
}
