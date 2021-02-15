package game;

//Taken from Professor Jean-Pierre Corriveau's Yahtzee code posted on cuLearn and adapted for this assignment


import java.io.Serializable;

import java.util.ArrayList;

public class Game implements Serializable {
    public Card tCard = new Card(0,0);

    public static int roundID =0;
    public int updatedSuit = 0;

    Player[] players;

    public int scoreHand(ArrayList<Card> hand){
        int score = 0;

        for(int i=0;i<hand.size();i++){
            if((hand.get(i).getValue() >= 0 && hand.get(i).getValue() <= 9) &&  hand.get(i).getValue() != 7 ){
                score = score + hand.get(i).getValue() + 1;
            }
            if(hand.get(i).getValue() == 7){
                score += 50;
            }
            if(hand.get(i).getValue() >9){
                score += 10;
            }
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

    public boolean hasTwoPlayable(ArrayList<Card> hand){
        int numPlayable = 0;
        for(int i=0;i<hand.size();i++){
            if(checkCard(hand.get(i))){
                numPlayable++;
            }
        }
        if(numPlayable >= 2){
            return true;
        }
        return false;
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

    public ArrayList<Card> rigHandPart1(int index){
        ArrayList<Card> moddedHand = new ArrayList<>();
        if(index == 1) {
            moddedHand.add(new Card(0, 2));
            moddedHand.add(new Card(2, 0));
            moddedHand.add(new Card(0, 11));
            moddedHand.add(new Card(2, 0));
            moddedHand.add(new Card(0,0));
        }
        if(index == 2){
            moddedHand.add(new Card(0,0));
            moddedHand.add(new Card(0,0));
            moddedHand.add(new Card(0,0));
            moddedHand.add(new Card(0,0));
            moddedHand.add(new Card(0,0));
        }
        if(index == 3){
            moddedHand.add(new Card(2,6));
            moddedHand.add(new Card(0,0));
            moddedHand.add(new Card(0,0));
            moddedHand.add(new Card(0,0));
            moddedHand.add(new Card(0,0));
        }
        if(index == 4){
            moddedHand.add(new Card(2,6));
            moddedHand.add(new Card(0,0));
            moddedHand.add(new Card(0,0));
            moddedHand.add(new Card(0,0));
            moddedHand.add(new Card(0,0));
        }
        return moddedHand;
    }


    public ArrayList<Card> rigDeckPart1(){
        ArrayList<Card> moddedDeck = new ArrayList<>();

        moddedDeck.add(new Card(0,12));
        moddedDeck.add(new Card(2,12));
        moddedDeck.add(new Card(0,6));
        moddedDeck.add(new Card(2,7));
        moddedDeck.add(new Card(3,4));
        moddedDeck.add(new Card(0,0));
        moddedDeck.add(new Card(0,0));
        moddedDeck.add(new Card(0,0));
        moddedDeck.add(new Card(0,0));
        moddedDeck.add(new Card(0,0));
        moddedDeck.add(new Card(0,0));
        moddedDeck.add(new Card(0,0));
        moddedDeck.add(new Card(0,0));
        moddedDeck.add(new Card(0,0));
        return moddedDeck;
    }
    public ArrayList<Card> rigHandPart2(int index) {
        ArrayList<Card> moddedHand = new ArrayList<>();
        tCard = new Card(3,5);
        if(index==1){
            moddedHand.add(new Card(3,0));
            moddedHand.add(new Card(3,2));
        }
        if(index ==2){
            moddedHand.add(new Card(3,1));
        }
        if(index == 3){
            moddedHand.add(new Card(2,7));
            moddedHand.add(new Card(2,10));
            moddedHand.add(new Card(2,5));
            moddedHand.add(new Card(2,12));
            moddedHand.add(new Card(3,12));
            moddedHand.add(new Card(3, 6));
        }
        if(index == 4){
            moddedHand.add(new Card(0,7));
            moddedHand.add(new Card(1,7));
            moddedHand.add(new Card(1,1));
            moddedHand.add(new Card(3,4));
        }
        return moddedHand;
    }

    public ArrayList<Card> rigHandsPart32(int index){
        ArrayList<Card> moddedHand = new ArrayList<>();

        if(index == 1){
            moddedHand.add(new Card(1,6));
            moddedHand.add(new Card(3,3));
            moddedHand.add(new Card(0,6));
            moddedHand.add(new Card(2,3));
            moddedHand.add(new Card(1,4));
        }
        if(index == 2){
            moddedHand.add(new Card(1,8));
            moddedHand.add(new Card(3,2));
            moddedHand.add(new Card(0,8));
            moddedHand.add(new Card(2,2));
            moddedHand.add(new Card(0,10));
        }
        if(index==3){
            moddedHand.add(new Card(1,2));
            moddedHand.add(new Card(3,8));
            moddedHand.add(new Card(0,2));
            moddedHand.add(new Card(2,8));
            moddedHand.add(new Card(2,4));
        }
        if(index == 4){
            moddedHand.add(new Card(1,3));
            moddedHand.add(new Card(3,6));
            moddedHand.add(new Card(0,3));
            moddedHand.add(new Card(3,4));
            moddedHand.add(new Card(1,7));
        }
        return moddedHand;

    }

    public ArrayList<Card> rigDeckPart3(){
        ArrayList<Card> moddedDeck = new ArrayList<>();

        moddedDeck.add(new Card(0,1));
        moddedDeck.add(new Card(0,2));
        moddedDeck.add(new Card(0,2));
        moddedDeck.add(new Card(0,9));
        moddedDeck.add(new Card(0,10));
        moddedDeck.add(new Card(0,6));
        moddedDeck.add(new Card(0,0));
        moddedDeck.add(new Card(0,0));
        moddedDeck.add(new Card(0,0));



        return moddedDeck;
    }

    public ArrayList<Card> rigDeckPart32(){
        ArrayList<Card> moddedDeck = new ArrayList<>();

        moddedDeck.add(new Card(3,12));
        moddedDeck.add(new Card(3,11));
        moddedDeck.add(new Card(2,12));
        moddedDeck.add(new Card(1,5));
        moddedDeck.add(new Card(1,11));
        moddedDeck.add(new Card(1,10));
        moddedDeck.add(new Card(1,9));



        return moddedDeck;
    }


    public ArrayList<Card> rigDeckPart2(){
        ArrayList<Card> moddedDeck = new ArrayList<>();

        moddedDeck.add(new Card(0,5));
        moddedDeck.add(new Card(1,8));
        moddedDeck.add(new Card(2,8));
        moddedDeck.add(new Card(0,5));
        moddedDeck.add(new Card(3,6));
        moddedDeck.add(new Card(2,4));
        moddedDeck.add(new Card(2,1));
        moddedDeck.add(new Card(3,4));
        moddedDeck.add(new Card(1,5));
        moddedDeck.add(new Card(2,5));
        moddedDeck.add(new Card(0,6));


        return moddedDeck;
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
