/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_dirserv;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author David
 */
public class ClientsConnectionThread extends Thread{
    public final static int PORT_UDP_CONN = 6002;
    public static final int MAX_SIZE = 5000;
    int myPort;
    DatagramSocket socketToClient =null;
    List <String>clientList;
    String myIp;
    Map<String,Server> serverList;

    public ClientsConnectionThread(String myIp, int myPort,Map<String,Server> serverList) {
        this.myIp = myIp;
        this.myPort = myPort;
        clientList = new ArrayList<>();
        this.serverList = serverList;
    }
    
   
    
    
    @Override
    public void run(){
        byte[] buff = new byte[MAX_SIZE];
        String name,clientIp;
        String connected = "";
        DatagramPacket packetToReceive;
        DatagramPacket packetToSend;
        InetAddress addr = null;
        ByteArrayOutputStream byteArray = null;
        ObjectOutputStream os = null;
        
         
        try {
            socketToClient = new DatagramSocket(myPort);
        } catch (SocketException ex) {
            Logger.getLogger(HeartBeatReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        packetToReceive = new DatagramPacket(buff, MAX_SIZE);
     
        while(true){
        try{
            System.out.println("Waiting for clients");
            
            socketToClient.receive(packetToReceive);
            System.out.println("Client arrived");
            
            break;
        }catch(IOException e){
            System.out.println("Error receiving message : " + e);
        }
               
        }
        
            byte[] data = packetToReceive.getData();  //recebe nome do servidor e os dados ip/porto

             System.out.println("Reading client data");
            
            String username = new String(data);
         
            Scanner scan = new Scanner(username);

            name = scan.next();
            clientIp = scan.next();
            
            if (!clientList.contains(name)) {      //verifica se ja existe servidor com mesmo nome,
                clientList.add(name);
                connected = 1 + " ";
            }
            
            
            
            try {
                addr = InetAddress.getByName(clientIp);
            } catch (UnknownHostException e) {
                System.err.println("Error - " + e);
            }

           
        try {
             byteArray = new ByteArrayOutputStream(MAX_SIZE);
            os = new ObjectOutputStream(new BufferedOutputStream(byteArray));
            os.flush();
            os.writeObject(getServerList());
            os.flush();
            byte[] sendBuffer = byteArray.toByteArray();
                        packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, addr, PORT_UDP_CONN);  // envia ao servidor uma string a dizer se ja existe o nome
            socketToClient.send(packetToSend);
            os.close();
            System.out.println("Server list sent");
            
        } catch (IOException ex) {
            Logger.getLogger(ClientsConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
            


            System.out.println("Sending Packet");


            
          
        
    }
 public String[] getServerList(){
     int i=0;
    String[] list = new String[serverList.size()];
    for(String key: serverList.keySet()){
        Server s = serverList.get(key);
        list[i] = s.getName() + " " + s.getIp() + " " + s.getPort();
        i++;
    }
    return list;
}
}


