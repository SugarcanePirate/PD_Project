/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author David
 */
public class GlobalsC {
    private static String[] serverList;
    private static int logged = 0;

    public static String[]  getServerList(){
        return serverList;
    }

    public static int getLogged() {
        return logged;
    }

    public static void setLogged(int logged) {
        GlobalsC.logged = logged;
    }
    
    public static void setServerList( String[]  serverList ){
        GlobalsC.serverList = serverList; 
    }
}
