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
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dvchava
 */
public class Client implements ClientOperations{
    public static int MAX_SIZE = 5000;
    DatagramSocket connSocket = null;
    ArrayList<Socket> serverSockets = null;
    String dirServIP;
    int dirServPort;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    
    String username;
    String pass;
    
    String[] serverList = null;
    

    
    public Client(String username, String dirServIP, int dirServPort) {
        this.username = username;
        this.dirServIP = dirServIP;
        this.dirServPort = dirServPort;
        this.serverSockets = new ArrayList<>();
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
    
    public String[] getServerList(){ return serverList; }
    
    @Override
    public boolean connect(){
        boolean connected = false;
        String myIp = getLocalIpAddress();
        
        DatagramPacket packetToSend = null;
        DatagramPacket packetToReceive = null;
        ByteArrayInputStream byteStream = null;
        ObjectInputStream is = null;
        InetAddress addr = null;
        
        try {
            connSocket = new DatagramSocket(dirServPort);

            System.out.println("Asking directory server to connect...");

            addr = InetAddress.getByName(dirServIP);

            String msg = username + " " + myIp;
            
            byte[] sendBuffer = msg.getBytes();
            packetToSend = new DatagramPacket(sendBuffer, sendBuffer.length, addr, dirServPort);
            System.out.println("Sending username...");
            connSocket.send(packetToSend);

            byte[] recvBuffer = new byte[MAX_SIZE];
            packetToReceive = new DatagramPacket(recvBuffer, MAX_SIZE);
            System.out.println("Receiving server List...");
            connSocket.receive(packetToReceive);
            System.out.println("Server List received...");
            byteStream = new ByteArrayInputStream(recvBuffer);
            is = new ObjectInputStream(new BufferedInputStream(byteStream));
            
            serverList = (String[]) is.readObject();
            if(serverList.length != 0)
                connected = true;
            
            System.out.println("Server List received...");
            is.close();
            
        } catch (SocketException e) {
            System.err.println("Error creating the heart beat socket - " + e);
        }catch (ClassNotFoundException e) {
            System.err.println("Error receiving the list... - " + e);
        } catch (UnknownHostException e) {
            System.err.println("Error - " + e);
        }catch (IOException e) {
            System.err.println("Error sending/receiving the answer... - " + e);
        }
        return connected;
    }
    
    @Override
    public boolean register(String pass, int server){
        Socket s = null;
        boolean registered = false;
        
        String serverLine = serverList[server-1];
        serverLine = serverLine.trim();
        String[] serverData = serverLine.split(" ");
        
        String clientData = username + " " + pass;
        String serverIp = serverData[1];
        int serverPort = Integer.parseInt(serverData[2]);
        
        try {
            s = new Socket(serverIp, serverPort);
            oos = new ObjectOutputStream( s.getOutputStream() );
            ois = new ObjectInputStream( s.getInputStream() );
            
            oos.flush();
            oos.writeObject(clientData);
            oos.flush();
            
            registered = (Boolean)ois.readObject();
            
        } catch (IOException ex) {
            System.out.println("Error - Connecting to socket (Server: '" + serverIp + "').");
            registered = false;
        } catch (ClassNotFoundException ex) {
            System.out.println("Error - reading answer (Server: '" + serverIp + "').");
            registered = false;
        }
        
        return registered;
    }
    
    @Override
    public boolean login(String username, String password){
        boolean logged = false;
        String msg = "LOG " + username + " " + password;
        
       
        try {
            oos.flush();
            oos.writeObject(msg);
            oos.flush();
            
            logged = (Boolean)ois.readObject();
            
        } catch (IOException e) {
            System.out.println("Error - Writing login data! " + e);
            logged = false;
        } catch (ClassNotFoundException e) {
            System.out.println("Error - Reading server answer! " + e);
            logged = false;
        }
        
        
        return logged;
    }
    
    @Override
    public boolean logout(){
        boolean loggedOut = false;
        
        String msg = "OUT " + username;
        
       
        try {
            oos.flush();
            oos.writeObject(msg);
            oos.flush();
            
            loggedOut = (Boolean)ois.readObject();
            
        } catch (IOException e) {
            System.out.println("Error - Writing command! " + e);
            loggedOut = false;
        } catch (ClassNotFoundException e) {
            System.out.println("Error - Reading server answer! " + e);
            loggedOut = false;
        }
        
        return loggedOut;
    }
    
    @Override
    public String[] getDirContent(){
        String msg = "DIRCNT";
        String[] cnt = null;
       
        try {
            oos.flush();
            oos.writeObject(msg);
            oos.flush();
            
            cnt = (String[])ois.readObject();
            
        } catch (IOException e) {
            System.out.println("Error - Writing command! " + e);
            cnt = null;
        } catch (ClassNotFoundException e) {
            System.out.println("Error - Reading server answer! " + e);
            cnt = null;
        }
        return cnt;
    }
    
    @Override
    public String getDirPath(){
        String msg = "DIRPTH";
        String path = null;
       
        try {
            oos.flush();
            oos.writeObject(msg);
            oos.flush();
            
            path = (String)ois.readObject();
            
        } catch (IOException e) {
            System.out.println("Error - Writing command! " + e);
            path = null;
        } catch (ClassNotFoundException e) {
            System.out.println("Error - Reading server answer! " + e);
            path = null;
        }
        return path;
    }
    
    @Override
    public boolean makeDir(String dirName){
        String msg = "MKDIR" + " " + dirName;
        boolean created = false;
       
        try {
            oos.flush();
            oos.writeObject(msg);
            oos.flush();
            
            created = (Boolean)ois.readObject();
            
        } catch (IOException e) {
            System.out.println("Error - Writing command! " + e);
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error - Reading server answer! " + e);
            
        }
        return created;
    }
    
    @Override
    public boolean changeDir(String dirName){
        String msg = "CHDIR" + " " + dirName;
        boolean changed = false;
       
        try {
            oos.flush();
            oos.writeObject(msg);
            oos.flush();
            
            changed = (Boolean)ois.readObject();
            
        } catch (IOException e) {
            System.out.println("Error - Writing command! " + e);
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error - Reading server answer! " + e);
            
        }
        return changed;
    }
    
    @Override
    public String[] getFileContent(String fileName){
        String[] cnt = null;
        
        String msg = "FCNT" + " " + fileName;
        
        try {
            oos.flush();
            oos.writeObject(msg);
            oos.flush();
            
            cnt = (String[])ois.readObject();
            
        } catch (IOException e) {
            System.out.println("Error - Writing command! " + e);
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error - Reading server answer! " + e);
            
        }
        
        return cnt;
    }
    
    @Override
    public boolean removeFile(String fileName){
        String msg = "FRMV " + fileName;
        boolean removed = false;
         try {
            oos.flush();
            oos.writeObject(msg);
            oos.flush();
            
            removed = (Boolean)ois.readObject();
            
        } catch (IOException e) {
            System.out.println("Error - Writing command! " + e);
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Error - Reading server answer! " + e);
            return false;
        }       
         
         return removed;
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
