package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

public class Player {
    public String name;
    int playerID = 0;

    Game game = new Game();

    private int[] scoresheet = new int[50];

    public int[] playRound(int[] dieRoll) {
        Scanner myObj = new Scanner(System.in);
        int count = 0; // reroll 3 times
        int stop = 0;

        //game.printDieRoll(dieRoll);
        while (stop == 0) {
            System.out.println("Select an action: ");
            if (count < 3) {
                System.out.println("(1) Choose dice number to roll again");
                System.out.println("(2) Roll all again");
            }
            System.out.println("(3) Score this round");

            int act = myObj.nextInt();
            if (act == 1 && count < 3) {
                System.out.println("Select the die to hold (Ones not held get rerolled): (1,2...) ");
                String[] die = (myObj.next()).replaceAll("\\s", "").split(",");

                //dieRoll = game.reRollNotHeld(dieRoll, die);
                System.out.println("New Roll: ");
                //game.printDieRoll(dieRoll);
            }

            if (act == 2 && count < 3) {
                for (int i = 0; i < dieRoll.length; i++) {
                    //dieRoll = game.rerollDice(dieRoll, i);

                }
                System.out.println("New Roll: ");
                //game.printDieRoll(dieRoll);
            }
            count++;
            if (act == 3) {
//				set yahtzee bonus if applicable
                //setScoreSheet(13, game.yahtzeeBonus(scoreSheet, dieRoll));

//				get the score for the option requested
//				check if its been stored already before adding else ask for another number
                int r = 0;
                while (r != -1) {
                    System.out.println("Where do you want to score this round? (1/2/3...)");
                    r = myObj.nextInt();
//					add the yahtzee bonus if the roll was yahtzee and yahtzee is full
                    /*
                    if (game.isScoreSheetPositionEmpty(scoreSheet, r)) {
                        setScoreSheet(scoreRound(r, dieRoll));
                        r = -1;
                    } else {
                        System.out.println("The position is filled. Try another number");
                    }
                    */

                }
                stop = 1;
            }
        }
        //return this.scoreSheet;
        return this.scoresheet;

    }






}
