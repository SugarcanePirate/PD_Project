/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_serv;

import java.net.DatagramSocket;

/**
 *
 * @author dvchava
 */
public class Server {
    DatagramSocket hbSocket; //Heart beat Socket
    String name;
    String dirServIP;
    int dirServPort;

    public Server(DatagramSocket hbSocket, String name, String dirServIP, int dirServPort) {
        this.hbSocket = hbSocket;
        this.name = name;
        this.dirServIP = dirServIP;
        this.dirServPort = dirServPort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DatagramSocket getHbSocket() {
        return hbSocket;
    }

    public String getDirServIP() {
        return dirServIP;
    }

    public void setDirServIP(String dirServIP) {
        this.dirServIP = dirServIP;
    }

    public int getDirServPort() {
        return dirServPort;
    }

    public void setDirServPort(int dirServPort) {
        this.dirServPort = dirServPort;
    }
    
    
}
