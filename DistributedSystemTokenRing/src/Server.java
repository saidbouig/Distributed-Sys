import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public static final int GOAL=100;
    public static final int CLIENTS=4;
    public static Scanner sc=new Scanner(System.in);
    public static List<PrintWriter> pws=new ArrayList<>();
    public static List<BufferedReader> brs=new ArrayList<>();
    public static List<Socket> clients=new ArrayList<>();
    public static Map<Integer,Pair> ring=new HashMap<>();
    public static List<String> ports=new ArrayList<>();
    public static void main(String[] args){

        try {
            acceptConnections();
            sc.nextLine();
            makeRing(); // create ring

            /* start making the ring */
            for(int i=0;i<CLIENTS;i++){
                pws.get(i).println(ring.get(i));
                pws.get(i).flush();
            }

            /* get peers connection messages*/
            for(int i=0;i<CLIENTS;i++){
                System.out.println(brs.get(i).readLine());
            }

            /* send token test*/
            int rand=(new Random()).nextInt()%CLIENTS;
            pws.get(rand).println("*");
            pws.get(rand).flush();
            for(int i=0;i<CLIENTS;i++)
                if(i!=rand)
                {
                    pws.get(i).println("-");
                    pws.get(i).flush();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void acceptConnections() throws IOException{
        ServerSocket serverSocket = new ServerSocket(6666);
        for (int i = 0; i < CLIENTS; i++) {
            clients.add(serverSocket.accept());
            System.out.println("client("+i+") connected!");
            pws.add(new PrintWriter(clients.get(i).getOutputStream()));
            brs.add(new BufferedReader(new InputStreamReader(clients.get(i).getInputStream())));
            ports.add(brs.get(i).readLine());
            System.out.println("Client("+i+") sent port number :"+ports.get(i));
        }
        System.out.println("All clients are connected.");
    }

    public static void makeRing(){
        for(int i=0;i<CLIENTS;i++)
            ring.put(i,new Pair(ports.get((i-1+CLIENTS)%CLIENTS),ports.get((i+1+CLIENTS)%CLIENTS)));
    }

    public static void broadCastMessage(String message){
        System.out.println(message);
        for(int i=0;i<CLIENTS;i++) {
            pws.get(i).println(message);
            pws.get(i).flush();
        }
    }

    public static void sendMessage(String message,Integer client){
        System.out.println(message);
        pws.get(client).println(message);
        pws.get(client).flush();
    }

    public static Integer toInt(String str){return Integer.parseInt(str);}
}
