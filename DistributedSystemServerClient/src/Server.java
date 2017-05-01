import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
        public static final int GOAL=100;
        public static final int CLIENTS=2;
        public static List<PrintWriter> pws=new ArrayList<>();
        public static List<BufferedReader> brs=new ArrayList<>();
        public static List<Socket> clients=new ArrayList<>();
        public static void main(String[] args){
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(6666);
                for (int i = 0; i < CLIENTS; i++) {
                    clients.add(serverSocket.accept());
                    System.out.println("client : {"+i+"} connected!");
                }
                startGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void startGame() throws IOException{
                initIO();
                broadCastMessage("the game has started!");
                int count=0;
                int client=-1;
                while(count<GOAL){
                    client=(client+1)%CLIENTS;
                    pws.get(client).print("go!");
                    pws.get(client).flush();
                    pws.get(client).println(count);
                    pws.get(client).flush();
                    count=toInt(brs.get(client).readLine());
                }
                broadCastMessage("Game Over!");
                broadCastMessage("The winner is : Client "+client);
        }
        public static void initIO() throws IOException{
            for(int i=0;i<CLIENTS;i++){
                pws.add(new PrintWriter(clients.get(i).getOutputStream()));
                brs.add(new BufferedReader(new InputStreamReader(clients.get(i).getInputStream())));
            }
        }
        public static void broadCastMessage(String message){
            System.out.println(message);
            for(int i=0;i<CLIENTS;i++){
                pws.get(i).println(message);
                pws.get(i).flush();
            }
        }
        public static Integer toInt(String str){return Integer.parseInt(str);}
}