package com.gui;

/**
 *
 * @author Nate Heppard
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InputPanel extends JPanel{
    
    private JLabel roundLabel, playerLabel, statusLabel;
    private JButton surrenderButton;
    private boolean surrenderPressed;
    
    public InputPanel(){        
        roundLabel = new JLabel("Round: ");
        playerLabel = new JLabel("(No turn info)");
        statusLabel = new JLabel("Game in progress.");
        surrenderButton = new JButton("Surrender");
        surrenderButton.addActionListener(new ButtonListener());
        surrenderPressed = false;
        
        this.setSize(new Dimension(200,100));
        this.setLayout(new GridLayout(1,3));
        this.add(roundLabel);
        this.add(statusLabel);
        this.add(playerLabel);
        this.add(surrenderButton);
        this.setVisible(true);
    }
    
    public void setRoundLabel(String text){
        this.roundLabel.setText(text);
    }
    
    public void setPlayerLabel(String text){
        this.playerLabel.setText(text);
    }
    
    public void setStatusLabel(String text){
        this.statusLabel.setText(text);
    }
    
    public boolean surrenderCheck(){
        return surrenderPressed;
    }
    
    private class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            surrenderPressed = true;
        }
    }
}
