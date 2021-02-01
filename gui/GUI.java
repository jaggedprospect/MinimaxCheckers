package com.gui;

/**
 * GUI Class
 *
 * @author Nate Heppard
 */

import java.awt.*;
import javax.swing.*;

public final class GUI extends JFrame{
    
    private static final int WIDTH=600,HEIGHT=620;
    
    private BoardPanel board;
    private InputPanel input;
    private InfoPanel info;
                
    public GUI(BoardPanel board){
        this.board = board;   
        this.input = new InputPanel();
        this.info = new InfoPanel();
        init();        
    }
    
    public BoardPanel getBoard(){
        return board;
    }
    
    public InputPanel getInputPanel(){
        return input;
    }
    
    public InfoPanel getInfoPanel(){
        return info;
    }
    
    public void init(){
        this.setTitle("CheckerAI - GUI");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        
        this.add(info, BorderLayout.CENTER);
        this.add(board, BorderLayout.CENTER);
        this.add(input, BorderLayout.SOUTH);
        
        //info.setVisible(false);
        
        this.getContentPane();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
