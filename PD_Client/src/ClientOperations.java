/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author dvchava
 */
public interface ClientOperations {
    public boolean connect();
    public boolean register(String pass, int server);
    public boolean login(String username, String password, String serverName);
    public boolean logout();
    public String[] getDirContent();
    public String getDirPath();
    public boolean makeDir(String dirName);
    public boolean changeDir(String dirName);
    public String[] getFileContent(String fileName);
    public boolean removeFile(String fileName);
    public boolean copyFileRemote(String filePath, String server_origin, String server_destination, boolean move);
    public boolean copyFileLocal(String filePath, String destination);
    public boolean moveFileLocal(String filePath, String destination);
    public void exit();
}
