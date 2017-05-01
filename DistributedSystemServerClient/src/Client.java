import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        Socket socket;
        BufferedReader br;
        PrintWriter pw;
        Integer a;
        String message;
        try {
            socket=new Socket("192.168.43.66",6666);
            Scanner sc=new Scanner(System.in);
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw=new PrintWriter(socket.getOutputStream());
            System.out.println("connected !");
            System.out.println("waiting for the game to start ...");
            System.out.println(br.readLine());
            while(!(message=br.readLine()).equals("Game Over!")){
                System.out.println(message);
                a=toInt(br.readLine());
                sc.nextLine();
                pw.println(++a);
                System.out.println("new token value: "+a);
                pw.flush();
            }
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static Integer toInt(String str){return Integer.parseInt(str);}
}