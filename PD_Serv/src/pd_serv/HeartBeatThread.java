package pd_serv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author dvchava
 */
public class HeartBeatThread extends Thread{
    public static int MAX_SIZE = 256;
    InetAddress addr;
    DatagramSocket hbSocket;
    DatagramPacket packet;
    int dirListeningPort;
    String dirServIP;
    String hbMsg;

    public HeartBeatThread(DatagramSocket hbSocket, int servListeningPort, String name, int dirListeningPort, String dirServIP) {
        this.hbSocket = hbSocket;
        this.packet = null;
        this.addr = null;
        hbMsg = name + " " + servListeningPort;
        this.dirListeningPort = dirListeningPort;
        this.dirServIP = dirServIP;
    }
    
    public void packetInitialization(){
        byte[] sendbuf = hbMsg.getBytes();
        try {
            addr = InetAddress.getByName(dirServIP);
        } catch (UnknownHostException e) {
            System.err.println("Error - " + e);
        }
        packet = new DatagramPacket(sendbuf, sendbuf.length, addr, dirListeningPort);
    }
    
    @Override
    public void run(){
        packetInitialization();
        while(true){
            try {
                Thread.sleep(30000);
                hbSocket.send(packet);
            } catch (InterruptedException e) {
                System.err.println("Error in Heatbeat - " + e);
            }catch (IOException e){
                System.err.println("Error sending heart beat - " + e);
            }
        }
    }
}
