// Implementacion de la interfaz de servidor que define los metodos remotos
// para completar la descarga de un fichero
package afs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.*;
import java.rmi.server.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ViceReaderImpl extends UnicastRemoteObject implements ViceReader {
	private static final String AFSDir = "AFSDir/";
	private RandomAccessFile fichero;
	private int tamTot = 0;
	private LockManager lock;
	private String fich;
	private ReentrantReadWriteLock rwl;
	public ViceReaderImpl(String fileName, String permisos, LockManager lock /* pasar referencia de ViceImpl */)
			throws RemoteException, FileNotFoundException, IOException {
		this.fichero = new RandomAccessFile(AFSDir + fileName, permisos);
		this.tamTot = (int) this.fichero.length();
		this.lock = lock;
		this.fich = fileName;
		this.rwl = this.lock.bind(this.fich);
		this.rwl.readLock().lock();
	}

	public byte[] read(int tam) throws IOException {
		
		byte[] b = new byte[tam];	
		if ((this.fichero.read(b)) == -1) {
			b = null;
			return b;
		}
		this.tamTot -= tam;
		if (this.tamTot < 0) {	
			int fin = this.tamTot + tam;
			byte [] c = new byte[fin];
			this.fichero.seek(this.fichero.length()-fin);
			this.fichero.read(c);
			return c;
		}
		return b;
	}

	public void close() throws IOException {
		this.fichero.close();
		this.lock.unbind(this.fich);
		this.rwl.readLock().unlock();
		
	}
}
