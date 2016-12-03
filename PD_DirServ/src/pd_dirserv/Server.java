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
    DatagramSocket hbSocket=null; //Heart beat Socket
    String name;
    String ip;
    int port;
    Thread thb;

    public Server(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public Thread getThb() {
        return thb;
    }

    public void setThb(Thread thb) {
        this.thb = thb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHbSocket(DatagramSocket hbSocket) {
        this.hbSocket = hbSocket;
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
