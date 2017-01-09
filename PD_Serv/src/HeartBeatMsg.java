

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

/**
 *
 * @author dvchava
 */
public class HeartBeatMsg implements Serializable
{	
    static final long serialVersionUID = 1010L;
    String name;
    int listeningPort;

    public HeartBeatMsg(String name, int listeningPort) {
        this.name = name;
        this.listeningPort = listeningPort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getListeningPort() {
        return listeningPort;
    }

    public void setListeningPort(int listeningPort) {
        this.listeningPort = listeningPort;
    }
    
    
    
}
