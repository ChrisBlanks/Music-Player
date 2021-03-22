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
public abstract class AudioResource {
    String audioResourceFilePath;
    String fileName;
    
    //Media Details
    String author;
    String title;
    String album;
    String genre;
    String dateReleased;
    String fileTypeExtension;
    int trackNumber;
    
    //Audio details
    int chanNum;
    int frameSize;
    int sampleSize;
    float sampleRate; 
    float frameRate;
    long numFrames;
    long playTime;
    String encodingType;
    
    //Basic API for all inheriting classes
    public abstract void closeResource();
    public abstract void displayAudioDetails();
    public abstract boolean isIniatialized();
    public abstract boolean isPlaying();
    public abstract void loadNewResource(String filePath);
    public abstract void pauseAudio();
    public abstract void playAudio();
    public abstract void resumeAudio();
    public abstract void setMicrosecondPosition(long timePos);
    public abstract void setVolume(float newVolumeLevel);
    public abstract void stopAudio();
    
}
