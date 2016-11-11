/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_serv;

import java.net.DatagramSocket;
import java.net.SocketException;
/**
 *
 * @author David
 */
public class PD_Serv {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String name;
        int listeningPort=-1;
        String dirServIP;
        DatagramSocket hbSocket = null;
        int dirServPort;
        Thread tHb; // Thread heart beat
        
        
        int argc = args.length;
        if (argc != 3) {
            System.out.println("Error - Number of arguments...");
            return;
        }
        
        name = args[0];
        dirServIP = args[1];
        dirServPort = Integer.parseInt(args[2]);
        
        try {
            hbSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.err.println("Error creating the heart beat socket - " + e);
            return;
        }
        
        Server server = new Server(hbSocket, name, dirServIP, dirServPort);
        tHb = new HeartBeatThread(hbSocket, listeningPort, name, dirServPort, dirServIP);
        tHb.start();
        
    }
    
}
