// Interfaz de servidor que define los metodos remotos
// para completar la descarga de un fichero
package afs;
import java.io.IOException;
import java.rmi.*;

public interface ViceReader extends Remote {
    public byte[] read(int tam) throws RemoteException, IOException;
    public void close() throws RemoteException, IOException;
    /* aniada los metodos remotos que requiera */
}       

