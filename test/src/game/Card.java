package game;

import java.util.ArrayList;
import java.util.Collections;

public class Card {
    public static char[] suits = {'C', 'D', 'H', 'S'};
    public static String[] values = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    public int suit, value;

    public Card(){
        this.suit = 0;
        this.value =0;
    }

    public Card(int s, int v){
        this.suit = s;
        this.value = v;
    }

    public static ArrayList<Card> rigDeck(){
        int size = 52;

        ArrayList<Card> riggedDeck = new ArrayList<>();
       // ArrayList<Card> d2 = new ArrayList<>();

        /*
        This part rigs the deck so that the pickup pile is exactly the same as the test cases for Part 1 Testing
         */

        //6C
        riggedDeck.add(new Card(0, 5));
        //6D
        riggedDeck.add(new Card(1,5));
        //5C
        riggedDeck.add(new Card(0,4));
        //6D
        riggedDeck.add(new Card(1,5));
        //5S
        riggedDeck.add(new Card(3,4));
        //7H
        riggedDeck.add(new Card(2,6));
        //6D
        riggedDeck.add(new Card(1,5));
        //5S
        riggedDeck.add(new Card(3,4));
        //4H
        riggedDeck.add(new Card(2,3));
        //6D
        riggedDeck.add(new Card(1,5));
        //8H
        riggedDeck.add(new Card(2,7));
        //6C
        riggedDeck.add(new Card(0, 5));

        return riggedDeck;
    }

    public static ArrayList<Card> deck(){
        int size = 52;
        ArrayList<Card> deck = new ArrayList<>(size);
        for(int i=0;i<suits.length;i++){
            for( int j=0;j<values.length;j++){
                deck.add(new Card(i,j));
            }
        }
        Collections.shuffle(deck);

        return deck;


    }

    public String toString(){
        return values[value] + suits[suit];
    }

    public static void main(String[] args) {
        Card c = new Card();
        Card c1 = new Card(1,2);

        ArrayList<Card> d = deck();
        System.out.println(d.size());
        for (Card card : d) {
           System.out.println(card.toString());
        }
    }
}
