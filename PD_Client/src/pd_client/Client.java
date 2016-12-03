/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_client;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Scanner;

/**
 *
 * @author dvchava
 */
public class Client {
    public static int MAX_SIZE = 5000;
    DatagramSocket connSocket;
    String dirServIP;
    int dirServPort;
    
    String username;

    public Client(DatagramSocket connSocket, String dirServIP, int dirServPort) {
        this.connSocket = connSocket;
        this.dirServIP = dirServIP;
        this.dirServPort = dirServPort;
    }

    public DatagramSocket getConnSocket() {
        return connSocket;
    }

    public void setConnSocket(DatagramSocket connSocket) {
        this.connSocket = connSocket;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String[] connect(){
        String myIp = getLocalIpAddress();
        Scanner scan = new Scanner(System.in);
        String[] serverList = null;
        
        DatagramPacket packetToSend = null;
        DatagramPacket packetToReceive = null;
        ByteArrayInputStream byteStream = null;
        ObjectInputStream is = null;
        InetAddress addr = null;
        
        
        System.out.println("Asking directory server to connect...");

        try {
            addr = InetAddress.getByName(dirServIP);
        } catch (UnknownHostException e) {
            System.err.println("Error - " + e);
        }
        System.out.print("Username: ");
        username = scan.nextLine();
        String msg = username + " " + myIp;
        byte[] sendBuffer = msg.getBytes();
        packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, addr, dirServPort);
        
        try {
            System.out.println("Sending username...");
            connSocket.send(packetToSend);
        } catch (IOException e) {
            System.err.println("Error sending the name and listening port... - " + e);
        }
        byte[] recvBuffer = new byte[MAX_SIZE];
        packetToReceive = new DatagramPacket(recvBuffer, MAX_SIZE);
        
        try {
            System.out.println("Receiving server List...");
            connSocket.receive(packetToReceive);
            System.out.println("Server List received...");
            byteStream = new ByteArrayInputStream(recvBuffer);
            is = new ObjectInputStream(new BufferedInputStream(byteStream));
            serverList = (String[])is.readObject();
            System.out.println("Server List received...");
            is.close();
        } catch (IOException e) {
            System.err.println("Error receiving the answer... - " + e);
        }catch (ClassNotFoundException e) {
            System.err.println("Error receiving the list... - " + e);
        }
        return serverList;
    }
    
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
