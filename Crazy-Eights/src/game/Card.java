package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;



public class Card implements Serializable {
    public static char[] suits = {'C', 'D', 'H', 'S'};
    public static String[] values = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    public int suit, value;
    public static ArrayList<Card> GlobalDeck = new ArrayList<>();
    ArrayList<Card> hand = new ArrayList<>();

    public Card(){
        this.suit = 0;
        this.value =0;
    }

    public Card(int s, int v){
        this.suit = s;
        this.value = v;
    }

    public static ArrayList<Card> initDeck(){
        ArrayList<Card> deck = new ArrayList<>();
        int size = 52;
        for(int i=0;i<suits.length;i++){
            for( int j=0;j<values.length;j++){
                deck.add(new Card(i,j));
            }
        }
        GlobalDeck = deck;

        Collections.shuffle(deck);
        return deck;
    }

    public ArrayList<Card> init(){
        GlobalDeck = initDeck();
        return GlobalDeck;
    }



    public String toString(){
        return values[value] + suits[suit];
    }
    public int getValue(){

        return this.value;
    }

    public char getSuit(){
        return suits[suit];
    }

    public static void main(String[] args) {
        Card c = new Card();
        Card c1 = new Card(1,2);

        ArrayList<Card> d = initDeck();
        System.out.println(c1.getValue());
        System.out.println(c1.getSuit());
        for(int i=0;i<52; i++){
            System.out.println(d.get(i).toString());
        }
    }
}
