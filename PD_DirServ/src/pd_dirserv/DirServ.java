/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_dirserv;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author David
 */
public class DirServ {
    
    public static final int MAX_SIZE = 256;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private boolean debug;
     
    public DirServ(int listeningPort) throws SocketException{
        socket = null;
        packet = null;
        socket = new DatagramSocket(listeningPort);
    }
    
    public String WaitForDatagram () throws IOException{
        String request;
        
        if(socket == null){
            return null;
        }
        
        packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
        socket.receive(packet);
        request = new String(packet.getData(), 0, packet.getLength());
        
        if(debug){
            System.out.println("Received \"" + request + "\" from " + 
                    packet.getAddress().getHostAddress() + ":" + packet.getPort());
        }
        
        return request;
    }
    
    
}
