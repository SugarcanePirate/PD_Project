/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_client;


import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author dvchava
 */
public class ClientUI {
    private Client me;
    String dirServIP;
    int dirServPort;
    
    boolean connected;

    public ClientUI(String dirServIP, int dirServPort) {
        this.dirServIP = dirServIP;
        this.dirServPort = dirServPort;
        connected = false;
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
    
    public final static void cls() {
        char c = '\n';
        int length = 25;
        char[] chars = new char[length];
        Arrays.fill(chars, c);
        System.out.print(String.valueOf(chars));
    }
    
    public void initClient(String username){
        if(connected)
            return;
        
        me = new Client(username, dirServIP, dirServPort);
    }
    
    public String[] processCmd(String cmd){
        String[] commands;
        
        cmd = cmd.trim();
        
        commands = cmd.split(" ");
        for(int i = 0; i < commands.length; i++)
            commands[i] = commands[i].toUpperCase();
        
        return commands;
    }
    
    public void printServerList(){
        String[] list = me.getServerList();
        
        for(int i = 0; i < list.length; i++)
            System.out.println((i+1) + " - " + list[i]);
        
    }
    
    public void runUI(){
        Scanner sc = new Scanner(System.in);
        String cmd, user;
        String[] commands;
                
        if(!connected){
            cls();
            System.out.println("Enter a valid username: ");
            System.out.println(":>");
            user = sc.nextLine();
            
            initClient(user);
            me.connect();
            
            printServerList();
        }
        
        while(true){
            System.out.print(":> ");
            commands = processCmd(sc.nextLine());
            switch(commands[0]){
                case "REG":
                    if(commands.length != 3)
                        break;
                    
                    String pass = commands[1];
                    String server = commands[2];
                    
                    if(!me.register(pass, server)){
                        System.out.println("Registration failed!");
                    }
                    
                    break;
                default:
                    System.out.println("'" + commands[0] + "' is not recognized as a command...");
            }
        }
    }

}
