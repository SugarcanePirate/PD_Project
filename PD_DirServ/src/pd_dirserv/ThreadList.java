/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_dirserv;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author David
 */
public class ThreadList extends Thread{

    DatagramPacket packet = null;
    public static final int MAX_SIZE = 256;
    Socket socket = null;
    InetAddress addr;
    int dirListeningPort;
    byte[] buff;
    
    public ThreadList(){
        this.socket = socket;
        this.packet = null;
        this.addr = null;
        this.dirListeningPort = dirListeningPort;
    }
    
    
    @Override
    public void run(){
     
        
        packet = new DatagramPacket(buff, MAX_SIZE, addr, dirListeningPort);
        
        while(true){
        try{
            
        }
        socket.receive(packet);
        
        byte[] data = packet.getData();
        String hb = new String(data);
        
        Scanner scan = new Scanner(hb);
        
        
        }catch(IOException e){
            System.out.println("Erro a receber a mensagem : " + e);
        }
        msg = new String(packet.getData(), 0, packet.getLength());
        
        System.out.println("Connectado a: " + msg);
        
    }
}
