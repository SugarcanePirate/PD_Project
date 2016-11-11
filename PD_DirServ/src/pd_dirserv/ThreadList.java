/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_dirserv;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author David
 */
public class ThreadList extends Thread{

    DatagramPacket packet = null;
    public static final int MAX_SIZE = 256;
    Socket socket = null;

    
    public ThreadList(){
        this.socket = socket;
    }
    
    
    @Override
    public void run(){
        String msg;
        
            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
        while(true){
        try{
        socket.receive(packet);
        }catch(IOException e){
            System.out.println("Erro a receber a mensagem : " + e);
        }
        msg = new String(packet.getData(), 0, packet.getLength());
        
        System.out.println("Connectado a: " + msg);
        
    }
}
