/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author dvchava
 */
public class ClientUI {
    private ClientC me;
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
        int length = 35;
        char[] chars = new char[length];
        Arrays.fill(chars, c);
        System.out.print(String.valueOf(chars));
    }

    
    public void initHelp(){
        help = new String[] {"{exit} - exit application.",
                            "{help} - show command list.",
                            "{cls} - clear screen.",
                            "{reg password server_number} - register in a server.",
                            "{log username password} - login in the actual server.",
                            "{out} - logout from the actual server.",
                            "{dircnt} - list actual directory content.",
                            "{dirpth} - get actual directory path.",
                            "{chdir destination_directory} - change working directory.",
                            "{mkdir directory_name} - make a directory.",
                            "{fcnt file_name} - get and list file content.",
                            "{fcpy file_name destination_directory} - copy file.",
                            "{fmv file_name destination_directory} - move file.",
                            "{frcpy file_name origin_server destination_server} - copy file between directories.",
                            "{frmov file_name origin_server destination_server} - move file between directories.",
                            "{frmv file_name} - remove file/directory.",
                            "{srvls} - shows servers list."};
    }
    
    
    public void initClient(String username){
        if(connected)
            return;
        
        me = new ClientC(username, dirServIP, dirServPort);
    }
    
    public String[] processCmd(String cmd){
        String[] commands;
        
        cmd = cmd.trim();
        
        commands = cmd.split(" ");
        
            commands[0] = commands[0].toUpperCase();
        
        return commands;
    }
    
//    public void printServerList(){
//        String[] list = me.getServerList();
//        
//        for(int i = 0; i < list.length; i++)
//            System.out.println((i+1) + " - " + list[i]);
//    }
    
    public void printList(String[]  l){
        if(l == null)
            return;
        
        for (String line : l) {
            System.out.println(line);
        }
    }
    
    public void runUI(){
        Scanner sc = new Scanner(System.in);
        String username, password;
        String[] commands;
        String[] cnt;
                
       do{
            cls();
            System.out.println("Enter a valid username: ");
            System.out.println(":>");
            username = sc.nextLine();
            initClient(username);
            
        }while(!me.connect());
       
       initHelp();
       printList(me.getServerList());
        
        while(true){
            System.out.print(":> ");
            commands = processCmd(sc.nextLine());
            switch(commands[0]){
                case "EXIT":
                    if(commands.length != 1)
                        break;
                   
                    me.exit();
                    System.exit(0);
                    
                    break;
                    
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
                    int server = 0;
                    try{
                        server = Integer.parseInt(commands[2]);
                    }catch(NumberFormatException e){
                        System.out.println("Servidor desconhecido...");
                        break;
                    }
                    if(!me.register(password, server))
                        System.out.println("Registration failed!");
                    
                    
                    break;
                    
                case "LOG":
                    if(commands.length != 4)
                        break;
                    
                    
                    username = commands[1];
                    password = commands[2];
                    String serverName = commands[3];
                    if(!me.login(username, password, serverName))
                        System.out.println("Login failed!");
                    
                    break;
                    
                case "OUT":
                    if(commands.length != 1 || GlobalsC.getLogged() != 1)
                        break;
                    
                    if(!me.logout())
                        System.out.println("Logout failed!");
                    
                    break;
                    
                case "DIRCNT":
                    if(commands.length != 1 || GlobalsC.getLogged() != 1)
                        break;
                    cnt = null;
                    
                    if((cnt = me.getDirContent()) != null)
                        printList(cnt);
                    else
                        System.out.println("Error - getting directory content.");
                    
                    break;
                    
                case "DIRPTH":
                    if(commands.length != 1 || GlobalsC.getLogged() != 1)
                        break;
                    
                    System.out.println(me.getDirPath());
                    
                    break;
                    
                case "MKDIR":
                    if(commands.length != 2 || GlobalsC.getLogged() != 1)
                        break;
                    
                    if(!me.makeDir(commands[1]))
                        System.out.println("Error - creating directory.");
                    
                    break;
                    
                case "CHDIR":
                    if(commands.length != 2 || GlobalsC.getLogged() != 1)
                        break;
                    
                    if(!me.changeDir(commands[1]))
                        System.out.println("Error - changing directory.");
                    
                    break;
                    
                case "FCNT":
                    if(commands.length != 2 || GlobalsC.getLogged() != 1)
                        break;
                    
                    cnt = null;
                    
                    if((cnt = me.getFileContent(commands[1])) != null)
                        printList(cnt);
                    
                    break;
                    
                case "FRMV":
                    if(commands.length != 2 || GlobalsC.getLogged() != 1)
                        break;
                    
                    if(me.removeFile(commands[1]))
                        System.out.println("File removed successfully.");
                    
                    break; 
                case "FRCPY":
                    if(commands.length != 4 || GlobalsC.getLogged() != 1)
                        break;
                    
                    if(me.copyFileRemote(commands[1],commands[2],commands[3], false))
                        System.out.println("File transfered successfully.");
                    
                    break; 
                case "FRMOV":
                    if(commands.length != 4 || GlobalsC.getLogged() != 1)
                        break;
                    
                    if(me.copyFileRemote(commands[1],commands[2],commands[3], true))
                        System.out.println("File transfered successfully.");
                    
                    break;
                    
                case "FCPY":
                    if(commands.length != 3 || GlobalsC.getLogged() != 1)
                        break;
                    
                    if(me.copyFileLocal(commands[1],commands[2]))
                        System.out.println("File copied successfully.");
                    
                    break; 
                    
                case "FMOV":
                    if(commands.length != 3 || GlobalsC.getLogged() != 1)
                        break;
                    
                    if(me.moveFileLocal(commands[1],commands[2]))
                        System.out.println("File transfered successfully.");
                    
                    break;
                    
                case "SRVLS":
                    if(commands.length != 1)
                        break;
                    
                    printList(GlobalsC.getServerList());
                    
                    break;    
                    
                default:
                    System.out.println("'" + commands[0] + "' is not recognized as a command...");
            }
        }
    }

}