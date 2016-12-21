

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author David
 */
public class DirServService extends UnicastRemoteObject implements DirServRemoteInterface{
    
     
    public DirServService() throws RemoteException{
        
    }
    
    @Override
    public String[] getServerListRemote() {
        String[] serverList = null;
        int i = 0;

        Iterator it = Globals.getServerList().entrySet().iterator();

        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();

            Server s = (Server) pair.getValue();
            serverList[i] = s.getName() + " " + s.getIp() + " " + s.getPort();

            i++;
        }

        return serverList;
    }
}
