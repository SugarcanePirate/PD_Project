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
    String ip;
    int logged;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getLogged() {
        return logged;
    }

    public void setLogged(int logged) {
        this.logged = logged;
    }
    Thread thb;

    public Client(String name, String ip) {
        this.ip = ip;
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
