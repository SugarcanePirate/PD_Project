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
    
    String[] help = null;

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
    
    private void pressEnter()
    { 
        System.out.println("Press ENTER to continue...");
        try{
            System.in.read();
        }
        catch(Exception e){
            System.out.println("Error - " + e);
        }
        
    }
    
    public void initHelp(){
        help = new String[] {"{help} - show command list.",
                            "{cls} - clear screen.",
                            "{reg password server_number} - register in a server.",
                            "{log username password} - login in the actual server.",
                            "{out} - logout from the actual server.",
                            "{dircnt} - list actual directory content.",
                            "{dirpth} - get actual directory path.",
                            "{dirch destination_directory} - change working directory.",
                            "{dirmk directory_name} - make a directory.",
                            "{fcnt file_name} - get and list file content.",
                            "{fcpy file_name destination_directory} - copy file.",
                            "{fmv file_name destination_directory} - move file.",
                            "{frmv file_name} - remove file/directory."};
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
    
    public void printList(String[] l){
        for (String line : l) {
            System.out.println(line);
        }
    }
    
    public void runUI(){
        Scanner sc = new Scanner(System.in);
        String username, password;
        String[] commands;
                
        if(!connected){
            cls();
            System.out.println("Enter a valid username: ");
            System.out.println(":>");
            username = sc.nextLine();
            
            initClient(username);
            me.connect(); // CONNECTED?????
            initHelp();
            
            printServerList();
        }
        
        while(true){
            System.out.print(":> ");
            commands = processCmd(sc.nextLine());
            switch(commands[0]){
                case "HELP":
                    if(commands.length != 1)
                        break;
                   
                    printList(help);
                    
                    break;
               case "CLS":
                    if(commands.length != 1)
                        break;
                   
                    cls();
                    
                    break;
                    
                case "REG":
                    if(commands.length != 3)
                        break;
                    
                    password = commands[1];
                    int server = Integer.parseInt(commands[2]);
                    
                    if(!me.register(password, server))
                        System.out.println("Registration failed!");
                    
                    
                    break;
                    
                case "LOG":
                    if(commands.length != 3)
                        break;
                    
                    
                    username = commands[1];
                    password = commands[2];
                    
                    if(!me.login(username, password))
                        System.out.println("Login failed!");
                    
                    break;
                    
                case "OUT":
                    if(commands.length != 1)
                        break;
                    
                    if(!me.logout())
                        System.out.println("Logout failed!");
                    
                    break;
                    
                case "DIRCNT":
                    if(commands.length != 1)
                        break;
                    String[] cnt = null;
                    
                    if((cnt = me.getDirContent()) != null)
                        printList(cnt);
                    else
                        System.out.println("Error - getting directory content.");
                    
                    break;
                    
                case "DIRPTH":
                    if(commands.length != 1)
                        break;
                    
                    System.out.println(me.getDirPath());
                    
                    break;
                default:
                    System.out.println("'" + commands[0] + "' is not recognized as a command...");
            }
        }
    }

}
