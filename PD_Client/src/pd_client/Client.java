/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author dvchava
 */
public class Client implements ClientOperations{
    public static int MAX_SIZE = 5000;
    public static int TIMEOUT = 5;
    
    public static int CHUNCK_MAX_SIZE = 1000;
    DatagramSocket connSocket = null;
    HashMap<String, Server> remoteServers = null;
    Server localServer = null;
    String dirServIP;
    int dirServPort;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    int PORT_HB;
    
    String username;
    String pass;
    
    String[] serverList = null;
    

    
    public Client(String username, String dirServIP, int dirServPort) {
        this.username = username;
        this.dirServIP = dirServIP;
        this.dirServPort = dirServPort;
        this.remoteServers = new HashMap<>();
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
        boolean connected = true;
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
            System.out.println("Receiving confirmation");
            connSocket.receive(packetToReceive);
            byteStream = new ByteArrayInputStream(recvBuffer);
            is = new ObjectInputStream(new BufferedInputStream(byteStream));
            
            String answer = (String) is.readObject();
            answer = answer.trim();
             String[] answers = answer.split(" ");
            if(answers.length == 1)
                connected = false;
            else{
                PORT_HB = Integer.parseInt(answers[1]);
                new HeartBeatThread(username,dirServIP,PORT_HB).start();
                System.out.println("HB created...");
            }
            
            
            is.close();
            
   
        } catch (SocketException e) {
            System.err.println("Error creating the heart beat socket - " + e);
            connected = false;
        }catch (ClassNotFoundException e) {
            System.err.println("Error receiving the list... - " + e);
            connected = false;
        } catch (UnknownHostException e) {
            System.err.println("Error - " + e);
        }catch (IOException e) {
            System.err.println("Error sending/receiving the answer... - " + e);
            connected = false;
        }
        return connected;
    }
    
    @Override
    public boolean register(String pass, int server){
        Socket s = null;
        boolean registered = false;
        serverList = Globals.getServerList();
        if(serverList == null)
            return false;
        
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
            
            if(registered)
                remoteServers.put(serverData[0], new Server(serverData[0],s,oos,ois));
            
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
    public boolean login(String username, String password, String serverName){
        boolean logged = false;
        String msg = "LOG " + username + " " + password;
        
       
        try {
            if(!remoteServers.containsKey(serverName))
                return false;
            
            Server server = remoteServers.get(serverName);
            ObjectOutputStream auxOos = server.getOos();
            ObjectInputStream auxOis = server.getOis();
            auxOos.flush();
            auxOos.writeObject(msg);
            auxOos.flush();
            
            logged = (Boolean)auxOis.readObject();
            if(logged){
                Globals.setLogged(1);
                localServer = new Server(remoteServers.remove(serverName));
            }
            
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
            localServer.getOos().flush();
            localServer.getOos().writeObject(msg);
            localServer.getOos().flush();
            
            loggedOut = (Boolean)localServer.getOis().readObject();
            if(loggedOut){
                Globals.setLogged(0);
                remoteServers.put(localServer.getName(), new Server(localServer));
            }
            
            
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
            localServer.getOos().flush();
            localServer.getOos().writeObject(msg);
            localServer.getOos().flush();
            
            cnt = (String[])localServer.getOis().readObject();
            
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
            localServer.getOos().flush();
            localServer.getOos().writeObject(msg);
            localServer.getOos().flush();
            
            path = (String)localServer.getOis().readObject();
            
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
            localServer.getOos().flush();
            localServer.getOos().writeObject(msg);
            localServer.getOos().flush();
            
            created = (Boolean)localServer.getOis().readObject();
            
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
            localServer.getOos().flush();
            localServer.getOos().writeObject(msg);
            localServer.getOos().flush();
            
            changed = (Boolean)localServer.getOis().readObject();
            
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
            localServer.getOos().flush();
            localServer.getOos().writeObject(msg);
            localServer.getOos().flush();
            
            cnt = (String[])localServer.getOis().readObject();
            
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
            localServer.getOos().flush();
            localServer.getOos().writeObject(msg);
            localServer.getOos().flush();
            
            removed = (Boolean)localServer.getOis().readObject();
            
        } catch (IOException e) {
            System.out.println("Error - Writing command! " + e);
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Error - Reading server answer! " + e);
            return false;
        }       
         
         return removed;
    }
    
    @Override
    public boolean copyFile(String filePath, String server_origin, String server_destination){
        String msg = "FCPY1 " + filePath;
        File tempDir = new File("temp");
        String localFilePath = "";
        FileOutputStream localFileOutputStream = null;
        InputStream in = null;
        byte []fileChunck = new byte[CHUNCK_MAX_SIZE];
        int nbytes;  
        ObjectOutputStream out = null;
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        String fileName = "";
        
        String requestedCanonicalFilePath = null;
        FileInputStream requestedFileInputStream = null;
        

        
            
        try {
            
            if (!server_origin.equals(localServer.getName())) {
                if (remoteServers.containsKey(server_origin)) {
                    out = remoteServers.get(server_origin).getOos();
                    remoteServers.get(server_origin).getSocket().setSoTimeout(TIMEOUT*1000);
                    in = remoteServers.get(server_origin).getSocket().getInputStream();
                }
            } else if (server_origin.equals(localServer.getName())) {
                out = localServer.getOos();
                localServer.getSocket().setSoTimeout(TIMEOUT*1000);
                in = localServer.getSocket().getInputStream();
            } else {
                return false;
            }
            
            if(!tempDir.exists())
                tempDir.mkdir();
            
            if(!tempDir.isDirectory())
                return false;
                
            if(!tempDir.canWrite())
                return false;
            
            String[] filePathArray = filePath.trim().split(pattern);
            fileName = filePathArray[filePathArray.length-1];
            
            localFilePath = tempDir.getCanonicalPath()+File.separator+fileName;
            localFileOutputStream = new FileOutputStream(localFilePath);
            
            out.flush();
            out.writeObject(msg);
            out.flush();
            
            while((nbytes = in.read(fileChunck)) > 0){                    
                    localFileOutputStream.write(fileChunck, 0, nbytes);
//                   if(nbytes < CHUNCK_MAX_SIZE)
//                        break;
            } 
            
            if (!server_destination.equals(localServer.getName())) {
                if (remoteServers.containsKey(server_destination)) {
                    out = remoteServers.get(server_destination).getOos();
                    remoteServers.get(server_destination).getSocket().setSoTimeout(TIMEOUT*1000);
                }
            } else if (server_destination.equals(localServer.getName())) {
                out = localServer.getOos();
                localServer.getSocket().setSoTimeout(TIMEOUT*1000);
            } else {
                return false;
            }
            msg = "FCPY2 " + filePath;
            
                    requestedCanonicalFilePath = new File(tempDir.getCanonicalPath()+File.separator+fileName).getCanonicalPath();

                    if(!requestedCanonicalFilePath.startsWith(tempDir.getCanonicalPath()+File.separator)){
                        return false;
                    }
                    
                    requestedFileInputStream = new FileInputStream(requestedCanonicalFilePath);
                    
                    out.flush();
                    out.writeObject(msg);
                    out.flush();
                    
                    while((nbytes = requestedFileInputStream.read(fileChunck))>0){                        
                        
                        out.write(fileChunck, 0, nbytes);
                        out.flush();
                                                
                    }     
            
            }catch(SocketTimeoutException e){
                
            }catch(SocketException e){
                System.out.println("Error - TCP socket:\n\t"+e);
                return false;
            }catch(IOException e){
                System.out.println("Error - socket / local file:\n\t"+e);
                return false;
            }finally{
            
            if(localFileOutputStream != null){
                try{
                    localFileOutputStream.close();
                }catch(IOException e){}
            }
            if(requestedFileInputStream != null){
                    try {
                        requestedFileInputStream.close();
                    } catch (IOException ex) {}
                }
            
           
            try {
                File file;
                file = new File(tempDir.getCanonicalPath()+File.separator+fileName);
                Files.deleteIfExists(file.toPath());
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            
        }         
        
        return true;
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
