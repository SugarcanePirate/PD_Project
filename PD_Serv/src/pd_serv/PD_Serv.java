package pd_serv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author David
 */
public class PD_Serv {
    
    public final static void clearConsole() {
        char c = '\n';
        int length = 25;
        char[] chars = new char[length];
        Arrays.fill(chars, c);
        System.out.print(String.valueOf(chars));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String name;
        int myTCP_PORT=6003;
        String myServerIP;
        String dirServIP;
        Map <String,ClientData> client = new HashMap<>();
        DatagramSocket connSocket = null;
        int dirServPort;
        Thread tHb; // Thread heart beat
        Thread ct; // Thread clientes
        
        
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
            connSocket = new DatagramSocket(dirServPort);
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
        ct = new ClientThreadEchoServer(client);
        ct.start();
        
    }
    
}
