/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_client;

/**
 *
 * @author dvchava
 */
public interface ClientOperations {
    public void connect();
    public boolean register(String pass, String server);
}
