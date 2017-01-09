

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
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dvchava
 */
public class HeartBeatThreadS extends Thread{
    public static int MAX_SIZE = 256;
    InetAddress addr;
    DatagramSocket hbSocket;
    int PORT_HB;
    DatagramPacket packet;
    String dirServIP;
    String hbMsg;

    public HeartBeatThreadS(int servListeningPort, String name, String dirServIP, int PORT_HB) {
        try {
            this.hbSocket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(HeartBeatThreadS.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.PORT_HB = PORT_HB;
        this.packet = null;
        this.addr = null;
        hbMsg = name + " " + servListeningPort;
        this.dirServIP = dirServIP;
    }
    
    public void packetInitialization(){
        byte[] sendbuf = hbMsg.getBytes();
        try {
            addr = InetAddress.getByName(dirServIP);
        } catch (UnknownHostException e) {
            System.err.println("Error - " + e);
        }
        packet = new DatagramPacket(sendbuf, sendbuf.length, addr, PORT_HB);
    }
    
    @Override
    public void run(){
        packetInitialization();
        while(true){
            try {
                Thread.sleep(20000);
                System.out.println("Sending HearBeat...");
                hbSocket.send(packet);
                System.out.println("HearBeat sended...");
            } catch (InterruptedException e) {
                System.err.println("Error in Heatbeat - " + e);
            }catch (IOException e){
                System.err.println("Error sending heart beat - " + e);
            }
        }
    }
}
