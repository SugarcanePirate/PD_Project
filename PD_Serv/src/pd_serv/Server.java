package pd_serv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author dvchava
 */
public class Server {
    public static int MAX_SIZE = 5000;
    DatagramSocket connSocket; //Heart beat Socket
    String name;
    String serverIP;
    int myTCP_PORT;
    int PORT_HB;
    String dirServIP;
    int dirServPort;
    boolean active = false;

    public Server(DatagramSocket connSocket, int myTCP_PORT, String serverIP, String name, String dirServIP, int dirServPort) {
        this.connSocket = connSocket;
        this.name = name;
        this.serverIP = serverIP;
        this.dirServIP = dirServIP;
        this.dirServPort = dirServPort;
        this.myTCP_PORT = myTCP_PORT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMyTCP_PORT() {
        return myTCP_PORT;
    }

    public void setMyTCP_PORT(int myTCP_PORT) {
        this.myTCP_PORT = myTCP_PORT;
    }

    public DatagramSocket getConnSocket() {
        return connSocket;
    }

    public String getDirServIP() {
        return dirServIP;
    }

    public void setDirServIP(String dirServIP) {
        this.dirServIP = dirServIP;
    }

    public int getDirServPort() {
        return dirServPort;
    }

    public void setDirServPort(int dirServPort) {
        this.dirServPort = dirServPort;
    }

    public int getPORT_HB() {
        return PORT_HB;
    }

    public void setPORT_HB(int PORT_HB) {
        this.PORT_HB = PORT_HB;
    }
    
    public int makeHomeDir() {
        String nomeDirectoria = System.getProperty("user.dir");

        nomeDirectoria = nomeDirectoria + File.separator + name;

        File directoria = new File(nomeDirectoria);

        if (!directoria.exists()) {
            try {
                directoria.mkdir();
            } catch (Exception e) {
                return -1;
            }
        } else {
            return 0;
        }

        return 1;
    }
    

    public String connect() {
        DatagramPacket packetToSend = null;
        DatagramPacket packetToReceive = null;
        
        InetAddress addr = null;

        // Asking to connect
        String msg = name + " " + myTCP_PORT + " " + serverIP;

        System.out.println("Asking directory server to connect...");

        byte[] sendBuffer = msg.getBytes();
        try {
            addr = InetAddress.getByName(dirServIP);
        } catch (UnknownHostException e) {
            System.err.println("Error - " + e);
        }

        packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, addr, dirServPort);

        try {
            System.out.println("Sending name and listening port...");
            connSocket.send(packetToSend);
        } catch (IOException e) {
            System.err.println("Error sending the name and listening port... - " + e);
        }

        // Receiving answer
        byte[] recvBuffer = new byte[MAX_SIZE];
        packetToReceive = new DatagramPacket(recvBuffer, MAX_SIZE);
        try {
            System.out.println("Receiving answer...");
            connSocket.receive(packetToReceive);
        } catch (IOException e) {
            System.err.println("Error receiving answer... - " + e);
        }
        
        String answer = new String(packetToReceive.getData());
        answer = answer.trim();
        String[] answers = answer.split(" ");
        String connected = answers[0];
        PORT_HB = Integer.parseInt(answers[1]);
        System.out.println("Answer: " + answer);
        if(connected.equals("1"))
            makeHomeDir();
            
        
        return connected;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
