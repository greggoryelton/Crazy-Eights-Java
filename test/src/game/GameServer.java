package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

//Taken from Professor Jean-Pierre Corriveau's Yahtzee code posted on cuLearn and adapted for this assignment

public class GameServer {
    private int numPlayers;
    private int MAX_TURNS;

    Server[] playerServer = new Server[4];
    Player[] players = new Player[4];
    ServerSocket ss;

    public static void main(String[] args) {
        GameServer sr = new GameServer();
         //sr.acceptConnections();
        //sr.gameLoop();

    }

    public GameServer(){
        System.out.println("Starting Crazy-Eights Server...");
        numPlayers = 0;

    }

    public void acceptConnections() throws ClassNotFoundException, IOException {
        try{
            System.out.println("Waiting for players");
            while(numPlayers < 3){
                Socket s = ss.accept();
                numPlayers++;

                Server server = new Server(s,numPlayers);
                server.dOut.flush();

                Player in = (Player) server.dIn.readObject();
                System.out.println("Player "+server.playerId + " ~ " + in.name  + " ~ has joined" );
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

    public void gameLoop(){
        //TO-DO
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
    }
}









