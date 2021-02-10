package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

//Taken from Professor Jean-Pierre Corriveau's Yahtzee code posted on cuLearn and adapted for this assignment


public class Player implements Serializable {
    public String name;
    int playerID = 0;
    static Client clientConnection;
    public int score=0;
    private static final long serialVersionUID = 1L;

    Game game = new Game();
    Player[] players = new Player[4];
    private Card[] hand = new Card[4];

    public Card[] playRound(Card[] deck) {
        /*
        Playing logic
        1. Checks cards in hand to see if they can play
        2. If yes, play card.
        3. if no, pickup top card on the pile.
        4. If the pickup card can be played, play it.

       Scoring logic:
       1. User plays an 8 card, user#.setScore(user#.getScore() += 50)
       2. User plays a King, Queen or Jack, user#.setScore(user#.getScore() += 10)
       3. All other cards are their value, user#.setScore(user#.getScore += user#.getHand(i).getValue())


         */


        return deck;
    }

    public Player getPlayer() {
        return this;
    }
    public int getScore(){
        return score;
    }
    public void setScore(int s){
        this.score = s;

    }
    public void sendStringToServer(String str) {
        clientConnection.sendString(str);
    }

    public void connectToClient(){
        clientConnection = new Client();
    }
    public void connectToClient(int port){
        clientConnection = new Client(port);

    }

    public void initPlayers(){
        for(int i=0;i<4;i++){
            players[i] = new Player("");
        }
    }
    public void startGame() {
        // receive players once for names
        /*
        players = clientConnection.receivePlayer();
        while (true) {
            int round = clientConnection.receiveRoundNo();
            if (round == -1)
                break;
            System.out.println("\n \n \n ********Round Number " + round + "********");
            int[][] pl = clientConnection.receiveScores();
            for (int i = 0; i < 3; i++) {
                players[i].setScoreSheet(pl[i]);
            }
            printPlayerScores(players);
            int[] dieRoll = game.rollDice();
            clientConnection.sendScores(playRound(dieRoll));
        }

         */

    }


    public class Client {
        Socket socket;
        private ObjectInputStream dIn;
        private ObjectOutputStream dOut;

        public Client() {


            try {
                socket = new Socket("localhost", 3333);
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());

                playerID = dIn.readInt();

                System.out.println("Connected as " + playerID);
               // sendString(players[playerID-1].name);
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

                playerID = dIn.readInt();

                System.out.println("Connected as " + playerID);
                sendPlayer();

            } catch (IOException ex) {
                System.out.println("Client failed to open");
            }
        }

        public void sendPlayer() {
            try {
                dOut.writeObject(getPlayer());
                dOut.flush();
            } catch (IOException ex) {
                System.out.println("Player not sent");
                ex.printStackTrace();
            }
        }
        public void sendString(String str) {
            try {
                dOut.writeUTF(str);
                dOut.flush();
            } catch (IOException ex) {
                System.out.println("Player not sent");
                ex.printStackTrace();
            }
        }

    }
    /*
    public Player[] receivePlayer() {
        Player[] pl = new Player[3];
        try {
            Player p = (Player) dIn.readObject();
            pl[0] = p;
            p = (Player) dIn.readObject();
            pl[1] = p;
            p = (Player) dIn.readObject();
            pl[2] = p;
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

     */

    public Player(String n){
        name = n;
        for(int i=0;i< hand.length;i++){
            //Init to be a new card of 1C
            hand[i] = new Card(0,0);
        }
    }


    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        System.out.print("What is your name ? ");
        String name = myObj.next();
        Player p = new Player(name);


       // p.initializePlayers();
        p.connectToClient();
        //p.startGame();
        //p.returnWinner();
        myObj.close();

    }
}