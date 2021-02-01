package com.gui;

/**
 *
 * @author Nate Heppard
 */

import java.awt.Dimension;
import javax.swing.JPanel;


public class InfoPanel extends JPanel{
    
    public enum State{
        INFO
    }
    
    public InfoPanel(){
        this.setSize(new Dimension(100,100));
        //this.setLayout();
        this.setVisible(true);
    }
}
