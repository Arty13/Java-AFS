// Clase de cliente que inicia la interaccion con el servicio de
// ficheros remotos
package afs;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.Registry;

public class Venus {
	private Vice serv;
	private String tamBloque;
	private VenusCBImpl venus;

	public Venus() {
		try {
			String servidor = System.getenv("REGISTRY_HOST");
			String puerto = System.getenv("REGISTRY_PORT");
			this.tamBloque = System.getenv("BLOCKSIZE");
			this.serv = (Vice) Naming.lookup("rmi://" + servidor + ":" + puerto + "/AFS");
			this.venus = new VenusCBImpl();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getTamBloque() {
		int tam = Integer.parseInt(this.tamBloque);
		return tam;
	}

	public Vice getServidor() {
		return this.serv;
	}
	
	public VenusCB getVenus() {
		return venus;
	}
}
