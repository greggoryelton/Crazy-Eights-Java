package game;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Player implements Serializable {

    /*
     * score sheet is saved as a hashmap upper one, two, three, four, five, six
     * lower 3ok, 4ok, full, sst, lst, yahtzee, chance, lowerbonus, upperbonus
     */

    /**
     *
     */

    private static final long serialVersionUID = 1L;
    public String name;

    int playerId = 0;

    Game game = new Game();
    private int[] scoreSheet = new int[15];
    int roundNum =0;
    ArrayList<Card> hand = new ArrayList<>();
    ArrayList<Card> deck = new ArrayList<>();
    public int score = 0;
    public Card tCard = game.tCard;
    public String direction = "Right";


    static Client clientConnection;



    Player[] players = new Player[4];
//	private ArrayList<String> scoreSheetKey = new ArrayList<String>(Arrays.asList("one", "two", "three", "four", "five",
//			"six", "3ok", "4ok", "full", "sst", "lst", "yahtzee", "chance", "bonus"));

    /*
     * play a round of the game
     */
    public ArrayList<Card> playRound(ArrayList<Card> userHand) {

        Scanner myObj = new Scanner(System.in);
        int turn =0;
        int count = 0; // reroll 3 times
        int end = 0;

        //game.printHand(dieRoll);

        while (end == 0) {
            System.out.println("Select an action: ");
            while (count < 3) {
                System.out.println("(1) Choose a card to play from your hand (ie. 0,1,2,3...)");
                System.out.println("(2) Draw a new card");

                System.out.print("Hand: ");
                for(int i=0;i<userHand.size();i++){
                    System.out.print(userHand.get(i).toString() +" ");
                }
                System.out.println();
                int act = myObj.nextInt();
                if (act == 1 && count < 3) {
                    System.out.println("Choose a card to play from 0-" + (userHand.size() - 1));
                    int c = myObj.nextInt();
                    if (game.checkCard(userHand.get(c))) {
                        game.setPickUpCard(userHand.get(c));
                        score = game.scoreCard(userHand.get(c));
                        System.out.println(score);
                        System.out.println("Player played card: " + userHand.get(c));
                        userHand.remove(c);
                        if(game.tCard.getValue() == 7){
                            System.out.println("Change suit to: 0. C, 1. D, 2. H, 3. S");
                            int suitSelection = myObj.nextInt();
                            game.tCard.suit = suitSelection;
                        }
                        if(game.tCard.getValue() == 0){
                            if(direction.equals("Left")){
                                direction = "Right";
                            }if(direction.equals("Right")){
                                direction = "Left";
                            }

                        }
                        if(game.tCard.getValue() == 1){

                        }

                        break;

                    }
                    else {
                        System.out.println(game.tCard);
                        System.out.println("Error: Card Invalid");
                    }
                }
                if (act == 2 && count < 3) {
                    //System.out.println("Top card in pile is: " + deck.get(deck.size()-1))
                    System.out.println("New Card has been drawn: " + deck.get(0).toString());
                    userHand.add(deck.get(0));
                    deck.remove(0);
                    count++;
                }
                if(act == 0){
                    System.out.println("Cheat Menu Discovered ^_^");
                    System.out.println();
                    System.out.println("(4) Set top card");
                    System.out.println("(5) Add card to hand");
                    System.out.println("(6) Empty current hand");
                }

                if (act == 4){
                    //Change top card

                    System.out.println("Select top card suit/value Ex. 0,0 -> 1C");
                    int selection = myObj.nextInt();
                    game.tCard.suit = selection;
                    selection = myObj.nextInt();
                    game.tCard.value = selection;
                    System.out.println("New Top Card: " + game.tCard.toString());
                }
                if (act == 5){
                    ArrayList<Card> moddedHand = new ArrayList<>();
                    //Insert card into hand

                    System.out.println("Select the card to add to hand Ex. 0,0 -> 1C");
                    int s = myObj.nextInt();
                    int v = myObj.nextInt();
                    userHand.add(new Card(s, v));
                    System.out.println("Card: " + userHand.get(userHand.size()-1) + " has been added to hand.");
                        //userHand = moddedHand;

                }
                if(act == 6){
                    //Clear hand

                    System.out.println("Hand has been emptied");
                    ArrayList<Card> emptyHand = new ArrayList<>();
                    userHand = emptyHand;


                }

                //ADD IN DIRECTION OF PLAYERS

            }end =1;
        }

        System.out.println();

        return deck;
    }

    public void cheatGame(){


    }

    public ArrayList<Card> rigDeck(){
        ArrayList<Card> riggedDeck = new ArrayList<>();
        //Value, Suit
        riggedDeck.add(new Card(0,12)); //KC

        return riggedDeck;

    }

    public int[] scoreRound(int r, int[] dieRoll) {
        /*
        if (r == 7)
            setScoreSheet(6, game.scoreThreeOfAKind(dieRoll));
        else if (r == 8)
            setScoreSheet(7, game.scoreFourOfAKind(dieRoll));
        else if (r == 9)
            setScoreSheet(8, game.scoreFullHouse(dieRoll));
        else if (r == 10)
            setScoreSheet(9, game.scoreSmallStraight(dieRoll));
        else if (r == 11)
            setScoreSheet(10, game.scoreLargeStraight(dieRoll));
        else if (r == 12)
            setScoreSheet(11, game.scoreYahtzee(dieRoll));
        else if (r == 13) {
            setScoreSheet(12, game.scoreChance(dieRoll));
        } else
            setScoreSheet(r - 1, game.scoreUpper(dieRoll, r));

         */
        return getScoreSheet();
    }

    public int getScore() {
        int sc = getLowerScore() + getUpperScore();
        if (getScoreSheet()[13] >= 0)
            sc += scoreSheet[13];
        if (getScoreSheet()[14] >= 0)
            sc += scoreSheet[14];
        return sc;
    }

    /*
     * loop through the first 6 elements of the score sheet and return
     */
    public int getUpperScore() {
        int count = 0;
        for (int i = 0; i < 6; i++) {
            if (this.getScoreSheet()[i] >= 0)
                count += this.scoreSheet[i];
        }
        return count;
    }

    /*
     * sum of elements 6 - 13 including the yahtzee bonus
     */
    public int getLowerScore() {
        int count = 0;
        for (int i = 6; i < 13; i++) {
            if (this.getScoreSheet()[i] >= 0)
                count += this.scoreSheet[i];
        }
        return count;
    }

    public int[] getScoreSheet() {
        return scoreSheet;
    }

    public void setScoreSheet(int cat, int score) {
        this.scoreSheet[cat] = score;
    }

    public void setScoreSheet(int[] ss) {
        this.scoreSheet = ss;
    }

    public Player getPlayer() {
        return this;
    }

    /*
     * ----------Network Stuff------------
     */

    /*
     * send the to do to test server
     */
    public void sendStringToServer(String str) {
        clientConnection.sendString(str);
    }

    public void connectToClient() {
        clientConnection = new Client();
    }

    public void connectToClient(int port) {
        clientConnection = new Client(port);
    }

    public void initializePlayers() {
        for (int i = 0; i < 4; i++) {
            players[i] = new Player(" ");
        }
    }


    /*
     * update turns
     */
    public void printPlayerScores(Player[] pl) {
        /*
        // print the score sheets

        if (playerId == 1) {
            game.printScoreSheet(pl[0]);
            game.printScoreSheet(pl[1]);
            game.printScoreSheet(pl[2]);
        } else if (playerId == 2) {
            game.printScoreSheet(pl[1]);
            game.printScoreSheet(pl[0]);
            game.printScoreSheet(pl[2]);
        } else {
            game.printScoreSheet(pl[2]);
            game.printScoreSheet(pl[0]);
            game.printScoreSheet(pl[1]);
        }*/
    }

    public void startGame() throws IOException {
        // receive players once for names
        players = clientConnection.receivePlayer();
       // int nextID = game.getNextID();
       // String topCard = clientConnection.receiveString();
        ArrayList<Card> h1 = clientConnection.receiveHand();
        int round;
        boolean end = false;

        while (!end) {
            round = clientConnection.receiveRoundNo();
            System.out.println("********Round Number " + round + "********");

            tCard = clientConnection.receiveCard();

            System.out.println();
            clientConnection.receiveScores();
            System.out.println();
            direction = clientConnection.receiveString();
            String skipped = clientConnection.receiveString();
            System.out.print("Hand: ");
            for(int i=0;i<h1.size();i++){
                System.out.print(h1.get(i).toString() +" ");
            }
            System.out.println();
            System.out.println("Direction: " + direction);
            if(skipped.equals("True")){
                System.out.println("Queen Played... Player " + round + "'s turn was skipped");
                continue;
            }
            game.setPickUpCard(tCard);
            System.out.println("Top Card: " +tCard.toString());
            System.out.println("ID: " + this.playerId );
            System.out.println("Player " + round + " is playing");
            if(round == this.playerId){
                if(round == 4 && direction.equals("Right")){
                    round =0;
                }
                if(round == 1 && direction.equals("Left")){
                    round = 5;
                }
                if(direction.equals("Right") ){
                    round++;
                }
                else if(direction.equals("Left")){
                    round--;
                }
                System.out.println("Player " + round + "'s turn is next" );
                System.out.println();
                deck = clientConnection.receiveDeck();
                clientConnection.sendDeck(playRound(h1));
                clientConnection.sendCard(game.tCard);
                clientConnection.sendScore(score);
                clientConnection.sendString(direction);
            }


            if (round == -1)
                break;

            game.setPickUpCard(tCard);

            end = clientConnection.receiveBoolean();

            if(end){
                //REceives index of winner
                int winner =  clientConnection.receiveRoundNo();
                System.out.println("Player " + players[winner].playerId + " has won the game!");

                System.out.println("Final Scores: ");
                clientConnection.receiveScores();

            }

        }

    }
    private class Client {
        Socket socket;
        private ObjectInputStream dIn;
        private ObjectOutputStream dOut;

        public Client() {
            try {
                socket = new Socket("localhost", 3333);
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());

                playerId = dIn.readInt();

                System.out.println("Connected as " + playerId);
                sendPlayer();

            } catch (IOException ex) {
                System.out.println("Client failed to open");
            }
        }

        public Client(int portId) {
            try {
                socket = new Socket("localhost", portId);
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());

                playerId = dIn.readInt();

                System.out.println("Connected as " + playerId);
                sendPlayer();

            } catch (IOException ex) {
                System.out.println("Client failed to open");
            }
        }

        public void sendCard(Card c){
            try{
                dOut.writeObject(c);
                dOut.flush();
            } catch (Exception e){
                System.out.println("Error");
                e.printStackTrace();
            }
        }
        public Card receiveCard(){
            Card c = new Card(0,0);
            try{
                c =(Card) dIn.readObject();
                return c;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return c;
        }


        /*
         * function to send the score sheet to the server
         */
        public void sendPlayer() {
            try {
                dOut.writeObject(getPlayer());
                dOut.flush();
            } catch (IOException ex) {
                System.out.println("Player not sent");
                ex.printStackTrace();
            }
        }

        /*
         * function to send strings
         */
        public void sendString(String str) {
            try {
                dOut.writeUTF(str);
                dOut.flush();
            } catch (IOException ex) {
                System.out.println("Player not sent");
                ex.printStackTrace();
            }
        }
        public void sendHand(ArrayList<Card> h){
            try{
                dOut.writeObject(h);
                dOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void sendDeck(ArrayList<Card> d){
            try{
                dOut.writeObject(d);
                dOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String receiveString() {
            String s = "";
            try {
                return dIn.readUTF();
            } catch (IOException e){
                System.out.println("Error");
            }
            return s;
        }

        public boolean receiveBoolean(){
            boolean b = false;
            try{
                b = dIn.readBoolean();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return b;
        }


        public ArrayList<Card> receiveHand(){
            ArrayList<Card> h = new ArrayList<>();
            try {
                h = (ArrayList<Card>) dIn.readObject();
                return h;

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return h;

        }

        public ArrayList<Card> receiveDeck(){
            ArrayList<Card> h = new ArrayList<>();
            try {
                h = (ArrayList<Card>) dIn.readObject();
                return h;

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return h;

        }


        /*
         * receive scoresheet
         */
        public void sendScores(int[] scores) {
            try {
                for (int i = 0; i < scores.length; i++) {
                    dOut.writeInt(scores[i]);
                }
                dOut.flush();

            } catch (IOException e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            }
        }

        /*
         * receive scores of other players
         */
        public Player[] receivePlayer() {
            Player[] pl = new Player[4];
            try {
                Player p = (Player) dIn.readObject();
                pl[0] = p;
                p = (Player) dIn.readObject();
                pl[1] = p;
                p = (Player) dIn.readObject();
                pl[2] = p;
                p = (Player) dIn.readObject();
                pl[3] = p;
                return pl;

            } catch (IOException e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                e.printStackTrace();
            }
            return pl;
        }

        /*
         * receive scores of other players
         */
        public int[] receiveScores() {
            try {
                int[] sc = new int[4];
                System.out.println("Scores: ");
                for (int i = 0; i < 4; i++) {
                    sc[i] = dIn.readInt();
                    System.out.println("Player " + (i+1) + ": " + sc[i]);
                }
                return sc;
            } catch (Exception e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            }
            return null;
        }
        public void sendScore(int s) {
            try {
                dOut.writeInt(s);
                dOut.flush();
            } catch (Exception e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            }
        }

        /*
         * receive scores of other players
         */
        public int receiveRoundNo() {
            int rnd =0;
            try {
                rnd = dIn.readInt();
            } catch (Exception e) {
                System.out.println("Invalid Round Number.");
                e.printStackTrace();
            }
            return rnd;

        }


    }

    /*
     * ---------Constructor and Main class-----------
     */

    /*
     * constructor takes the name of the player and sets the score to 0
     */
    public Player(String n) {
        name = n;
        for (int i = 0; i < scoreSheet.length; i++) {
            scoreSheet[i] = -1;
        }
    }

    public Player(String n, ArrayList<Card> h){
        name = n;
        hand = h;
    }
    public ArrayList<Card> getHand(){
        return this.hand;
    }




    public static void main(String args[]) throws IOException {
        Scanner myObj = new Scanner(System.in);
        System.out.print("What is your name ? ");
        String name = myObj.next();
        ArrayList<Card> hand = new ArrayList<>();
        Player p = new Player(name, hand);
        p.initializePlayers();
        p.connectToClient();
        p.startGame();
        //p.returnWinner();
        myObj.close();
    }
}
