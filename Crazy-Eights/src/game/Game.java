package game;

//Taken from Professor Jean-Pierre Corriveau's Yahtzee code posted on cuLearn and adapted for this assignment


import java.io.Serializable;

import java.util.ArrayList;

public class Game implements Serializable {
    public Card tCard = new Card(0,0);

    public static int roundID =0;
    public int updatedSuit = 0;

    Player[] players;

    public int scoreCard(Card c){
        int score = 0;
        if((c.getValue() >= 0 && c.getValue() <= 9) &&  c.getValue() != 7 ){
            score = score + c.getValue() + 1;
            return score;
        }
        if(c.getValue() == 7){
            score += 50;
        }
        if(c.getValue() >9){
            score += 10;
        }
        return score;
    }



    public void setPlayerTurn(int id){
        roundID = id;
    }

    public boolean checkCard(Card c){
        //Check if the card
        if(c.getSuit() == tCard.getSuit() || c.getValue() == tCard.getValue()){
            return true;
        }
        if(c.getValue() == 7){
            //suit change
            return true;
        }
       return false;
    }
    public void setPickUpCard(Card c){
        this.tCard = c;
    }

    public int getNextID(){
        if(tCard.getValue() == 0 ) {
            roundID = roundID - 1;
        }

        return roundID;
    }

    public Card changeSuit(int s){
        return new Card(s,7);
    }



    public boolean isSpecialCard(Card c){
        if(c.toString().contains("K")||c.toString().contains("Q")||c.toString().contains("J") || c.toString().contains("8")){
            return true;
        }else{
            return false;
        }
    }


    public int getWinner(int[] scores){
        int winnerScore = scores[0];
        int index = 0;
        for(int i=0;i<scores.length;i++){
            if(scores[i]< winnerScore ){
                winnerScore = scores[i];
                index = i;
            }
        }
        return index;
    }

    public ArrayList<Card> rigHands3(int playerID){
        ArrayList<Card> moddedHand = new ArrayList<>();

        if(playerID == 1){
            moddedHand.add(new Card(2,3));
            moddedHand.add(new Card(3,6));
            moddedHand.add(new Card(1,4));
            moddedHand.add(new Card(1,5));
            moddedHand.add(new Card(1,8));
        }
        if(playerID == 2){
            moddedHand.add(new Card(3,3));
            moddedHand.add(new Card(3,5));
            moddedHand.add(new Card(0,12));
            moddedHand.add(new Card(2,7));
            moddedHand.add(new Card(1,9));
        }
        if(playerID==3){
            moddedHand.add(new Card(3,8));
            moddedHand.add(new Card(0,5));
            moddedHand.add(new Card(0,8));
            moddedHand.add(new Card(1,10));
            moddedHand.add(new Card(2,2));
        }
        if(playerID == 4){
            moddedHand.add(new Card(1,6));
            moddedHand.add(new Card(2,10));
            moddedHand.add(new Card(2,11));
            moddedHand.add(new Card(2,12));
            moddedHand.add(new Card(0,4));
        }
        return moddedHand;

    }



    public boolean isGameFinished(int[] scores){
        for(int i=0;i<scores.length;i++){
            if(scores[i] >= 100){
                return true;
            }
        }
        return false;
    }

}
