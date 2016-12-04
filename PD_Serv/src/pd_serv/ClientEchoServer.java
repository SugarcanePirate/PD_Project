/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_serv;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author David
 */
public class ClientEchoServer extends Thread{
    
        ArrayList <ClientData> client;
        ServerSocket serverSocket = null;
        Socket socket = null;
     static final int PORT = 6003;
     
 public ClientEchoServer(ArrayList <ClientData> client) {
             this.client = client;
           }
 
  public void run() {
     String name=null;
     String password=null;
     String connected = "";
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("I/O error: "+e);

        }

        while (true) {
            try {
                socket = serverSocket.accept();
                
                if (!client.contains(name)) {      //verifica se ja existe servidor com mesmo nome,
                client.add(new ClientData(name, password));
                connected = 1 + " " ;
            }
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // nova thread para cada cliente
            new ClientEchoThread(socket).start();
            
        }
    }
   
}
   

