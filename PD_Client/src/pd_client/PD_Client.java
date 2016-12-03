/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_client;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

/**
 *
 * @author David
 */
public class PD_Client {
    
    public final static void clearConsole() {
        char c = '\n';
        int length = 25;
        char[] chars = new char[length];
        Arrays.fill(chars, c);
        System.out.print(String.valueOf(chars));
    }
    
    public static void printServerList(String[] list){
        for(int i = 0; i < list.length; i++){
            System.out.println(list[i]);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String dirServIP;
        int dirServPort;
        DatagramSocket connSocket = null;
        
        dirServIP = args[0];
        dirServPort = Integer.parseInt(args[1]);
        
        try {
            connSocket = new DatagramSocket(dirServPort);
        } catch (SocketException e) {
            System.err.println("Error creating the heart beat socket - " + e);
            return;
        }
        
        Client client = new Client(connSocket, dirServIP, dirServPort);
        
        String[] serverList = client.connect();
        clearConsole();
        printServerList(serverList);
}
}