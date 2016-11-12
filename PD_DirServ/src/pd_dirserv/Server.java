package pd_dirserv;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.DatagramSocket;

/**
 *
 * @author dvchava
 */
public class Server {
    DatagramSocket hbSocket; //Heart beat Socket
    String name;
    String ip;
    int port;

    public Server(DatagramSocket hbSocket, String name, String ip, int port) {
        this.hbSocket = hbSocket;
        this.name = name;
        this.ip = ip;
        this.port = port;
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
        return ip;
    }

    public void setDirServIP(String dirServIP) {
        this.ip = dirServIP;
    }

    public int getDirServPort() {
        return port;
    }

    public void setDirServPort(int dirServPort) {
        this.port = dirServPort;
    }
    
    
}
