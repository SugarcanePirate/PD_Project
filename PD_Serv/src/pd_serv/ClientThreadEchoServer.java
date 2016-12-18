/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String servName;
        

    public ClientThreadEchoServer(Map <String,ClientData> client, String servName) {
        this.clientdata = client;
        this.servName=servName;
    }
     

     public String makeClientDir(String cliName) {
        String nomeDirectoria = System.getProperty("user.dir");

        nomeDirectoria = nomeDirectoria + File.separator + servName + File.separator + cliName;

        File directoria = new File(nomeDirectoria);

        if (!directoria.exists()) {
            try {
                directoria.mkdir();
            } catch (Exception e) {
                return "";
            }
            
    }
        return nomeDirectoria;
 }
    
 public void run() {
  
    ServerSocket serverSocket = null;
    Socket socket = null;
    String name,password=null;
    String connected=null;
    Object ob;
    boolean registered=false;
    
    String data;
    
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("I/O error: "+e);

        }
        while (true) {
            try {
                registered=false;
                socket = serverSocket.accept();
                
                
                oos = new ObjectOutputStream( socket.getOutputStream() );
                ois = new ObjectInputStream(socket.getInputStream());

                System.out.println("Client socket open" );
                
                data = (String)ois.readObject();
                Scanner scan = new Scanner(data);
                name = scan.next();
                password = scan.next();

                if (!clientdata.containsKey(name)) {   
                    //verifica se ja existe cliente com o mesmo nome
                clientdata.put(name,new ClientData(name, password));
                            // nova thread para cada cliente
                clientdata.get(name).setHomeDir(makeClientDir(name));
                clientdata.get(name).setCurrentDir(clientdata.get(name).getHomeDir());
                registered = true;
                

                
                System.out.println("Client arrived:" + name);
                 
            }
                oos.flush();
                oos.writeObject(registered);
                oos.flush();
                
                
                
                if(!registered)
                System.out.println("Client denied:" + name);
                else{
               Thread t = new EchoThread(socket,clientdata,oos,ois,clientdata.get(name));
               t.start();
                }
                
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThreadEchoServer.class.getName()).log(Level.SEVERE, null, ex);
        }

            
        }
    }
   
}
   

