/*
 	Yggdrasill
 	RMI-based distributted HTTP.

 	Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.rmi.registry.LocateRegistry;

public class YggdrasillClient {

	public static void main(String args[]) 
	{
		try {
			/* # Use the network name established in YggdrasillServer to get a
			proxy to an object implementing the Yggdrasill interface. */
			Yggdrasill yProxy = (Yggdrasill) LocateRegistry.getRegistry().lookup("YggdrasillService");

			System.out.println("\nYggdrasill client started...");
			String response = yProxy.sendRespond("GET /index.html HTTP/1.1");
			System.out.println(response);
		}
		catch (Exception e) {
			System.out.println("\nClient problem: " + e);			
		}
		System.out.println("Client terminated");
	}
}
