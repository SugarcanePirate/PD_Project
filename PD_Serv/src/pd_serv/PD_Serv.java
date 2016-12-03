package pd_serv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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
        int myTCP_PORT=6003;
        String myServerIP;
        String dirServIP;
        DatagramSocket connSocket = null;
        int dirServPort;
        Thread tHb; // Thread heart beat
        
        
        int argc = args.length;
        if (argc != 4) {
            System.out.println("Error - Number of arguments...");
            return;
        }
        
        name = args[0];
        myServerIP = args[1];
        dirServIP = args[2];
        dirServPort = Integer.parseInt(args[3]);
        
        try {
            connSocket = new DatagramSocket(6000);
        } catch (SocketException e) {
            System.err.println("Error creating the heart beat socket - " + e);
            return;
        }
        
        Server server = new Server(connSocket, myTCP_PORT, myServerIP, name, dirServIP, dirServPort);

        if(server.connect().equals("0"))
            return;
        
        server.setActive(true);
        tHb = new HeartBeatThread(myTCP_PORT, name, dirServIP, server.getPORT_HB());
        tHb.start();
        
    }
    
}
