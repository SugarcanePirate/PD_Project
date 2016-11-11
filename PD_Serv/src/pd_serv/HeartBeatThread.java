/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_serv;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author dvchava
 */
public class HeartBeatThread extends Thread{
    public static int MAX_SIZE = 1000;
    DatagramSocket hbSocket;
    DatagramPacket packet;
    HeartBeatMsg hbMsg = null;

    public HeartBeatThread(DatagramSocket hbSocket, int listeningPort, String name) {
        this.hbSocket = hbSocket;
        this.packet = null;
        HeartBeatMsg hbMsg = new HeartBeatMsg(name,listeningPort);
    }
    
    public void packetInitialization(){
        ByteArrayOutputStream byteOutStream;
        ObjectOutputStream os;
        packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
    }
    
    @Override
    public void run(){
        while(true){
            try {
                Thread.sleep(30000);
                
            } catch (InterruptedException e) {
                System.err.println("Error in Heatbeat - " + e);
            }
        }
    }
}
