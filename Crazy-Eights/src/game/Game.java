package game;

//Taken from Professor Jean-Pierre Corriveau's Yahtzee code posted on cuLearn and adapted for this assignment


import java.io.Serializable;

import java.util.ArrayList;

public class Game implements Serializable {


    public Game(){
        ArrayList<Card> d = new ArrayList<>();
        for(int i=0;i<Card.suits.length;i++){
            for(int j=0;j<Card.values.length;j++){
                d.add(new Card(i,j));
            }

        }


    }










    public Player getWinner(Player[] players){
        Player t = players[1];
        if(players[0].getScore() >= players[1].getScore()){
            t = players[0];
        }
        if(players[2].getScore() >= t.getScore()){
            t = players[2];
        }
        if(players[3].getScore() >= t.getScore()){
            t = players[3];
        }
        return t;
    }

}
