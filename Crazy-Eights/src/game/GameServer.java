package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;


//Taken from Professor Jean-Pierre Corriveau's Yahtzee code posted on cuLearn and adapted for this assignment

public class GameServer implements Serializable {
    public ArrayList<Card> pile = Card.initDeck();
    private int numPlayers;
    private int MAX_TURNS;
    private static final long serialVersionUID = 1L;

    Server[] playerServer = new Server[4];
    Player[] players = new Player[4];
    int[] scores;
    ServerSocket ss;
    int currentPlayerID =0;
    public Card tCard;
    Game game = new Game();

    public static void main(String[] args) throws  ClassNotFoundException {
        GameServer sr = new GameServer();
        sr.acceptConnections();
        sr.gameLoop();

    }

    public GameServer(){
        System.out.println("Starting Crazy-Eights Server...");
        numPlayers = 0;
        ArrayList<Card> hand = new ArrayList<>();


        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(" ");
        }

        try {
            ss = new ServerSocket(3333);
        } catch (IOException ex) {
            System.out.println("Server Failed to open");
        }
    }

    public ArrayList<Card> randomizeHand(ArrayList<Card> d){
        ArrayList<Card> h = new ArrayList<>();
        h.add(0,d.get(0));
        h.add(1,d.get(1));
        h.add(2,d.get(2));
        h.add(3,d.get(3));
        h.add(4,d.get(4));



        return h;


    }

    public ArrayList<Card> removeDealtCards(ArrayList<Card> hand, ArrayList<Card> deck){
        ArrayList<Card> newDeck = new ArrayList<>();
       for(int i=0;i<deck.size();i++){
         for(int j=0;j<5;j++){
             if(deck.get(i) != hand.get(j)){
                 newDeck.add(deck.get(i));
             }
         }
       }
        return newDeck;


    }

    public void acceptConnections() throws ClassNotFoundException {
        try{
            System.out.println("Waiting for players");
            while(numPlayers < 4){
                Socket s = ss.accept();
                numPlayers++;

                Server server = new Server(s,numPlayers);
                server.dOut.writeInt(server.playerId);
                server.dOut.flush();

                Player in = (Player) server.dIn.readObject();

                System.out.println("Player "+server.playerId + " ~ " + in.name  + " ~ has joined" );
                //System.out.println(pname);
                players[server.playerId-1] = in;
                playerServer[numPlayers -1] = server;

            }
            for (Server server : playerServer) {
                Thread t = new Thread(server);
                t.start();
            }
        }catch (IOException e){
            System.out.println("Could not connect players");
        }
    }



    public void gameLoop() {
        Boolean flag = false;
        //TO-DO
        int roundNum = 1;
        ArrayList<Card> h1;
        ArrayList<Card> h2;
        ArrayList<Card> h3;
        ArrayList<Card> h4;
        ArrayList<Card> pickupPile;
        String buffer = "";
        h1 = randomizeHand(pile);
        for(int i=0;i<5;i++){
            pile.remove(0);
        }


        h2 = randomizeHand(pile);
        for(int i=0;i<5;i++){
            pile.remove(0);
        }


        h3 = randomizeHand(pile);
        for(int i=0;i<5;i++){
            pile.remove(0);
        }

        h4 = randomizeHand(pile);
        for(int i=0;i<5;i++){
            pile.remove(0);
        }
        try {
            playerServer[0].sendPlayers(players);
            playerServer[1].sendPlayers(players);
            playerServer[2].sendPlayers(players);
            playerServer[3].sendPlayers(players);
            playerServer[0].sendString("Your turn");
            playerServer[1].sendString("Player " + players[0].playerId + "'s turn");
            playerServer[2].sendString("Player " + players[0].playerId + "'s turn");
            playerServer[3].sendString("Player " + players[0].playerId + "'s turn");
            playerServer[0].sendString("Top card in pile is: " + pile.get(0).toString());
            playerServer[1].sendString("Top card in pile is: " + pile.get(0).toString());
            playerServer[2].sendString("Top card in pile is: " + pile.get(0).toString());
            playerServer[3].sendString("Top card in pile is: " + pile.get(0).toString());
            playerServer[0].sendHand(h1);
            playerServer[1].sendHand(h2);
            playerServer[2].sendHand(h3);
            playerServer[3].sendHand(h4);
            playerServer[0].sendRoundNo(roundNum);
            playerServer[1].sendRoundNo(roundNum);
            playerServer[2].sendRoundNo(roundNum);
            playerServer[3].sendRoundNo(roundNum);

            game.setPickUpCard(pile.get(0));

            pile.remove(0);

            System.out.println(game.tCard.toString());

            //Send the Deck
            playerServer[0].sendDeckPile(pile);
            playerServer[1].sendDeckPile(pile);
            playerServer[2].sendDeckPile(pile);
            playerServer[3].sendDeckPile(pile);

            playerServer[0].sendCard(game.tCard);


            pickupPile = pile;

            pickupPile = playerServer[0].receiveDeck();


            //System.out.println(top);
            //currentPlayerID = playerServer[0].receiveTurnID();
            System.out.println(game.playerTurnID);



        }

        catch (Exception e){
            System.out.println("ARGHHH");
            e.printStackTrace();
        }


    }

    public class Server implements Runnable {
        private Socket socket;
        private ObjectInputStream dIn;
        private ObjectOutputStream dOut;
        private int playerId;

        public Server(Socket s, int pID) {
            socket = s;
            playerId = pID;
            try {
                dOut = new ObjectOutputStream(socket.getOutputStream());
                dIn = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                System.out.println("Server Connection failed");
            }
        }


        public void run() {
            try {
                while (true){

                }
            } catch (Exception e){
                System.out.println("Run Failed");
                e.printStackTrace();
            }
        }
        public void sendPlayers(Player[] pl) {
            try {
                for (Player p : pl) {
                    dOut.writeObject(p);
                    dOut.flush();
                }

            } catch (IOException ex) {
                System.out.println("Score sheet not sent");
                ex.printStackTrace();
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
        public void sendString(String str) {
            try {
                dOut.writeUTF(str);
                dOut.flush();
            } catch (IOException ex) {
                System.out.println("String not sent");
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
        public ArrayList<Card> receiveDeck() {
            ArrayList<Card> h = new ArrayList<>();
            try {
                h = (ArrayList<Card>) dIn.readObject();
                return h;

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return h;

        }
        public void sendDeckPile(ArrayList<Card> d){
            try{
                dOut.writeObject(d);
                dOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /*
         * receive scores of other players
         */
        public void sendRoundNo(int r) {
            try {
                dOut.writeInt(r);
                dOut.flush();
            } catch (Exception e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
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
         * receive scores of other players
         */
        public int[] receiveScores() {
            try {
                int[] sc = new int[15];
                for (int i = 0; i < 15; i++) {
                    sc[i] = dIn.readInt();
                }
                return sc;
            } catch (Exception e) {
                System.out.println("Score sheet not received");
                e.printStackTrace();
            }
            return null;
        }
        public int receiveTurnID(){
            int id =0;
            try{
                id = dIn.readInt();
                return id;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return id;
        }

        /*
         * send scores of other players
         */
        public void sendScores(int[] pl) {
            /*
            try {
                for (int i = 0; i < pl.length; i++) {
                    for (int j = 0; j < pl[i].getScoreSheet().length; j++) {
                        dOut.writeInt(pl[i].getScoreSheet()[j]);
                    }
                }
                dOut.flush();
            } catch (Exception e) {
                System.out.println("Score sheet not sent");
                e.printStackTrace();
            }

             */
        }



    }
}









