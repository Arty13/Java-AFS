// Implementacion de la interfaz de servidor que define los motodos remotos
// para iniciar la carga y descarga de ficheros
package afs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ViceImpl extends UnicastRemoteObject implements Vice {
	private LockManager lm = new LockManager();
	private HashMap<String, ArrayList<VenusCB>> lista = new HashMap<String, ArrayList<VenusCB>>();

	// Crear estructura para relacionar los ficheros con las listas de callbacks
	public ViceImpl() throws RemoteException, FileNotFoundException {
	}

	public ViceReader download(String fileName, String permisos,
			VenusCB callback/* , LockManager lm /* añadir callback */)
			throws RemoteException, FileNotFoundException, IOException {
		ViceReaderImpl lectura = new ViceReaderImpl(fileName, permisos, lm);
		// añadir a la lista
		addCB(fileName, callback);
		return lectura;
	}

	public ViceWriter upload(String fileName, String permisos,
			VenusCB callback/* , LockManager lm/* anñadir callback */)
			throws RemoteException, FileNotFoundException, IOException {
		ViceWriterImpl escritura = new ViceWriterImpl(fileName, permisos, lm);
		deleteCB(fileName, callback);
		return escritura;
	}

	private synchronized void addCB(String fileName, VenusCB callback) {
		if (this.lista.containsKey(fileName)) {
			this.lista.get(fileName).add(callback);
		} else {
			ArrayList<VenusCB> clientes = new ArrayList<VenusCB>();
			clientes.add(callback);
			this.lista.put(fileName, clientes);
		}
	}

	private synchronized void deleteCB(String fileName, VenusCB callback) throws RemoteException {
		for (int i = 0; i < this.lista.get(fileName).size(); i++) {
			if (!this.lista.get(fileName).get(i).equals(callback)) {
				this.lista.get(fileName).get(i).invalidate(fileName);
			}
		}
		this.lista.get(fileName).remove(fileName);

	}
	// Posibles metodos para gestionar la estructura
}
