package pd_dirserv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class PD_DirServ {
    public final static int PORT = 6000;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String ip = "192.168.1.116";
        Map<String,Server> serverList = new HashMap<>();
        
        String name;
        Thread sct;
        
        
        sct = new ServersConnectionThread(ip,PORT,serverList);
        sct.start();
        
    }
    
}
