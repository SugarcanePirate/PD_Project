package pd_dirserv;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author David
 */
public class ServersConnectionThread extends Thread{
   public static final int MAX_SIZE = 256;
   int dirServPort;
    String dirServIP;
    DatagramSocket socket;
    Map<String,Server> serverList;

    public ServersConnectionThread(DatagramSocket socket,String dirServIP,int dirServPort,Map<String,Server> serverList) {
        this.socket = socket;
        this.dirServIP = dirServIP;
        this.dirServPort = dirServPort;
        this.serverList = serverList;
    }
    
 
     
    @Override
    public void run() {
        String connected = "0";
        byte[] buff = new byte[MAX_SIZE];
        String ip;
        DatagramPacket packetToSend = null;
        DatagramPacket packetToReceive;
        String name;
        int port;
        InetAddress addr=null;
        
        packetToReceive = new DatagramPacket(buff, MAX_SIZE);
        while(true){
        try{
            System.out.println("Receiving packet");
             socket.receive(packetToReceive);
        System.out.println("waiting");
        }catch(IOException e){
            System.out.println("Erro a receber a mensagem : " + e);
        }
                
            
        
        byte[] data = packetToReceive.getData();
        
        String hb = new String(data);
        
        Scanner scan = new Scanner(hb);
        
        name = scan.next();
        port = scan.nextInt();
        ip = scan.next();
        
        if(!serverList.containsKey(name)){
            serverList.put(name, new Server(socket,name,ip,port));
            connected = "1";
        }
                    
        
        byte[] sendBuffer = connected.getBytes();
        try {
            addr = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            System.err.println("Error - " + e);
        }

            System.out.println("Sending Packet");
        
        packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, addr, dirServPort);

        try {
            System.out.println("Sending name and listening port...");
            socket.send(packetToSend);
        } catch (IOException e) {
            System.err.println("Error sending the name and listening port... - " + e);
        }
        
//        msg = new String(packetToReceive.getData(), 0, packetToReceive.getLength());
//        
//        System.out.println("Confirming connection to: " + msg);
//        
        }
    }
    
}
