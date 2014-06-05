/*
 	Yggdrasill
 	RMI-based distributed HTTP.

 	Copyright (c) Sam Saint-Pettersen.
*/
import java.rmi.registry.LocateRegistry;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;

public class YggdrasillServer {

	public static void main(String args[]) 
	{
		try {
			/* # Create the object to be accessed remotely using the interface */
			Yggdrasill y = new YggdrasillImpl();

			/* # Create a proxy object to supply to a remote client */
			Remote yProxy = UnicastRemoteObject.exportObject(y, 0);

			/* # Give the proxy object a network name */
			LocateRegistry.getRegistry().rebind("YggdrasillService", yProxy);

			/* # Confirm success with preparing the proxy */
			System.out.println("Yggdrasill server ready...");

			/* # Main method will now terminate, but the JVM holding Yggdrasill 
			and proxy objects will continue to run until killed. (Ctrl+C) */
		}
		catch(Exception e) {
			System.out.println("\nServer problem:" + e);
		}
	}
}
