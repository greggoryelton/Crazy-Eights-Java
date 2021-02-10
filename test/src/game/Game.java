package game;

//Taken from Professor Jean-Pierre Corriveau's Yahtzee code posted on cuLearn and adapted for this assignment


public class Game {

    public Game(){
        //TO-DO

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
