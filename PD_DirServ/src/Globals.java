/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dvchava
 */
public final class Globals {
    private static Map<String,Server> serverList = new HashMap<>();
    private static Map<String,Client> clientList = new HashMap<>();

    public static Map<String, Client> getClientList() {
        return clientList;
    }

    public static void setClientList(Map<String, Client> clientList) {
        Globals.clientList = clientList;
    }

    public static Map<String,Server> getServerList(){
        return serverList;
    }
    
    public static void setServerList( Map<String,Server> serverList ){
        Globals.serverList = serverList; 
    }
}
