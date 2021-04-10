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
import javax.swing.event.ChangeListener;

/**
 *
 * @author KuRi
 */
public class MediaPlaybackPanel extends JPanel {

    final static Color DEFAULT_PANE_BG_COLOR = new Color(0,0,0);
    final static Color DEFAULT_COMPONENT_BG_COLOR = new Color(255,255,255);
    
    final static int SEC_TO_MICROSEC = 1000000;
    
    final static String DEFAULT_ART_IMAGE_RESOURCE = "images/default_art.jpg";
    final static String DEFAULT_TIME_TEXT = "00:00";
    
    private JPanel sliderPanel;
    private JLabel timeIndicatorLabel;
    private JLabel maxTimeLabel;
    private JSlider timeSlider;
    
    private long previousTimePos = 0;
    
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
                
                test.setCurrentTimeLabel(3  * MediaPlaybackPanel.SEC_TO_MICROSEC);
                test.setMaxPlayTimeLabel(5 * MediaPlaybackPanel.SEC_TO_MICROSEC);
                test.setTimeSliderBounds(5 * MediaPlaybackPanel.SEC_TO_MICROSEC);
                
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
        
        this.sliderPanel = new JPanel();
        this.timeIndicatorLabel = new JLabel(MediaPlaybackPanel.DEFAULT_TIME_TEXT);
        this.maxTimeLabel = new JLabel(MediaPlaybackPanel.DEFAULT_TIME_TEXT);
        
        this.timeSlider = new JSlider(0,100,0);
        this.setCurrentPostionOnTimeSlider(0);
        
        BorderLayout layout = new BorderLayout();
        layout.setHgap(10);
        
        this.sliderPanel.setLayout(layout);
        this.sliderPanel.add(this.timeIndicatorLabel,BorderLayout.WEST);
        this.sliderPanel.add(this.timeSlider,BorderLayout.CENTER);
        this.sliderPanel.add(this.maxTimeLabel,BorderLayout.EAST);
        
        this.setLayout(new BorderLayout() );
        this.add(this.sliderPanel,BorderLayout.SOUTH);
        
    }
    
    
    /**
     * Add a change listener to the time slider
     * @param listener Listener that specifies an action to perform when a change
     * is detected with the internal JSlider component.
     */
    public void addSliderChangeListener(ChangeListener listener){
        this.timeSlider.addChangeListener(listener);
    }
    
    public long getPreviousTime(){
        return this.previousTimePos;
    }
    
    
    /**
     * Set slider position to timePos value
     * @param timePos Position in microseconds
     */
    public void setCurrentPostionOnTimeSlider(int timePos){
        this.previousTimePos = this.timeSlider.getValue();
        this.timeSlider.setValue(timePos);
    }
    
    /**
     * Set the current time text label to currentTime in mm:ss format.
     * @param currentTime Time in microseconds
     */
    public void setCurrentTimeLabel(int currentTime){
        
        int playTimeSeconds = (currentTime/ MediaPlaybackPanel.SEC_TO_MICROSEC) %60 ;
        int playTimeMinutes = ((currentTime/ MediaPlaybackPanel.SEC_TO_MICROSEC) /60) % 60;
        String playTimeStr = String.format("%02d:%02d",playTimeMinutes,playTimeSeconds);
        
        this.timeIndicatorLabel.setText(playTimeStr);
    }
    
    /**
     * Sets the text for the max play time label to maxPlayTime.
     * @param maxPlayTime 
     */
    public void setMaxPlayTimeLabel(int maxPlayTime){
        int playTimeSeconds = (maxPlayTime/ MediaPlaybackPanel.SEC_TO_MICROSEC) %60 ;
        int playTimeMinutes = ((maxPlayTime/ MediaPlaybackPanel.SEC_TO_MICROSEC) /60) % 60;
        String playTimeStr = String.format("%02d:%02d",playTimeMinutes,playTimeSeconds);
        
        this.maxTimeLabel.setText(playTimeStr);
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
    
    
}
