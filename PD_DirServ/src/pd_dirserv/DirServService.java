package pd_dirserv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import static com.sun.prism.impl.PrismSettings.debug;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import static pd_dirserv.HeartBeatReceiverClient.MAX_SIZE;

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
