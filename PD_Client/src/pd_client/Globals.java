/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_client;

/**
 *
 * @author David
 */
public final class Globals {
    private static String[] serverList;
    private static int logged = 0;

    public static String[] getServerList(){
        return serverList;
    }

    public static int getLogged() {
        return logged;
    }

    public static void setLogged(int logged) {
        Globals.logged = logged;
    }
    
    public static void setServerList( String[] serverList ){
        Globals.serverList = serverList; 
    }
}
