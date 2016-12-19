/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_client;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class HeartBeatThread extends Thread{
    public static int MAX_SIZE = 5000;
    InetAddress addr;
    DatagramSocket hbSocket;
    int PORT_HB;
    DatagramPacket packetsend;
    DatagramPacket packetreceive;
    String dirServIP;
    String hbMsg;
    String[]  serverlist = null;
    ByteArrayInputStream byteStream = null;
    ObjectInputStream is = null;
    
    byte[] recvbuf;
    
    
    public HeartBeatThread(String name, String dirServIP, int PORT_HB) {
        
        this.PORT_HB = PORT_HB;
        this.packetsend = null;
        this.packetreceive = null;
        this.addr = null;
        hbMsg = name + " " + Globals.getLogged();
        this.dirServIP = dirServIP;
    }
    
    public void packetInitialization(){
        
       
        try {
            addr = InetAddress.getByName(dirServIP);
        } catch (UnknownHostException e) {
            System.err.println("Error - " + e);
        }
        
        
    }
    
    @Override
    public void run(){
        packetInitialization();
        try {
            recvbuf = new byte[MAX_SIZE];
            this.hbSocket = new DatagramSocket(PORT_HB);
            
     
        } catch (SocketException ex) {
            Logger.getLogger(HeartBeatThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HeartBeatThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] sendbuf = hbMsg.getBytes();
        while(true){
            try {
                Thread.sleep(5000);
                recvbuf = new byte[MAX_SIZE];
                System.out.println("Sending HearBeat...");
                packetsend = new DatagramPacket(sendbuf, sendbuf.length, addr, PORT_HB);
                hbSocket.send(packetsend);
                System.out.println("HearBeat sent...");
               
                packetreceive = new DatagramPacket(recvbuf, MAX_SIZE);
                hbSocket.receive(packetreceive);
                byteStream = new ByteArrayInputStream(recvbuf);
            is = new ObjectInputStream(byteStream);
                System.out.println("Server List received...");
                
                
                
                serverlist = (String[]) is.readUnshared();
                Globals.setServerList(serverlist);
    
                
            } catch (InterruptedException e) {
                System.err.println("Error in Heatbeat - " + e);
            }catch (IOException e){
                System.err.println("Error sending heart beat - " + e);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HeartBeatThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

