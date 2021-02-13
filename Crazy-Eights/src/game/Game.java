package game;

//Taken from Professor Jean-Pierre Corriveau's Yahtzee code posted on cuLearn and adapted for this assignment


import java.io.Serializable;

import java.util.ArrayList;

public class Game implements Serializable {
    public Card tCard = new Card(0,0);

    public int playerTurnID;
    public int updatedSuit = 0;
    int score = 0;
    Player[] players;

    public int score8s(Card c){
        if(c.getValue() == 8){
            score = score -50;
            return score;
        }
        return score;
    }

    public int scoreHighCard(Card c){
        if(c.toString().contains("K") || c.toString().contains("Q") || c.toString().contains("J")){
            score = score - 10;
            return score;
        }
        return score;
    }

    public int scoreCard(Card c){
        if(c.getValue() >= 1 && c.getValue() <= 10){
            score = score - c.getValue();
            return score;
        }
        return score;
    }

    public int scorePlayedCards(ArrayList<Card> played){
        return 0;
    }

    public void setPlayerTurn(int id){
        this.playerTurnID = id;
    }

    public boolean checkCard(Card c){

        System.out.println(tCard.toString());
        //Check if the card

        if(c.getSuit() == tCard.getSuit() || c.getValue() == tCard.getValue()){
            return true;
        }
        if(tCard.getValue() == 8){
            //suit change
            return true;
        }else{
            return false;
        }
    }
    public void setPickUpCard(Card c){
        this.tCard = c;
    }



    public boolean isSpecialCard(Card c){
        if(c.toString().contains("K")||c.toString().contains("Q")||c.toString().contains("J") || c.toString().contains("8")){
            return true;
        }else{
            return false;
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
