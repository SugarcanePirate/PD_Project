package pd_dirserv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author David
 */
public class PD_DirServ {
    public final static int PORT_Conn_Serv = 6000;
     public final static int PORT_Conn_Client = 6002;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String ip = "192.168.1.116";
        Map<String,Server> serverList = new HashMap<>();
        
        String name;
        Thread serverconnection;
        Thread clientconnection;
        
        
        serverconnection = new ServersConnectionThread(ip,PORT_Conn_Serv,serverList);
        serverconnection.start();
        clientconnection = new ClientsConnectionThread(ip,PORT_Conn_Client,serverList);
        clientconnection.start();
    }
    
}
