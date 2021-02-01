package com.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Board{

    private Type[][] board;
    public final int SIZE=8;

    private int numWhiteNormalPieces;
    private int numBlackNormalPieces;
    private int numBlackKingPieces;
    private int numWhiteKingPieces;

    public enum Type{
        EMPTY,RED,GRAY,RED_KING,GRAY_KING
    }

    public enum Decision{
        COMPLETED,
        FAILED_MOVING_INVALID_PIECE,
        FAILED_INVALID_DESTINATION,
        ADDITIONAL_MOVE,
        GAME_ENDED,
        SURRENDERED
    }

    public Board(){
        setUpBoard();
    }

    public Board(Type[][] board){
        numWhiteNormalPieces=0;
        numBlackNormalPieces=0;
        numBlackKingPieces=0;
        numWhiteKingPieces=0;

        this.board=board;
        
        for(int i=0;i<SIZE;i++){
            for(int j=0;j<SIZE;j++){
                Type piece=getPiece(i,j);
                
                if(piece==Type.GRAY)
                    numBlackNormalPieces++;
                else if(piece==Type.GRAY_KING)
                    numBlackKingPieces++;
                else if(piece==Type.RED)
                    numWhiteNormalPieces++;
                else if(piece==Type.RED_KING)
                    numWhiteKingPieces++;
            }
        }
    }

    private void setUpBoard(){
        numWhiteNormalPieces=12;
        numBlackNormalPieces=12;
        numBlackKingPieces=0;
        numWhiteKingPieces=0;
        board=new Type[SIZE][SIZE];
        
        for(int i=0;i<board.length;i++){
            int start=0;
            
            if(i%2==0)
                start=1;

            Type pieceType=Type.EMPTY;
            
            if(i<=2)
                pieceType=Type.RED;
            else if(i>=5)
                pieceType=Type.GRAY;

            for(int j=start;j<board[i].length;j+=2)
                board[i][j]=pieceType;
        }

        populateEmptyOnBoard();
    }

    private void populateEmptyOnBoard(){
        for(Type[] board1:board){
            for(int j=0;j<board1.length;j++){
                if(board1[j]==null) board1[j]=Type.EMPTY;
            }
        }
    }

    public Type getPiece(int row,int col){
        return board[row][col];
    }

    public Type getPiece(Point point){
        return board[point.x][point.y];
    }

    public Type[][] getBoard(){
        return board;
    }

    public int getNumWhitePieces(){
        return numWhiteKingPieces+numWhiteNormalPieces;
    }

    public int getNumBlackPieces(){
        return numBlackKingPieces+numBlackNormalPieces;
    }

    public int getNumWhiteKingPieces(){
        return numWhiteKingPieces;
    }

    public int getNumBlackKingPieces(){
        return numBlackKingPieces;
    }

    public int getNumWhiteNormalPieces(){
        return numWhiteNormalPieces;
    }

    public int getNumBlackNormalPieces(){
        return numBlackNormalPieces;
    }

    // returns true if move successful
    public Decision makeMove(Move move,Player.Side side){
        
        if(move==null) return Decision.GAME_ENDED;

        Point start=move.getStart();
        int startRow=start.x;
        int startCol=start.y;
        
        Point end=move.getEnd();
        int endRow=end.x;
        int endCol=end.y;

        // can only move own piece and not empty space
        if(!isMovingOwnPiece(startRow,startCol,side)||getPiece(startRow,startCol)==Type.EMPTY)
            return Decision.FAILED_MOVING_INVALID_PIECE;

        List<Move> possibleMoves=getValidMoves(startRow,startCol,side);
        // System.out.println(possibleMoves);

        Type currType=getPiece(startRow,startCol);

        if(possibleMoves.contains(move)){
            boolean jumpMove=false;
            
            // if it contains move then it is either 1 move or 1 jump
            if(startRow+1==endRow||startRow-1==endRow){
                board[startRow][startCol]=Type.EMPTY;
                board[endRow][endCol]=currType;
            }else{
                jumpMove=true;
                board[startRow][startCol]=Type.EMPTY;
                board[endRow][endCol]=currType;
                Point mid=findMidSquare(move);

                Type middle=getPiece(mid);
                
                if(middle==Type.GRAY)
                    numBlackNormalPieces--;
                else if(middle==Type.GRAY_KING)
                    numBlackKingPieces--;
                else if(middle==Type.RED)
                    numWhiteNormalPieces--;
                else if(middle==Type.RED_KING)
                    numWhiteKingPieces--;
                
                board[mid.x][mid.y]=Type.EMPTY;
            }

            if(endRow==0&&side==Player.Side.GRAY){
                board[endRow][endCol]=Type.GRAY_KING;
                numBlackNormalPieces--;
                numBlackKingPieces++;
            }else if(endRow==SIZE-1&&side==Player.Side.RED){
                board[endRow][endCol]=Type.RED_KING;
                numWhiteNormalPieces--;
                numWhiteKingPieces++;
            }
            
            if(jumpMove){
                List<Move> additional=getValidSkipMoves(endRow,endCol,side);
                
                if(additional.isEmpty()) return Decision.COMPLETED;

                return Decision.ADDITIONAL_MOVE;
            }
            
            return Decision.COMPLETED;
        }else return Decision.FAILED_INVALID_DESTINATION;
    }

    public List<Move> getAllValidMoves(Player.Side side){

        Type normal=side==Player.Side.GRAY?Type.GRAY:Type.RED;
        Type king=side==Player.Side.GRAY?Type.GRAY_KING:Type.RED_KING;

        List<Move> possibleMoves=new ArrayList<>();
        
        for(int i=0;i<SIZE;i++){
            for(int j=0;j<SIZE;j++){
                Type t=getPiece(i,j);
                
                if(t==normal||t==king)
                    possibleMoves.addAll(getValidMoves(i,j,side));
            }
        }

        return possibleMoves;
    }

    // requires there to actually be a mid square
    private Point findMidSquare(Move move){
        Point ret=new Point((move.getStart().x+move.getEnd().x)/2,
                (move.getStart().y+move.getEnd().y)/2);

        return ret;
    }

    private boolean isMovingOwnPiece(int row,int col,Player.Side side){
        Type pieceType=getPiece(row,col);
        
        if(side==Player.Side.GRAY&&pieceType!=Type.GRAY&&pieceType!=Type.GRAY_KING)
            return false;
        else if(side==Player.Side.RED&&pieceType!=Type.RED&&pieceType!=Type.RED_KING)
            return false;
        
        return true;
    }

    public List<Move> getValidMoves(int row,int col,Player.Side side){
        Type type=board[row][col];
        Point startPoint=new Point(row,col);
        
        if(type==Type.EMPTY)
            throw new IllegalArgumentException();

        List<Move> moves=new ArrayList<>();

        //4 possible moves, 2 if not king
        if(type==Type.RED||type==Type.GRAY){
            //2 possible moves
            int rowChange=type==Type.RED?1:-1;

            int newRow=row+rowChange;
            
            if(newRow>=0||newRow<SIZE){
                int newCol=col+1;
                
                if(newCol<SIZE&&getPiece(newRow,newCol)==Type.EMPTY)
                    moves.add(new Move(startPoint,new Point(newRow,newCol)));
                
                newCol=col-1;
                
                if(newCol>=0&&getPiece(newRow,newCol)==Type.EMPTY)
                    moves.add(new Move(startPoint,new Point(newRow,newCol)));
            }

        } // must be king
        else{
            // 4 possible moves
            int newRow=row+1;
            
            if(newRow<SIZE){
                int newCol=col+1;
                
                if(newCol<SIZE&&getPiece(newRow,newCol)==Type.EMPTY)
                    moves.add(new Move(startPoint,new Point(newRow,newCol)));
                
                newCol=col-1;
                
                if(newCol>=0&&getPiece(newRow,newCol)==Type.EMPTY)
                    moves.add(new Move(startPoint,new Point(newRow,newCol)));
            }
            
            newRow=row-1;
            
            if(newRow>=0){
                int newCol=col+1;
                
                if(newCol<SIZE&&getPiece(newRow,newCol)==Type.EMPTY)
                    moves.add(new Move(startPoint,new Point(newRow,newCol)));
                
                newCol=col-1;
                
                if(newCol>=0&&getPiece(newRow,newCol)==Type.EMPTY)
                    moves.add(new Move(startPoint,new Point(newRow,newCol)));
            }
        }

        moves.addAll(getValidSkipMoves(row,col,side));
        
        return moves;
    }

    public List<Move> getValidSkipMoves(int row,int col,Player.Side side){
        List<Move> move=new ArrayList<>();
        Point start=new Point(row,col);

        List<Point> possibilities=new ArrayList<>();

        if(side==Player.Side.RED&&getPiece(row,col)==Type.RED){
            possibilities.add(new Point(row+2,col+2));
            possibilities.add(new Point(row+2,col-2));
        }else if(side==Player.Side.GRAY&&getPiece(row,col)==Type.GRAY){
            possibilities.add(new Point(row-2,col+2));
            possibilities.add(new Point(row-2,col-2));
        }else if(getPiece(row,col)==Type.GRAY_KING||getPiece(row,col)==Type.RED_KING){
            possibilities.add(new Point(row+2,col+2));
            possibilities.add(new Point(row+2,col-2));
            possibilities.add(new Point(row-2,col+2));
            possibilities.add(new Point(row-2,col-2));
        }

        for(Point temp:possibilities){
            Move m=new Move(start,temp);
            
            if(temp.x<SIZE&&temp.x>=0&&temp.y<SIZE&&temp.y>=0&&getPiece(temp.x,temp.y)==Type.EMPTY
                    &&isOpponentPiece(side,getPiece(findMidSquare(m)))){
                move.add(m);
            }
        }

        // System.out.println("Skip moves: " + move);
        return move;
    }

    // return true if the piece is opponents
    private boolean isOpponentPiece(Player.Side current,Type opponentPiece){
        if(current==Player.Side.GRAY&&(opponentPiece==Type.RED||opponentPiece==Type.RED_KING))
            return true;

        return current==Player.Side.RED&&(opponentPiece==Type.GRAY||opponentPiece==Type.GRAY_KING);
    }

    @Override
    public String toString(){
        StringBuilder b=new StringBuilder();
        b.append("  ");
        
        for(int i=0;i<board.length;i++)
            b.append(i).append(" ");
        
        b.append("\n");
        
        for(int i=0;i<board.length;i++){
            for(int j=-1;j<board[i].length;j++){
                String a;
                
                if(j==-1)
                    a=i+"";
                else if(board[i][j]==Type.RED)
                    a="r";
                else if(board[i][j]==Type.GRAY)
                    a="g";
                else if(board[i][j]==Type.RED_KING)
                    a="R";
                else if(board[i][j]==Type.GRAY_KING)
                    a="G";
                else
                    a="_";

                b.append(a);
                b.append(" ");
            }
            
            b.append("\n");
        }
        
        return b.toString();
    }

    public Board copy(){
        Type[][] newBoard=new Type[SIZE][SIZE];
        
        for(int i=0;i<SIZE;i++){
            System.arraycopy(board[i],0,newBoard[i],0,SIZE);
        }
        
        Board b=new Board(newBoard);
        
        return b;
    }
}