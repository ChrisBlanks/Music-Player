/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 *
 * @author KuRi
 */
public class MediaPlaybackPanel extends JPanel {

    final static Color DEFAULT_PANE_BG_COLOR = new Color(0,0,0);
    final static Color DEFAULT_COMPONENT_BG_COLOR = new Color(255,255,255);
    
    final static String DEFAULT_ART_IMAGE_RESOURCE = "images/default_art.jpg";
    
    public JLabel songArtLabel;
    public JSlider timeSlider;
    
    
    MediaPlaybackPanel(){
        createGUIComponents(null);
    }
    
   
    public static void main(String[] args){
        
        java.awt.EventQueue.invokeLater(new Runnable(){

            @Override
            public void run(){
                JFrame frame = new JFrame();
                frame.setSize(400, 400);
                MediaPlaybackPanel test = new MediaPlaybackPanel(); 
                test.setTimeSliderBounds(5000000);
                frame.add(test);
                frame.setVisible(true);
            }
        });

    }
    
    private void createGUIComponents(Color newColor){
        Color bgColor = MediaControlPanel.DEFAULT_COMPONENT_BG_COLOR;
        if(newColor != null){
            bgColor = newColor;
        }
        
        this.songArtLabel = new JLabel();
        this.songArtLabel.setIcon(new ImageIcon(MediaDisplayPanel.DEFAULT_ART_IMAGE_RESOURCE));
        
        this.timeSlider = new JSlider();
        
        this.add(this.songArtLabel,BorderLayout.NORTH);
        this.add(this.timeSlider,BorderLayout.SOUTH);
        
    }
    
    /**
     * Sets the playback slider bounds from 0 to the maxPlayTime
     * @param maxPlayTime How much time the media will play for. Unit: microseconds
     */
    public void setTimeSliderBounds(int maxPlayTime){
        this.timeSlider.setMinimum(0);
        this.timeSlider.setMaximum(maxPlayTime);
        
        this.timeSlider.setValue(0);
        
    }
    
    /**
     * Set slider position to timePos value
     * @param timePos Position in microseconds
     */
    public void setCurrentPostionOnTimeSlider(int timePos){
        this.timeSlider.setValue(0);
    }
    
}
