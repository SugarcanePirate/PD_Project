/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_dirserv;

import java.net.DatagramPacket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author David
 */
public class ThreadList extends Thread{
    
    Socket socket = null;
    DatagramPacket packet = null;
    
    public ThreadList(){
        this.socket = socket;
    }
    
    @Override
    public void run(){
        
         Map<String,Servers> m = new HashMap<>();
        
    }
}
