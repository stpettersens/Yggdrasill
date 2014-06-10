/*
 	Yggdrasill
 	RMI-based distributed HTTP.

 	Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.rmi.registry.LocateRegistry;
import java.util.List;

public class YggdrasillClient {

	public static void main(String args[]) 
	{
		try {
			/* # Use the network name established in YggdrasillServer to get a
			proxy to an object implementing the Yggdrasill interface. */
			Yggdrasill yProxy = (Yggdrasill) LocateRegistry.getRegistry().lookup("YggdrasillService");

			System.out.println("\nYggdrasill client started...");
			List response = yProxy.sendRespond("GET /index.html HTTP/1.1", false);
			System.out.println(response.get(0));
			YggdrasillDecoder yDecoder = new YggdrasillDecoder();
			System.out.println(yDecoder.decodeResponse(response));
			
		}
		catch (Exception e) {
			System.out.println("\nClient problem: " + e);			
		}
		System.out.println("Client terminated");
	}
}
