package com.main;

import com.gui.Cell;
import com.gui.BoardPanel;
import com.gui.GUI;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

public class Main{

    public static final double TOTAL = 1;                   // number of matches
    public static final int ROUNDS = 200;                   // number of rounds
    public static final int FPS = 60;
    
    private static boolean multipleMatchesTest;              // tests for multiple matches
    private static Queue<Cell> moveQ;                       // queue of moves to poll
    private static String entityString1, entityString2;     // entity game identifiers
    private static int ticks;                               // frame counter
    private static boolean running;                         // game switch
    private static boolean oneTurn;                         // one goes first if true

    private static int grayWin;                             // gray win counter
    private static int redWin;                              // red win counter
    private static int draws;                               // tie game counter

    private static Board board;                             // board for logic
    private static BoardPanel boardpanel;                   // board for visuals
    private static GUI gui;                                 // board GUI

    public static void main(String[] args) throws InterruptedException{
        initEntities("player", "minimax");
        initGame();
        
        // === Define Entities ================================
        Player one = new Player("Player 1", Player.Side.GRAY);
        //Player two = new Player("Player 2", Player.Side.RED);

        //MinimaxAI one=new MinimaxAI(Player.Side.GRAY,3);
        MinimaxAI two=new MinimaxAI(Player.Side.RED,6);

        //RandomAI one = new RandomAI(Player.Side.GRAY);
        //RandomAI two = new RandomAI(Player.Side.RED);
        // ====================================================
        
        running = true;
        
        play(one, two);
    }
    
    private static void play(Player one, Player two){
        long initialTime = System.nanoTime();
        final double time = 1000000000 / FPS;
        double delta = 0;
        long timer = System.currentTimeMillis();
        
        for(int t=0;t<TOTAL;t++){            
            if(t>0)
                board=new Board();
            
            Player current=one;
            
            if(!oneTurn) 
                current=two;
            
            int rounds = 1;
            
            // game loop
            while(running){
                long currentTime = System.nanoTime();
                delta += (currentTime - initialTime) / time;
                initialTime = currentTime;
                
                gui.getInputPanel().setRoundLabel("Round: "+rounds);
                gui.getInputPanel().setPlayerLabel(current.toString()+"'s turn");
                
                if(rounds >= ROUNDS){
                    if(board.getNumWhitePieces() > board.getNumBlackPieces()){
                        gui.getInputPanel().setStatusLabel("RED Wins");
                        redWin++;
                    }else if(board.getNumBlackPieces() > board.getNumWhitePieces()){
                        gui.getInputPanel().setStatusLabel("GRAY Wins");
                        grayWin++;
                    }else{
                        gui.getInputPanel().setStatusLabel("Draw Game");
                        draws++;
                    }
                    
                    break;
                }
                
                Board.Decision decision=null;
                
                if(delta >= 1){
                    ticks++;
                    delta--;
        
                    // get player input
                    decision = getInput(current, decision);
                    
                    if(decision != null) rounds++;
                    
                    if(gui.getInputPanel().surrenderCheck()){
                        running = false;
                        decision = Board.Decision.SURRENDERED;
                        gui.getBoard().setVisible(false);
                    }
                }
                
                if(decision==Board.Decision.FAILED_INVALID_DESTINATION ||
                        decision==Board.Decision.FAILED_MOVING_INVALID_PIECE){
                    gui.getInputPanel().setStatusLabel("Move Failed");
                }else if(decision==Board.Decision.COMPLETED){
                    if(board.getNumBlackPieces()==0){
                        gui.getInputPanel().setStatusLabel("RED Wins");
                        redWin++;
                        break;
                    }
                    
                    if(board.getNumWhitePieces()==0){
                        gui.getInputPanel().setStatusLabel("GRAY Wins");
                        grayWin++;
                        break;
                    }
                    
                    if(oneTurn)
                        current=two;
                    else
                        current=one;
                    
                    oneTurn=!oneTurn;
                }else if(decision==Board.Decision.ADDITIONAL_MOVE){
                    gui.getInputPanel().setStatusLabel("Additional Move");
                }else if(decision==Board.Decision.GAME_ENDED){
                    // current player cannot move
                    if(current.getSide()==Player.Side.GRAY){
                        gui.getInputPanel().setStatusLabel("RED Wins");
                        redWin++;
                    }else{
                        gui.getInputPanel().setStatusLabel("GRAY Wins");
                        grayWin++;
                    }
                    
                    break;
                }else if(decision == Board.Decision.SURRENDERED){
                    if(current.getSide() == Player.Side.GRAY){
                        gui.getInputPanel().setStatusLabel("RED Wins");
                        redWin++;
                    }else{
                        gui.getInputPanel().setStatusLabel("GRAY Wins");
                        grayWin++;
                    }
                }
                
                if(System.currentTimeMillis() - timer > 1000){
                    ticks = 0;
                    timer += 1000;
                }
            }
            
            System.out.println("Game finished after: "+rounds+" rounds");
            
            // MinimaxAI stats
            if(entityString1.equals("minimax")) System.out.println("(GRAY) Avg time per move: "+((MinimaxAI)one).getAverageTimePerMove());
            if(entityString2.equals("minimax")) System.out.println("(RED) Avg time per move: "+((MinimaxAI)two).getAverageTimePerMove());
        }
        
        showStats();
    }
    
    private static Board.Decision getInput(Player current, Board.Decision decision){
        if(current instanceof AI){
            decision=((AI)current).makeMove(board);
            boardpanel.update(board.getBoard());
        }else{
            Move m;
            Point p1, p2;
            Cell c = null;
            
            c = boardpanel.getCurrentCell();
            
            if(c != null && c != moveQ.peek()){
                moveQ.add(c);
            }
            
            if(moveQ.size() >= 2){
                p1 = moveQ.poll().getPoint();
                p2 = moveQ.poll().getPoint();
                m = new Move((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                decision = current.makeMove(m, board);
            }

            boardpanel.update(board.getBoard());
        }
        
        return decision;
    }
    
    private static void initEntities(String x, String y){
        entityString1 = x;
        entityString2 = y;
    }
    
    private static void initGame(){
        multipleMatchesTest = TOTAL > 1;
        moveQ = new LinkedList<>();
        oneTurn = true;
        
        redWin = 0;
        grayWin = 0;
        draws = 0;
        
        board = new Board();
        boardpanel = new BoardPanel(board.getBoard());
        gui = new GUI(boardpanel);
        
        ticks = 0;
    }
    
    private static void showStats(){
        System.out.println("Gray won "+grayWin/TOTAL*100+"%"+", Red won "+redWin/TOTAL*100+"%");
        System.out.println("Games played: "+TOTAL);
        System.out.println("Number of draws: "+draws+" ("+draws/TOTAL*100+"%)");
    }

    // === Check Methods ======================
    private static void printLine(String s){
        if(!multipleMatchesTest)
            System.out.println(s);
    }

    private static void simplePrint(String s){
        if(!multipleMatchesTest)
            System.out.print(s);
    }

    private static void printLine(){
        if(!multipleMatchesTest)
            System.out.println();
    }
    // =======================================
}
