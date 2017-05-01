import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Client {
    public static final int PORT_OFF=6000;
    public static final int CLIENTS=4;
    public static void main(String args[]){
            for(int i=0;i<1;i++){
                try {
                    Socket socket=new Socket("192.168.43.66",6666);
                    (new Thread(new Peer(socket,PORT_OFF+i))).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}

class Peer implements Runnable{
    private ServerSocket serverSocket;  // My server socket
    private Socket socket;              // Socket used to communicate with the server
    private Integer port;               // My port
    private Socket[] sockets;           // prev(0) and next(1)
    public Peer(Socket socket,Integer port){
        this.socket=socket;
        this.port=port;
        this.sockets=new Socket[2];
        try {serverSocket=new ServerSocket(port);}
        catch (IOException e) {e.printStackTrace();}
    }
    @Override
    public void run() {
            MessageHandler serverHandler=new MessageHandler(socket);
            serverHandler.sendMessage(Integer.toString(port));  //send port to server
            
        /*
            try {
            serverHandler.sendMessage(Integer.toString(port)+";"+Inet4Address.);  //send port to server
        } catch (UnknownHostException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
            String[] arr=serverHandler.getMessage().split(";"); // receive prev/next
            (new Thread(new PeerServer(sockets,serverSocket))).start(); // start personal server
            for(int i=0;i<2;i++) {
                try {
                    Socket socket=new Socket(arr[i].split("-")[1],Integer.parseInt(arr[i].split("-")[0]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            serverHandler.sendMessage("Peer "+port+" is good !"); // connection with prev/next is done!
            System.out.println(serverHandler.getMessage());
    }
}

class PeerServer implements Runnable{
    private Socket[] sockets;
    private ServerSocket serverSocket;
    public PeerServer(Socket[] sockets,ServerSocket serverSocket){
        this.sockets=sockets;
        this.serverSocket=serverSocket;
    }
    @Override
    public void run() {
            for (int i=0;i<2;i++) {
                try {
                    sockets[i] = serverSocket.accept();
                    System.out.println("Connected with other Client");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}

class MessageHandler{
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;
    public MessageHandler(Socket socket){
        this.socket=socket;
        try {
            pw=new PrintWriter(this.socket.getOutputStream());
            br=new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String str){
        pw.println(str);
        pw.flush();
    }
    public String getMessage(){
        String str=null;
        try {
            str=br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintWriter getPw() {
        return pw;
    }

    public void setPw(PrintWriter pw) {
        this.pw = pw;
    }

    public BufferedReader getBr() {
        return br;
    }

    public void setBr(BufferedReader br) {
        this.br = br;
    }
}

