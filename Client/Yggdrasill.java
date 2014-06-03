/*
 	Yggdrasill
 	RMI-based distributted HTTP.

 	Copyright (c) 2014 Sam Saint-Pettersen.
*/

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
	public String sendRespond(String httpRequest) throws java.rmi.RemoteException;
}
