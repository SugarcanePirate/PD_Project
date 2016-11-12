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
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author David
 */
public class HeartBeatReceiver extends Thread{

    DatagramPacket packet;
    public static final int MAX_SIZE = 256;
    DatagramSocket socket;
    InetAddress addr;
    int dirListeningPort;
    

    public HeartBeatReceiver(DatagramPacket packet, DatagramSocket socket, InetAddress addr, int dirListeningPort) {
        this.packet = packet;
        this.socket = socket;
        this.addr = addr;
        this.dirListeningPort = dirListeningPort;
    }
    
   
    
    
    @Override
    public void run(){
        byte[] buff = null;
        String name;
        int port;
        
        packet = new DatagramPacket(buff, MAX_SIZE);
        
        while(true){
        try{
             socket.receive(packet);
        
        byte[] data = packet.getData();
        
        String hb = new String(data);
        
        Scanner scan = new Scanner(hb);

        }catch(IOException e){
            System.out.println("Error receiving message : " + e);
        }
       
        }
    }
}
