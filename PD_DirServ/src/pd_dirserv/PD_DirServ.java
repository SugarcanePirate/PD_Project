package pd_dirserv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class PD_DirServ {
    public final static int PORT_Conn_Serv = 6000;
    public final static int PORT_Conn_Client = 6002;
    public static final String SERVICE_NAME = "DirServService";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String ip = "192.168.1.116";
        
        Thread serverconnection;
        Thread clientconnection;
        
        
        serverconnection = new ServersConnectionThread(ip,PORT_Conn_Serv);
        serverconnection.start();
        clientconnection = new ClientsConnectionThread(ip,PORT_Conn_Client);
        clientconnection.start();
        
        try{
            
            Registry r;
            
            try{
                
                r = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
                  
            }catch(RemoteException e){
                r = LocateRegistry.getRegistry();
            }            

            /*
             * Cria o servico
             */            
            DirServService service = new DirServService();
            
            System.out.println("Service created ... ("+service.getRef().remoteToString()+"...");
            
            /*
             * Regista o servico no rmiregistry local para que os clientes possam localiza'-lo, ou seja,
             * obter a sua referencia remota (endereco IP, porto de escuta, etc.).
             */
            
            r.bind(SERVICE_NAME, service);
                   
            System.out.println("Service " + SERVICE_NAME + " registered...");
            
            /*
             * Para terminar um servico RMI do tipo UnicastRemoteObject:
             * 
             * 
             */
//             UnicastRemoteObject.unexportObject(service, true);
            
        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        }
    }
    
}
