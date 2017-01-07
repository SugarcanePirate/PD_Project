
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author David
 */
public class PD_Monitor {
public static final String SERVICE_NAME = "DirServService";
    /**
     * @param args the command line arguments
     */

public final static void cls() {
        char c = '\n';
        int length = 35;
        char[] chars = new char[length];
        Arrays.fill(chars, c);
        System.out.print(String.valueOf(chars));
    }

    public static void printList(String[]  l){
        if(l == null || l.length == 0)
            return;
        
        for (String line : l) {
            System.out.println(line);
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        String objectUrl;
        String[] serverList;
        
        if(args.length != 1)
            return;
        
        objectUrl = args[0];
        String registration = "rmi://" + objectUrl + "/DirServService";
        try{
            
        Remote remote = Naming.lookup(registration);
        
        DirServRemoteInterface service = (DirServRemoteInterface) remote;
        
        while(true){
            cls();
            serverList = service.getServerListRemote();
            printList(serverList);
            Thread.sleep(1000);
            
        }
        } catch (NotBoundException ex) {
            System.out.println("Unknow remote service "+ex);
        }catch (RemoteException ex) {
            System.out.println("Remote error - " +ex);
         } catch (InterruptedException ex) {
            System.out.println("Sleep error - " +ex);
    }catch(Exception ex){
            System.out.println("Error "+ ex);
    }

    }
}
