import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author dvchava
 */
public interface DirServRemoteInterface extends Remote{
    public String[] getServerListRemote() throws RemoteException;
}