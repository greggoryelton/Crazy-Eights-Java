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
    public Card tCard;

    static Client clientConnection;



    Player[] players = new Player[4];
//	private ArrayList<String> scoreSheetKey = new ArrayList<String>(Arrays.asList("one", "two", "three", "four", "five",
//			"six", "3ok", "4ok", "full", "sst", "lst", "yahtzee", "chance", "bonus"));

    /*
     * play a round of the game
     */
    public Card playRound(ArrayList<Card> userHand) {

        Scanner myObj = new Scanner(System.in);
        int count = 0; // reroll 3 times
        int end = 0;
        //game.printHand(dieRoll);
        while (end == 0) {
            System.out.println("Select an action: ");
            if (count < 3) {
                System.out.println("(1) Choose a card to play from your hand (ie. 0,1,2,3 or 4)");
                System.out.println("(2) Draw a new card");
            }

            int act = myObj.nextInt();
            if(act == 1 && count < 3){
                System.out.println("Choose a card to play from 0-4");
                int c = myObj.nextInt();
                if((c > 4 || c<0) || (userHand.get(c).getSuit() != tCard.getSuit() && userHand.get(c).getValue() != tCard.getValue())){
                    System.out.println("Invalid Entry.");
                    c = myObj.nextInt();
                }

                clientConnection.sendCard(userHand.get(c));
                System.out.println("Top Card: " + tCard.toString());
                System.out.println("Success");
                return userHand.get(c);
            }
            if(act == 2 && count < 3){
                System.out.println("Drawing new card...");

            }

            end =1;


        }
        return new Card(-1,-1);



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

        while (true) {
            String turn = clientConnection.receiveString();
            String topCard = clientConnection.receiveString();
            ArrayList<Card> h1 = clientConnection.receiveHand();
            Card discardTop = clientConnection.receiveCard();


            System.out.println(turn);
            System.out.println(topCard);
            System.out.println("Your hand is: " + h1.get(0).toString() + ", " + h1.get(1).toString() + ", "+ h1.get(2).toString() + ", "+ h1.get(3).toString() + ", " + h1.get(4).toString());
            tCard = discardTop;

            int round = clientConnection.receiveRoundNo();
            if (round == -1)
                break;
            System.out.println("********Round Number " + round + "********");
            //int[][] pl = clientConnection.receiveScores();


            clientConnection.sendCard(playRound(h1));
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

        public String receiveString() {
            String s = "";
            try {
                return dIn.readUTF();
            } catch (IOException e){
                System.out.println("Error");
            }
            return s;
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
        public int[][] receiveScores() {
            try {
                int[][] sc = new int[3][15];
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < 15; i++) {
                        sc[j][i] = dIn.readInt();
                    }
                    System.out.println();
                }

                return sc;
            } catch (Exception e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            }
            return null;
        }

        /*
         * receive scores of other players
         */
        public int receiveRoundNo() {
            roundNum++;
            try {
                return dIn.readInt();
            } catch (Exception e) {
                System.out.println("Invalid Round Number.");
                e.printStackTrace();
            }
            return 0;
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
