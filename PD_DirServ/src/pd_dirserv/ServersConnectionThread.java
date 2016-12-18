package pd_dirserv;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author David
 */
public class ServersConnectionThread extends Thread {

    public final static int PORT_UDP_CONN = 6000;
    public final static int PORT_UDP_HB = 6001;
    public static final int MAX_SIZE = 256;
    int servPort;
    String dirServIP;
    DatagramSocket socket = null;

    public ServersConnectionThread(String dirServIP, int servPort) {

        this.dirServIP = dirServIP;
        this.servPort = servPort;
        
    }

    @Override
    public void run() {
        String connected = "";
        byte[] buff = new byte[MAX_SIZE];
        
        DatagramPacket packetToSend = null;
        DatagramPacket packetToReceive;
        
        
        InetAddress addr = null;
        
         try {

                socket = new DatagramSocket(PORT_UDP_CONN);

            } catch (SocketException e) {
                System.err.println("Error - "+e);
            }

        while (true) {
            DatagramSocket hbSocket =  null;
           

            packetToReceive = new DatagramPacket(buff, MAX_SIZE);
            connected = "0";  // define conneçao para nome de servidor de volta a 0

            try {
                System.out.println("Waiting for servers");
                socket.receive(packetToReceive);
            } catch (IOException e) {
                System.out.println("Erro a receber a mensagem : " + e);
            }
            System.out.println("server found");

            byte[] data = packetToReceive.getData();  //recebe nome do servidor e os dados ip/porto

            String hb = new String(data);

            Scanner scan = new Scanner(hb);

            String name = scan.next();
            int port = scan.nextInt();
            String ip = new String();
            ip = scan.next();
            if (!Globals.getServerList().containsKey(name)) {
                try {
                hbSocket = new DatagramSocket(0);
                Globals.getServerList().put(name, new Server(name, ip, port, hbSocket));
                connected = 1 + " " + hbSocket.getLocalPort();
                } catch (SocketException ex) {
                    Logger.getLogger(ServersConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

            byte[] sendBuffer = connected.getBytes();
            try {
                addr = InetAddress.getByName(ip);
            } catch (UnknownHostException e) {
                System.err.println("Error - " + e);
            }

            System.out.println("Sending Packet");

            packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, addr, PORT_UDP_CONN);  // envia ao servidor uma string a dizer se ja existe o nome
            
            try {
                System.out.println("Sending confirmation");
                socket.send(packetToSend);
            } catch (IOException e) {
                System.err.println("Error sending the name and listening port... - " + e);
            }
            new HeartBeatReceiverServer(Globals.getServerList().get(name)).start();
//        msg = new String(packetToReceive.getData(), 0, packetToReceive.getLength());
//        
//        System.out.println("Confirming connection to: " + msg);
//        
        }
    }

}
