// Clase de cliente que define la interfaz a las aplicaciones.
// Proporciona la misma API que RandomAccessFile.
package afs;

import java.rmi.*;

import java.io.*;

public class VenusFile {
	public static final String cacheDir = "Cache/";
	private Venus serv;
	private RandomAccessFile fich;
	private boolean escrito = false;
	private String nombreFich;
	private String modo;
	private int bloque;
	public VenusFile(Venus venus, String fileName, String mode)
			throws RemoteException, IOException, FileNotFoundException {
		
		File fichero = new File(cacheDir + fileName);
		this.nombreFich = fileName;
		this.modo = mode;
		this.bloque = venus.getTamBloque();
		this.serv = venus;
		if (!fichero.exists()) {
			ViceReader descarga = venus.getServidor().download(fileName, mode, venus.getVenus());
			fichero.createNewFile();
			this.fich = new RandomAccessFile(cacheDir + fileName, "rw");
			byte [] lectura = new byte [this.bloque];
			while( (lectura = descarga.read(this.bloque)) != null) {
				this.fich.write(lectura);
			}
			descarga.close();
			this.fich.close();
		}
		this.fich = new RandomAccessFile(cacheDir + fileName, mode);
	}

	public int read(byte[] b) throws RemoteException, IOException {
		int leidos = this.fich.read(b);
		return leidos;
	}

	public void write(byte[] b) throws RemoteException, IOException {
		fich.write(b);
		escrito = true;
	}

	public void seek(long p) throws RemoteException, IOException {
		fich.seek(p);
	}

	public void setLength(long l) throws RemoteException, IOException {
		fich.setLength(l);
		escrito = true;
	}

	public void close() throws RemoteException, IOException {
		if(this.escrito) {
			ViceWriter writer = this.serv.getServidor().upload(this.nombreFich, this.modo, this.serv.getVenus());
			byte b [] = new byte [bloque];
			int tam = (int) this.fich.length();
			boolean fin = false;
			this.fich.seek(0);
			while(!fin &&  this.fich.read(b)!= -1) {
				if(tam < this.bloque) {
					byte [] c = new byte[tam];
					this.fich.seek(this.fich.length()-tam);
					this.fich.read(c);
					writer.write(c);
					fin = true;
				} else {
					tam -= this.bloque;
					writer.write(b);
				}
			}
			writer.close();
		}
		this.fich.close();
	}
}
