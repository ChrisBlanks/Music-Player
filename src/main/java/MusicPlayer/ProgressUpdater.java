/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import java.util.TimerTask;
import javax.sound.sampled.Clip;
import javax.swing.JProgressBar;

/**
 *
 * @author KuRi
 */
class ProgressUpdater extends TimerTask{
    
    private JProgressBar bar;
    private Clip clip;
    
    ProgressUpdater(Clip clip,JProgressBar bar){
        this.bar = bar;
        this.clip = clip;
    }
    
    @Override
    public void run(){
        
        if(this.clip.isRunning()){
            long newPos = this.clip.getMicrosecondPosition();
            this.bar.setValue((int)newPos);
        }
        
    }
}
