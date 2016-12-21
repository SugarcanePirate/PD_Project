package pd_dirserv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class HeartBeatReceiverServer extends Thread{
    public static final int TIMEOUT = 31000;
  public int HB_PORT;
    public static final int MAX_SIZE = 256;
    DatagramSocket socket =null;
    Server server;
    

    public HeartBeatReceiverServer(Server server) {

        this.server = server;
        this.socket = server.getHbSocket();
    }
    
   
    
    
    @Override
    public void run(){
        byte[] buff = new byte[MAX_SIZE];
        String name;
        int port;
        InetAddress addr;
        DatagramPacket packetToReceive;
        DatagramPacket packetToSend;

        packetToReceive = new DatagramPacket(buff, MAX_SIZE);

        while(true){
        try{
            socket.setSoTimeout(TIMEOUT);
            System.out.println("Receiving Heartbeat...");
            
            socket.receive(packetToReceive);
            System.out.println("Heartbeat active!");

        }catch(SocketTimeoutException e){
            System.err.println("Heartbeat Timeout {"+ server.getName() +"}- " +e);
            Globals.getServerList().remove(server.getName());
            break;
        }catch(IOException e){
            System.out.println("Error receiving message : " + e);
        }
               
        }
        
        System.out.println("Heartbeat dead...");
        
        
    }
}
