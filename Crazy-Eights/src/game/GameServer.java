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
    String skip = "False";

    String direction = "Right";

    Server[] playerServer = new Server[4];
    Player[] players = new Player[4];
    int[] scores;
    ServerSocket ss;
    int currentPlayerID =0;
    public Card tCard;
    Game game = new Game();
    boolean end = false;

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
        int[] scores = {0,0,0,0};

        //TO-DO
        int roundID = 1;
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

           

            playerServer[0].sendHand(game.rigHandPart1(1));
            playerServer[1].sendHand(game.rigHandPart1(2));
            playerServer[2].sendHand(game.rigHandPart1(3));
            playerServer[3].sendHand(game.rigHandPart1(4));

            pile = game.rigDeckPart1();
           

          

            playerServer[0].sendHand(game.rigHandsPartTwosCard(1));
            playerServer[1].sendHand(game.rigHandsPartTwosCard(2));
            playerServer[2].sendHand(game.rigHandsPartTwosCard(3));
            playerServer[3].sendHand(game.rigHandsPartTwosCard(4));



        
            pile = game.rigDeckPartDraw();





            

            playerServer[0].sendHand(game.rigHands3(1));
            playerServer[1].sendHand(game.rigHands3(2));
            playerServer[2].sendHand(game.rigHands3(3));
            playerServer[3].sendHand(game.rigHands3(4));


            pile = game.rigDeckPart3();
               




            
            playerServer[0].sendHand(game.rigHandsPart32(1));
            playerServer[1].sendHand(game.rigHandsPart32(2));
            playerServer[2].sendHand(game.rigHandsPart32(3));
            playerServer[3].sendHand(game.rigHandsPart32(4));
            pile = game.rigDeckPart32();
            



            playerServer[0].sendHand(h1);
            playerServer[1].sendHand(h2);
            playerServer[2].sendHand(h3);
            playerServer[3].sendHand(h4);

            game.setPickUpCard(pile.get(0));

            pile.remove(0);


            while(!end){
                playerServer[0].sendRoundNo(roundID);
                playerServer[1].sendRoundNo(roundID);
                playerServer[2].sendRoundNo(roundID);
                playerServer[3].sendRoundNo(roundID);

                playerServer[0].sendCard(game.tCard);
                playerServer[1].sendCard(game.tCard);
                playerServer[2].sendCard(game.tCard);
                playerServer[3].sendCard(game.tCard);

                //Send scores here
                playerServer[0].sendScoresList(scores);
                playerServer[1].sendScoresList(scores);
                playerServer[2].sendScoresList(scores);
                playerServer[3].sendScoresList(scores);

                playerServer[0].sendString(direction);
                playerServer[1].sendString(direction);
                playerServer[2].sendString(direction);
                playerServer[3].sendString(direction);

                playerServer[0].sendString(skip);
                playerServer[1].sendString(skip);
                playerServer[2].sendString(skip);
                playerServer[3].sendString(skip);

                if(skip.equals("True")){
                    skip = "False";
                    if(roundID == 4 && direction.equals("Right")){
                        roundID =0;
                    }
                    if(roundID == 1 && direction.equals("Left")){
                        roundID = 5;
                    }
                    if(direction.equals("Right") ){
                        roundID++;
                    }
                    else {
                        roundID--;
                    }
                    continue;
                }


                //game.tCard = pile.get(0);
                //System.out.println(game.tCard.toString());

                System.out.println("Current Top Card: " + game.tCard.toString());
                //Send the Deck

                System.out.println(roundID);
                System.out.println("Deck: ");
                for(int i=0;i<pile.size();i++){
                    System.out.print(pile.get(i).toString() + " ");
                }

                playerServer[roundID-1].sendDeckPile(pile);

                pile = playerServer[roundID-1].receiveDeck();
                tCard = playerServer[roundID-1].receiveCard();
                scores[roundID-1] += playerServer[roundID-1].receiveScore();
                game.setPickUpCard(tCard);
                //System.out.println(tCard);

                direction = playerServer[roundID-1].receiveString();

                if(game.tCard.getValue() == 11){
                   skip = "True";
                }

                for(int i=0;i<scores.length;i++){
                    System.out.println("PLayer " + (i+1) + " score: "+ scores[i]);
                }

                //Go through scores, check if there is a winner
                //print that winner
                //flag to stop game

                if(roundID == 4 && direction.equals("Right")){
                    roundID =0;
                }
                if(roundID == 1 && direction.equals("Left")){
                    roundID = 5;
                }
                if(direction.equals("Right") ){
                    roundID++;
                }
                else {
                    roundID--;
                }
                //roundID++;

                if(game.isGameFinished(scores)){
                    end = true;
                    playerServer[0].sendBoolean(end);
                    playerServer[1].sendBoolean(end);
                    playerServer[2].sendBoolean(end);
                    playerServer[3].sendBoolean(end);
                    //NOT SENDING RND NO, THIS SENDS INDEX OF WINNER
                    playerServer[0].sendRoundNo(game.getWinner(scores));
                    playerServer[1].sendRoundNo(game.getWinner(scores));
                    playerServer[2].sendRoundNo(game.getWinner(scores));
                    playerServer[3].sendRoundNo(game.getWinner(scores));

                    playerServer[0].sendScoresList(scores);
                    playerServer[1].sendScoresList(scores);
                    playerServer[2].sendScoresList(scores);
                    playerServer[3].sendScoresList(scores);
                    continue;
                }
                playerServer[0].sendBoolean(end);
                playerServer[1].sendBoolean(end);
                playerServer[2].sendBoolean(end);
                playerServer[3].sendBoolean(end);


            }

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

        public void sendScoresList(int[] scores) {
            try {
                for (int i = 0; i < scores.length; i++) {
                    dOut.writeInt(scores[i]);
                }
                dOut.flush();

            } catch (IOException e) {
                System.out.println("Scores not received");
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

        public void sendBoolean(boolean b){
            try{
                dOut.writeBoolean(b);
                dOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        /*
         * receive scores of other players
         */


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
        public int receiveScore(){
            int score =0;
            try{
                score = dIn.readInt();
                return score;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return score;
        }

        /*
         * send scores of other players
         */
        public void sendScores(int[] pl) {
            
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

             
        }



    }
}









