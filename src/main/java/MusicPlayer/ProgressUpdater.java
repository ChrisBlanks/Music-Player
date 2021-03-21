/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import java.util.TimerTask;
import javax.sound.sampled.Clip;
import javax.swing.JSlider;

/**
 *
 * @author KuRi
 */
class ProgressUpdater extends TimerTask{
    
    private JSlider slider;
    private Clip clip;
    
    ProgressUpdater(Clip clip,JSlider slider){
        this.slider = slider;
        this.clip = clip;
    }
    
    @Override
    public void run(){
        
        if(this.clip.isRunning()){
            long newPos = this.clip.getMicrosecondPosition();
            this.slider.setValue((int)newPos);
        }
        
    }
}
