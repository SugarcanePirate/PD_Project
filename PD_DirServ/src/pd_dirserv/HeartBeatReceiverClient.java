/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_dirserv;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static pd_dirserv.ClientsConnectionThread.MAX_SIZE;
import static pd_dirserv.ClientsConnectionThread.PORT_HB;
import static pd_dirserv.ClientsConnectionThread.PORT_UDP_CONN;

/**
 *
 * @author dvchava
 */
public class HeartBeatReceiverClient  extends Thread{
    public static final int TIMEOUT = 11000;
    public int PORT_HB;
    public static final int MAX_SIZE = 5000;
    DatagramSocket socket = null;
    Client client;
    ObjectOutputStream oos = null;
    ByteArrayOutputStream byteArray = null;
    DatagramPacket packetToSend = null;
    DatagramPacket packetToReceive = null;
    

    public HeartBeatReceiverClient(Client client, int PORT_HB) {
        this.client = client;
        this.PORT_HB = PORT_HB;
    }
    
   public void updateClientInfo(String name, int logged){
       Globals.getClientList().get(name).setLogged(logged);
   }
   
    public void packetInitialization() throws IOException {
        byteArray = new ByteArrayOutputStream(MAX_SIZE);
        oos = new ObjectOutputStream(new BufferedOutputStream(byteArray));

        oos.flush();
        oos.writeObject(Globals.getServerList());
        oos.flush();
        byte[] recvBuffer = new byte[MAX_SIZE];
        byte[] sendBuffer = byteArray.toByteArray();
        InetAddress addr = InetAddress.getByName(client.getIp());
        packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, addr, PORT_HB);
        packetToReceive = new DatagramPacket(recvBuffer, MAX_SIZE);
    }
    
    
    @Override
    public void run(){
         
        try {
            socket = new DatagramSocket(PORT_HB);
            client.setHbSocket(socket);
        } catch (SocketException ex) {
            Logger.getLogger(HeartBeatReceiverServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try {
            socket.setSoTimeout(TIMEOUT);
        } catch (SocketException ex) {
            Logger.getLogger(HeartBeatReceiverServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(true){
        try{
            System.out.println("Receiving Heartbeat...");
            
            socket.receive(packetToReceive);
            byte[] data = packetToReceive.getData();  //recebe nome do servidor e os dados ip/porto

                System.out.println("Reading client data");

                String hbMsg = new String(data);

                Scanner scan = new Scanner(hbMsg);
                
                String name = scan.nextLine();
                int logged = scan.nextInt();
                
                updateClientInfo(name,logged);
                
            System.out.println("Heartbeat active!");

        }catch(SocketTimeoutException e){
            System.err.println("Heartbeat Timeout - " +e);
            
            break;
        }catch(IOException e){
            System.out.println("Error receiving message : " + e);
        }
               
        }
        
        System.out.println("Heartbeat dead...");
        
        
    }
}
