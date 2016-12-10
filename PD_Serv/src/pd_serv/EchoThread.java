/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_serv;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author David
 */
public class EchoThread extends Thread {
    boolean logged;
    boolean loggedOut;
    protected Socket socket;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    ClientData client;
    Map <String,ClientData> clientdata;

    public EchoThread(Socket clientSocket,Map <String,ClientData> clientmap,ObjectOutputStream oos,ObjectInputStream ois, ClientData client) {
        this.socket = clientSocket;
        logged = false;
        this.oos=oos;
        this.ois=ois;
        this.client=client;
        this.clientdata = clientmap;
    }

    
    public String[] processCmd(String cmd){
        String[] commands;
        
        cmd = cmd.trim();
        
        commands = cmd.split(" ");
        
            commands[0] = commands[0].toUpperCase();
        
        return commands;
    }

   
     public boolean makeDir(String dirName) {
        String nomeDirectoria  = client.getCurrentDir()+File.separator + dirName;

        File directoria = new File(nomeDirectoria);

        if (!directoria.exists()) {
            try {
                directoria.mkdir();
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }
     
     public boolean changeDir(String dirName){
         String pattern = Pattern.quote(System.getProperty("file.separator"));
         String newDir="";
         
         if(dirName.equals("..")){
            String[] dir = client.getCurrentDir().split(pattern);
            
             client.setCurrentDir("");
             for(int i = 0; i<dir.length-1;i++)
                 newDir+=dir[i]+File.separator;
             
             
         }else{
             File directoria = new File(client.getCurrentDir()+File.separator+dirName);
             if (!directoria.exists()) {
                 return false;
            }
             
             String[] dir = client.getCurrentDir().split(pattern);
             
             client.setCurrentDir("");
             for(int i = 0; i<dir.length;i++)
                 newDir+=dir[i]+File.separator;
             
             newDir+=dirName;
         }
         client.setCurrentDir(newDir);
         
             return true;
         
     }
     
    public void run() {
       
       
        String[] cmd ;
        
//        try {
//            oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.flush();
//            ois = new ObjectInputStream(socket.getInputStream());
//            
//            
//        } catch (Exception e) {
//            System.out.println(""+e);
//            return;
//        }
        
        String line;
//        try{
        while (!Thread.currentThread().isInterrupted()) {
            try {
                line = (String)ois.readObject();
                cmd = processCmd(line);
                switch(cmd[0]){
                    case "LOG":
                        
                        if(cmd[2].equals(clientdata.get(cmd[1]).getPassword()) && !logged ){
                            logged = true;
                            loggedOut = false;
                            System.out.println("Client logged in");
                            oos.flush();
                            oos.writeObject(logged);
                            oos.flush();
                        }
                        else{
                            oos.flush();
                            oos.writeObject(logged);
                            oos.flush();
                            System.out.println("Client login failed");
                        }
                        
                        break;
                        
                    case "OUT":
                        if(logged){
                            loggedOut = true;
                            logged=false;
                            oos.flush();
                            oos.writeObject(loggedOut);
                            oos.flush();
                            System.out.println("Client logged off");
//                            throw new InterruptedException();
                        }
                        else{
                            System.out.println("Client isn't logged in");
                        }
                        break;
                         case "MKDIR":
                        if(!logged)
                            break;
                            oos.flush();
                            oos.writeObject(makeDir(cmd[1]));
                            oos.flush();
                            System.out.println("Dir created");
//                            throw new InterruptedException();
                        break;
                        case "CHDIR":
                        if(!logged)
                            break;
                            oos.flush();
                            oos.writeObject(changeDir(cmd[1]));
                            oos.flush();
                            System.out.println("Dir changed");
//                            throw new InterruptedException();
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                System.out.println("I/O error: "+e);
                return;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(EchoThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
//         }catch (InterruptedException e) {
//         cancel();
//     }
    }
}
