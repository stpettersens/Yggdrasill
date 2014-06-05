/*
 	Yggdrasill
 	RMI-based distributed HTTP.

 	Copyright (c) Sam Saint-Pettersen.
*/
import java.util.List;

/** Interface for remote object on a server.
  * All remote objects must implement an interface
  * that extends java.rmi.Remote.
*/
public interface Yggdrasill extends java.rmi.Remote {
	/** 
	 * Signatures of the methods that our remote calculator 
	 * will offer to a client.
	 * All remote methods must indicate they they may throw a
	 * java.rmi.RemoteException which may occur due to a 
	 * networking problem.
	 */
	public List sendRespond(String httpRequest, boolean binary) throws java.rmi.RemoteException;
}
