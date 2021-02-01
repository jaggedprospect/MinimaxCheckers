package com.gui;

/**
 * Board Class
 *
 * @author Nate Heppard
 */

import com.main.Board;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public final class BoardPanel extends JPanel{
    
    private static final String TABLE_IMG="/res/wood-table.png";

    private final Map<Point,Cell> cellMap;
    private Cell currentCell;

    public BoardPanel(Board.Type[][] boardArray){
        cellMap=new HashMap<>();
        currentCell = null;

        int index=0;
        int count=0;

        this.setLayout(new GridBagLayout());
        this.setBackground(Color.BLACK);
        GridBagConstraints gbc=new GridBagConstraints();

        Point point;

        for(int row=0;row<8;row++){
            for(int col=0;col<8;col++){
                point=new Point(row,col);
                Color color=index%2==0?Color.WHITE:Color.BLACK;

                gbc.gridx=col;
                gbc.gridy=row;

                Cell cell=new Cell(count,point,color);
                cell.addActionListener(new ButtonListener());
                
                this.add(cell,gbc);
                cellMap.put(point,cell);

                index++;
                count++;
            } 

            index++;
        }
        
        // set Cell icons based on current board
        update(boardArray);
        
        fillEmptySpaces();
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        ImageIcon ic=new ImageIcon(getClass().getResource(TABLE_IMG));
        Image img=ic.getImage();
        g.drawImage(img,-3,-8,this); // uses arbitrary values
    }
    
    public void update(Board.Type[][] boardArray){
        for(int r=0;r<boardArray.length;r++){
            for(int c=0;c<boardArray[r].length;c++){
                Board.Type piece=boardArray[r][c];
                Cell cell=cellMap.get(new Point(r,c));
                                
                // boolean isRed for setIcon methods
                if(piece==Board.Type.GRAY)
                    cell.setRegularIcon(false);
                else if(piece==Board.Type.GRAY_KING)
                    cell.setKingIcon(false);
                else if(piece==Board.Type.RED)
                    cell.setRegularIcon(true);
                else if(piece==Board.Type.RED_KING)
                    cell.setKingIcon(true);
                else if(piece==Board.Type.EMPTY)
                    cell.resetCellIcon();
                
                cell.setType(piece);
            }
        }
    }

    public Map getCellMap(){
        return cellMap;
    }
    
    public void setCurrentCell(Cell currentCell){
        this.currentCell = currentCell;
    }
    
    public Cell getCurrentCell(){
        return currentCell;
    }
    
    private void fillEmptySpaces(){
        for(Map.Entry element : cellMap.entrySet()){
            Cell cell=(Cell)element.getValue();
            
            if(!cell.hasPiece()){
                cell.setType(Board.Type.EMPTY);
            }
        }
    }

    private class ButtonListener implements ActionListener{
        
        private Cell button;
        
        @Override
        public void actionPerformed(ActionEvent event){
            button=(Cell)event.getSource(); 

            // -- Check Statements --
            //System.out.println("Cell "+button.getCellNum()+" pressed.");
            //System.out.println("Type: "+button.getType());
            //System.out.println(button.getPoint()+", Type: "+button.getType());
            
            setCurrentCell(button);
        }
    }
}
