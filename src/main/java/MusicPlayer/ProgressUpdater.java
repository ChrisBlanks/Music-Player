/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import java.util.TimerTask;
import javax.sound.sampled.Clip;

/**
 *
 * @author KuRi
 */
class ProgressUpdater extends TimerTask{
    
    private final MediaPlaybackPanel mpp;
    private final MusicPlayerController mpc;
    
    ProgressUpdater(MusicPlayerController mpc,MediaPlaybackPanel mpp){
        this.mpp = mpp;
        this.mpc = mpc;
    }
    
    @Override
    public void run(){
        
        if(this.mpc.getPlayingState() == true){
            long newPos = this.mpc.getAudioMicrosecondPosition();
            
            this.mpp.setCurrentPostionOnTimeSlider((int)newPos);
            this.mpp.setCurrentTimeLabel((int)newPos);
        } else if(this.mpc.getPausedState() == false) {
            this.mpc.stopAudio();
        }
        
    }
}
