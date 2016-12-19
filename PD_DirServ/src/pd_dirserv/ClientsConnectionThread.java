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
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author David
 */
public class ClientsConnectionThread extends Thread{
    public final static int PORT_UDP_CONN = 6002;
    public int PORT_HB;
    public static final int MAX_SIZE = 5000;
    int myPort;
    DatagramSocket socketToClient =null;
    Client client;
    
    String myIp;

    public ClientsConnectionThread(String myIp, int myPort) {
        this.myIp = myIp;
        this.myPort = myPort;
    }
    
   
    
     @Override
    public void run() {
        byte[] buff = new byte[MAX_SIZE];
        String name, clientIp;
        String connected = "";
        DatagramPacket packetToReceive;
        DatagramPacket packetToSend;
        InetAddress addr = null;
        ByteArrayOutputStream byteArray = null;
        ObjectOutputStream os = null;
        byte[] sendBuffer = null;

        packetToReceive = new DatagramPacket(buff, MAX_SIZE);
        try {
            socketToClient = new DatagramSocket(myPort);
        } catch (SocketException ex) {
            Logger.getLogger(ClientsConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            DatagramSocket hbSocket =  null;
            try {
                

                System.out.println("Waiting for clients");

                socketToClient.receive(packetToReceive);
                System.out.println("Client arrived");

                byte[] data = packetToReceive.getData();  //recebe nome do servidor e os dados ip/porto

                System.out.println("Reading client data");

                String username = new String(data);

                Scanner scan = new Scanner(username);

                name = scan.next();
                clientIp = scan.next();

                addr = InetAddress.getByName(clientIp);

                byteArray = new ByteArrayOutputStream(MAX_SIZE);
                os = new ObjectOutputStream(new BufferedOutputStream(byteArray));

                if (!Globals.getClientList().containsKey(name)) {     
                    
                    hbSocket = new DatagramSocket(0);
                    PORT_HB  = hbSocket.getLocalPort();
                    connected = 1 + " " + PORT_HB;
                    
                    os.flush();
                    os.writeObject(connected);
                    os.flush();
                    sendBuffer = byteArray.toByteArray();
                    packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, addr, PORT_UDP_CONN); 
                    socketToClient.send(packetToSend);
                    System.out.println("ENTROU : " + name);
                    client = new Client(name, clientIp, hbSocket);
                    client.thb = new HeartBeatReceiverClient(client,PORT_HB, hbSocket);
                    client.thb.start();                    
                    Globals.getClientList().put(name,client);
                } else {
                    System.out.println("JA EXISTE : " + name);
                    connected = "0";
                    os.flush();
                    os.writeObject(connected);
                    os.flush();
                    sendBuffer = byteArray.toByteArray();
                    packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, addr, PORT_UDP_CONN);  
                    socketToClient.send(packetToSend);
                    os.close();
                    System.out.println("Answer sent...");
                }

            } catch (UnknownHostException e) {
                System.err.println("Error - " + e);
            } catch (IOException e) {
                System.out.println("Error receiving message : " + e);
            }

        }

    }
    
 public String[] getServerList(){
     int i=0;
     String[] list = null;
    if(Globals.getServerList().size() > 0){
    list = new String[Globals.getServerList().size()];
    for(String key: Globals.getServerList().keySet()){
        Server s = Globals.getServerList().get(key);
        list[i] = " " + s.getName() + " " + s.getIp() + " " + s.getPort();
        i++;
    }
    }else{
        list = new String[1] ;
        list[0] = "No servers...";
    }
    return list;
}
}


