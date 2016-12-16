/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_dirserv;

import java.net.DatagramSocket;

/**
 *
 * @author dvchava
 */
public class Client {
    DatagramSocket hbSocket=null; //Heart beat Socket
    String name;
    Thread thb;

    public Client(String name) {
        this.name = name;
    }

    public DatagramSocket getHbSocket() {
        return hbSocket;
    }

    public void setHbSocket(DatagramSocket hbSocket) {
        this.hbSocket = hbSocket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Thread getThb() {
        return thb;
    }

    public void setThb(Thread thb) {
        this.thb = thb;
    }
    
    
}
