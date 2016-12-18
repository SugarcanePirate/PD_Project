/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_dirserv;

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
 * @author dvchava
 */
public class HeartBeatReceiverClient  extends Thread{
    public static final int TIMEOUT = 11000;
  public final static int HB_PORT = 6003;
    public static final int MAX_SIZE = 256;
    DatagramSocket socket = null;
    Client client;
    

    public HeartBeatReceiverClient(Client client) {
        this.client = client;
    }
    
   
    
    
    @Override
    public void run(){
        byte[] buff = new byte[MAX_SIZE];
        String name;
        int port;
        InetAddress addr;
        DatagramPacket packetToReceive;
        DatagramPacket packetToSend;
        
         
        try {
            socket = new DatagramSocket(HB_PORT);
            client.setHbSocket(socket);
        } catch (SocketException ex) {
            Logger.getLogger(HeartBeatReceiverServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        packetToReceive = new DatagramPacket(buff, MAX_SIZE);
        try {
            socket.setSoTimeout(TIMEOUT);
        } catch (SocketException ex) {
            Logger.getLogger(HeartBeatReceiverServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(true){
        try{
            System.out.println("Receiving Heartbeat...");
            
            socket.receive(packetToReceive);
            System.out.println("Heartbeat active!");

        }catch(SocketTimeoutException e){
            System.err.println("Heartbeat Timeout - " +e);
            
            break;
        }catch(IOException e){
            System.out.println("Error receiving message : " + e);
        }
               
        }
        
        System.out.println("Heartbeat dead...");
        
        
    }
}
