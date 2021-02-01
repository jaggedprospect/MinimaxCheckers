package com.gui;

/**
 * Cell Class
 *
 * @author Nate Heppard
 */

import com.main.Board;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Cell extends JButton{

    private static final String RED="/res/red-circle.png";
    private static final String GREY="/res/grey-circle.png";
    private static final String RED_KING="/res/red-king.png";
    private static final String GREY_KING="/res/grey-king.png";
    private static final int RESIZE_VAL=45;

    private final int cellNum;
    private final Point point;
    private final Color color;
    private Board.Type type;

    public Cell(int cellNum,Point point,Color color){
        this.cellNum=cellNum;
        this.point=point;
        this.color=color;

        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setBackground(color);
        this.setOpaque(true);
    }

    public int getCellNum(){
        return cellNum;
    }

    public Point getPoint(){
        return point;
    }
    
    public Color getColor(){
        return color;
    }
    
    public void setType(Board.Type type){
        this.type=type;
    }
    
    public Board.Type getType(){
        return type;
    }

    public void setRegularIcon(boolean isRed){
        String file;

        if(isRed) file=RED;
        else file=GREY;

        ImageIcon ic=new ImageIcon(getClass().getResource(file));
        Image img=ic.getImage();
        Image newImg=img.getScaledInstance(RESIZE_VAL,RESIZE_VAL,Image.SCALE_SMOOTH);
        ic=new ImageIcon(newImg);
        this.setIcon(ic);
    }
    
    public void setKingIcon(boolean isRed){
        String file;
        
        if(isRed) file=RED_KING;
        else file=GREY_KING;
        
        ImageIcon ic=new ImageIcon(getClass().getResource(file));
        Image img=ic.getImage();
        Image newImg=img.getScaledInstance(RESIZE_VAL,RESIZE_VAL,Image.SCALE_SMOOTH);
        ic=new ImageIcon(newImg);
        this.setIcon(ic);
    }
    
    public void resetCellIcon(){
        this.setIcon(null);
    }
    
    public boolean hasPiece(){
        return type!=Board.Type.EMPTY;
    }
    
    public boolean isSimpleDiagonal(Cell cell){
        return this.point.getX()-cell.getPoint().getX()==-1 && this.point.getY()-cell.getPoint().getY()==-1 ||
                this.point.getX()-cell.getPoint().getX()==-1 && this.point.getY()-cell.getPoint().getY()==1 ||
                this.point.getX()-cell.getPoint().getX()==1 && this.point.getY()-cell.getPoint().getY()==-1 ||
                this.point.getX()-cell.getPoint().getX()==1 && this.point.getY()-cell.getPoint().getY()==1;
    }
    
    public boolean isJumpDiagonal(Cell cell){
        return this.point.getX()-cell.getPoint().getX()==-2 && this.point.getY()-cell.getPoint().getY()==-2 ||
                this.point.getX()-cell.getPoint().getX()==-2 && this.point.getY()-cell.getPoint().getY()==2 ||
                this.point.getX()-cell.getPoint().getX()==2 && this.point.getY()-cell.getPoint().getY()==-2 ||
                this.point.getX()-cell.getPoint().getX()==2 && this.point.getY()-cell.getPoint().getY()==2;
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(50,50);
    }
    
    public String toString(){
        return "Cell "+cellNum;
    }
}
