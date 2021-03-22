/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicPlayer;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

/**
 *
 * @author KuRi
 */
public class MediaControlPanel extends JPanel {
    final static Color DEFAULT_PANE_BG_COLOR = new Color(0,0,0);
    final static Color DEFAULT_COMPONENT_BG_COLOR = new Color(255,255,255);
    
    final static String PLAY_BUTTON_RESOURCE = "images/play.png";
    final static String PAUSE_BUTTON_RESOURCE = "images/pause.png";
    final static String STOP_BUTTON_RESOURCE = "images/stop.png";
    final static String BACK_SKIP_BUTTON_RESOURCE = "images/backward.png";
    final static String FORWARD_SKIP_BUTTON_RESOURCE = "images/forward.png";
    final static String VOLUME_ICON_RESOURCE = "images/volume_icon_transparent.jpg";
        
    JButton playButton;
    JButton pauseButton;
    JButton stopButton;
    JButton skipBackwardButton;
    JButton skipForwardButton;
    JLabel volumeLabel;
    JSlider volumeSlider;
    JPanel volumeContainer;
    
    MediaControlPanel(){
        createComponents(null);
    }
    
    /**
     * Creates a media control panel with the specified color for the components
     * @param componentBGColor Color object used to set background color
     */
    MediaControlPanel(Color componentBGColor){
       createComponents(componentBGColor); 
    }
    
    
    /**
     * Creates media control panel with the specified colors for the panel & components
     * @param componentBGColor
     * @param panelBGColor 
     */
    MediaControlPanel(Color componentBGColor, Color panelBGColor){
       createComponents(componentBGColor); 
       this.setBackgroundColor(panelBGColor);
    }
    
    
    public static void main(String[] args){
        
        java.awt.EventQueue.invokeLater(new Runnable(){

            @Override
            public void run(){
                JFrame frame = new JFrame();
                frame.setSize(600, 150);
                MediaControlPanel test = new MediaControlPanel(); 
                frame.add(test);
                frame.setVisible(true);
            }
        });

    }

    
    /**
     * Creates GUI components & adds them to the object instance to be displayed
     * @param newColor 
     */
    private void createComponents(Color newColor){
        Color bgColor = MediaControlPanel.DEFAULT_COMPONENT_BG_COLOR;
        if(newColor != null){
            bgColor = newColor;
        }
        
        this.playButton = new JButton();
        this.pauseButton = new JButton();
        this.stopButton = new JButton();
        this.skipBackwardButton = new JButton();
        this.skipForwardButton = new JButton();
        this.volumeContainer = new JPanel();
        this.volumeLabel = new JLabel();
        this.volumeSlider = new JSlider(0,100,50);
        
        this.playButton.setBackground(bgColor);
        this.playButton.setIcon(new ImageIcon(MediaControlPanel.PLAY_BUTTON_RESOURCE));
        
        this.pauseButton.setBackground(bgColor);
        this.pauseButton.setIcon(new ImageIcon(MediaControlPanel.PAUSE_BUTTON_RESOURCE));
        
        this.stopButton.setBackground(bgColor);
        this.stopButton.setIcon(new ImageIcon(MediaControlPanel.STOP_BUTTON_RESOURCE));
        
        this.skipBackwardButton.setBackground(bgColor);
        this.skipBackwardButton.setIcon(new ImageIcon(MediaControlPanel.BACK_SKIP_BUTTON_RESOURCE));
        
        this.skipForwardButton.setBackground(bgColor);
        this.skipForwardButton.setIcon(new ImageIcon(MediaControlPanel.FORWARD_SKIP_BUTTON_RESOURCE));
        
        this.volumeLabel.setIcon(new ImageIcon(MediaControlPanel.VOLUME_ICON_RESOURCE));
        
        this.volumeSlider.setBackground(bgColor);

        this.volumeContainer.add(this.volumeLabel);
        this.volumeContainer.add(this.volumeSlider);
        
        
        this.add(this.skipBackwardButton);
        this.add(this.stopButton);
        this.add(this.playButton);
        this.add(this.pauseButton);
        this.add(this.skipForwardButton);
        this.add(this.volumeContainer);
    }
    
    /**
     * Sets the background color of the Panel
     * @param bg Color object used to set the background color
     */
    private void setBackgroundColor(Color bg){
        this.setBackground(bg); 
    }
    
    //Public interface
    
    public void setPlayAction(ActionListener action){
        this.playButton.addActionListener(action);
    }
    
    public void setPauseAction(ActionListener action){
        this.pauseButton.addActionListener(action);
    }    
    
    public void setStopAction(ActionListener action){
        this.stopButton.addActionListener(action);
    }  
    
    public void setForwardAction(ActionListener action){
        this.skipForwardButton.addActionListener(action);
    }
    
    public void setBackwardAction(ActionListener action){
        this.skipBackwardButton.addActionListener(action);
    }
    
    public void setVolumeChangeListener(ChangeListener listener){
        this.volumeSlider.addChangeListener(listener);
    }
    
    public int getVolumeSliderPosition(){
        return this.volumeSlider.getValue();
    }
    
    
}
