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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class HeartBeatThread extends Thread{
    public static int MAX_SIZE = 256;
    InetAddress addr;
    DatagramSocket hbSocket;
    int PORT_HB;
    DatagramPacket packetsend;
    DatagramPacket packetreceive;
    String dirServIP;
    String hbMsg;
    ByteArrayInputStream byteStream = null;
    ObjectInputStream is = null;
    String [] serverlist;
    byte[] recvbuf = new byte[MAX_SIZE];
    
    public HeartBeatThread(String name, String dirServIP, int PORT_HB) {
        try {
            this.hbSocket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(HeartBeatThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.PORT_HB = PORT_HB;
        this.packetsend = null;
        this.packetreceive = null;
        this.addr = null;
        hbMsg = name + " " + Globals.getLogged();
        this.dirServIP = dirServIP;
    }
    
    public void packetInitialization(){
        byte[] sendbuf = hbMsg.getBytes();
       
        try {
            addr = InetAddress.getByName(dirServIP);
        } catch (UnknownHostException e) {
            System.err.println("Error - " + e);
        }
        packetsend = new DatagramPacket(sendbuf, sendbuf.length, addr, PORT_HB);
        packetreceive = new DatagramPacket(recvbuf, MAX_SIZE);
    }
    
    @Override
    public void run(){
        packetInitialization();
        while(true){
            try {
                Thread.sleep(10000);
                System.out.println("Sending HearBeat...");
                hbSocket.send(packetsend);
                System.out.println("HearBeat sent...");
                hbSocket.receive(packetreceive);
                System.out.println("Server List received...");
                byteStream = new ByteArrayInputStream(recvbuf);
                is = new ObjectInputStream(new BufferedInputStream(byteStream));

                serverlist = (String[]) is.readObject();
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

