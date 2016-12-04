/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_serv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author David
 */
public class ClientThreadEchoServer extends Thread{
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Map <String,ClientData> clientdata;
        ServerSocket serverSocket = null;
        Socket socket = null;
        static final int PORT = 6003;

    public ClientThreadEchoServer(Map <String,ClientData> client) {
        this.clientdata = client;
    }
     

 public void run() {
  
    ServerSocket serverSocket = null;
    Socket socket = null;
    String name,password=null;
    String connected=null;
    Object ob;
    BufferedReader in;
    String data;
    
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("I/O error: "+e);

        }
        while (true) {
            try {
                socket = serverSocket.accept();
                
                
                oos = new ObjectOutputStream( socket.getOutputStream() );
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                
                data = in.readLine();
                Scanner scan = new Scanner(data);
                name = scan.next();
                password = scan.next();

                if (!clientdata.containsKey(name)) {      //verifica se ja existe cliente com o mesmo nome
                clientdata.put(name,new ClientData(name, password));
                connected = 1 + " " ;
            }
          
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // nova thread para cada cliente
            new EchoThread(socket).start();
            
        }
    }
   
}
   

