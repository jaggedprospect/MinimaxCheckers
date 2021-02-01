package com.main;

import java.util.List;
import java.util.Random;

public class RandomAI extends Player implements AI{

    public RandomAI(String name,Side s){
        super(name,s);
    }

    public RandomAI(Side s){
        super("RandomAI",s);
    }

    @Override
    public Board.Decision makeMove(Board board){
        Random rand=new Random();
        List<Move> moves=board.getAllValidMoves(getSide());
        
        if(moves.isEmpty()) return Board.Decision.GAME_ENDED;
        
        Move m=moves.get(rand.nextInt(moves.size()));

        return board.makeMove(m,getSide());
    }
}
