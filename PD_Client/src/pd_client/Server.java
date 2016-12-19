/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author dvchava
 */
public class Server {
    String name;
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public Server(String name, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.name = name;
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
    }
    
    public Server(Server s) {
        this.name = s.getName();
        this.socket = s.getSocket();
        this.oos = s.getOos();
        this.ois = s.getOis();
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    
}
