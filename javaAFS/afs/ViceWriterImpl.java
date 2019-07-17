// Implementacion de la interfaz de servidor que define los metodos remotos
// para completar la carga de un fichero
package afs;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.*;
import java.rmi.server.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ViceWriterImpl extends UnicastRemoteObject implements ViceWriter {
    private static final String AFSDir = "AFSDir/";
    private RandomAccessFile fichero;
    private LockManager lock;
    private String fich;
    private ReentrantReadWriteLock rwl;
    public ViceWriterImpl(String fileName, String persmisos, LockManager lock /* pasar referencia de ViceImpl */)
		    throws RemoteException, FileNotFoundException,IOException {
    	this.fichero = new RandomAccessFile(AFSDir + fileName, persmisos);
    	this.fich = fileName;
    	this.fichero.setLength(0);
    	this.lock = lock;
    	this.fich = fileName;
    	rwl = this.lock.bind(this.fich);
    	rwl.writeLock().lock();
    }
    
    public void write(byte [] b) throws IOException {
    	
    	this.fichero.write(b);
    }
    public void close() throws IOException {
    	this.fichero.close();
    	this.lock.unbind(this.fich);
    	rwl.writeLock().unlock();
    	
    }
}       

