/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dvchava
 */
public class HeartBeatReceiverClient  extends Thread{
    public static final int TIMEOUT = 11000;
    public int PORT_HB;
    public static final int MAX_SIZE = 5000;
    DatagramSocket hbSocket = null;
    Client client;
    ObjectOutputStream oos = null;
    ByteArrayOutputStream byteArray = null;
    DatagramPacket packetToSend = null;
    DatagramPacket packetToReceive = null;
    InetAddress addr = null;
    byte[] sendBuffer;
    byte[] recvBuffer;
    

    public HeartBeatReceiverClient(Client client, int PORT_HB, DatagramSocket hbSocket) {
        this.client = client;
        this.PORT_HB = PORT_HB;
        this.hbSocket = hbSocket;
    }
    
   public void updateClientInfo(String name, int logged){
       Globals.getClientList().get(name).setLogged(logged);
   }
   
    public void packetInitialization() throws IOException {
        
        

//        oos.flush();
//        oos.writeObject(getServerList());
//        oos.flush();
        recvBuffer = new byte[MAX_SIZE];
//        sendBuffer = byteArray.toByteArray();
//        packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, addr, PORT_HB);
        packetToReceive = new DatagramPacket(recvBuffer, MAX_SIZE);
    }
    
    public String[] getServerList(){
     int i=0;
    String[] list = null;
    if(Globals.getServerList().size() > 0){
    list = new String[Globals.getServerList().size()];
    for(String key: Globals.getServerList().keySet()){
        Server s = Globals.getServerList().get(key);
        list[i] = /*" " +*/ s.getName() + " " + s.getIp() + " " + s.getPort();
        i++;
    }
    }else{
        list = new String[1] ;
        list[0] = "No servers...";
    }
    return list;
}
    
    
    @Override
    public void run(){
        
         
        try {
            
            
            
            client.setHbSocket(hbSocket);
//            byteArray = new ByteArrayOutputStream(MAX_SIZE);
//            oos = new ObjectOutputStream(new BufferedOutputStream(byteArray));
            addr = InetAddress.getByName(client.getIp());
        } catch (IOException ex) {
            Logger.getLogger(HeartBeatReceiverClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        while(true){
        try{
            packetInitialization();
            hbSocket.setSoTimeout(TIMEOUT);
            System.out.println("Receiving Heartbeat...");
            
            hbSocket.receive(packetToReceive);
            byte[] data = packetToReceive.getData();

                System.out.println("Reading client data");

                String hbMsg = new String(data);
                hbMsg = hbMsg.trim();

                String[] answers = hbMsg.split(" ");
                
                String name = answers[0];
                int logged = Integer.parseInt(answers[1]);
                
                updateClientInfo(name,logged);
                byteArray = new ByteArrayOutputStream(MAX_SIZE);
                oos = new ObjectOutputStream(byteArray);
                oos.reset();
                oos.flush();
                oos.writeUnshared(getServerList());
                oos.flush();
                System.out.println("sending HB...");
                sendBuffer = byteArray.toByteArray();
                //InetAddress addr = InetAddress.getByName(client.getIp());
               // packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, addr, PORT_HB);
                packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, addr, PORT_HB);
                
                hbSocket.send(packetToSend);
                
                System.out.println("HB sent...");
                
            System.out.println("Heartbeat active!");

        }catch(SocketTimeoutException e){
            try {
                System.err.println("Heartbeat Timeout {"+ client.getName() +"}- " +e);
                oos.close();
                hbSocket.close();
                Globals.getClientList().remove(client.getName());
                
                break;
            } catch (IOException ex) {
                System.out.println("Error closing client " + ex);
            }
        }catch(IOException e){
            System.out.println("Error receiving message : " + e);
        }
               
        }
        
        System.out.println("Heartbeat dead...");
        
        
    }
}
