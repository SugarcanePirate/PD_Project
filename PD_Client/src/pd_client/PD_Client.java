/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_client;

/**
 *
 * @author David
 */
public class PD_Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String dirServIp;
        int dirServPort;

        int argc = args.length;
        if (argc != 2) {
            System.out.println("Error - Number of arguments...");
            return;
        }

        dirServIp = args[0];
        dirServPort = Integer.parseInt(args[1]);

        ClientUI ui = new ClientUI(dirServIp, dirServPort);
        ui.runUI();
        

    }
}
