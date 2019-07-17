// Interfaz de servidor que define los metodos remotos para iniciar
// la carga y descarga de ficheros
package afs;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.*;

public interface Vice extends Remote {
    public ViceReader download(String fileName, String permisos, VenusCB callback/*, LockManager lm /* añadir callback */)
          throws RemoteException, FileNotFoundException, IOException;
    public ViceWriter upload(String fileName, String permisos, VenusCB callback/*, LockManager lm /* añadir callback */)
          throws RemoteException, FileNotFoundException, IOException;

    /* aniada los metodos remotos que requiera */
}
       

