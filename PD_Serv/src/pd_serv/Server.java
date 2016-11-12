package pd_serv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dvchava
 */
public class Server {
    public static int MAX_SIZE = 256;
    DatagramSocket hbSocket; //Heart beat Socket
    String name;
    String serverIP;
    int servListeningPort;
    String dirServIP;
    int dirServPort;

    public Server(DatagramSocket hbSocket, int servListeningPort, String serverIP, String name, String dirServIP, int dirServPort) {
        this.hbSocket = hbSocket;
        this.name = name;
        this.serverIP = serverIP;
        this.dirServIP = dirServIP;
        this.dirServPort = dirServPort;
        this.servListeningPort = servListeningPort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServListeningPort() {
        return servListeningPort;
    }

    public void setServListeningPort(int servListeningPort) {
        this.servListeningPort = servListeningPort;
    }

    public DatagramSocket getHbSocket() {
        return hbSocket;
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

    public String connect() {
        DatagramPacket packetToSend = null;
        DatagramPacket packetToReceive = null;
        
        InetAddress addr = null;

        // Asking to connect
        String msg = name + " " + servListeningPort + " " + serverIP;

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
            hbSocket.send(packetToSend);
        } catch (IOException e) {
            System.err.println("Error sending the name and listening port... - " + e);
        }

        // Receiving answer
        byte[] recvBuffer = new byte[MAX_SIZE];
        packetToReceive = new DatagramPacket(recvBuffer, MAX_SIZE);
        try {
            System.out.println("Receiving answer...");
            hbSocket.receive(packetToReceive);
        } catch (IOException e) {
            System.err.println("Error receiving answer... - " + e);
        }
        String nameExists = new String(packetToReceive.getData());
        System.out.println("Answer: " + nameExists);
        return nameExists;
    }
}
