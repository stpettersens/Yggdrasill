/*
 	Yggdrasill
 	RMI-based distributed HTTP.

 	Copyright (c) 2014 Sam Saint-Pettersen.
*/
//package io.stpettersen.yggdrasill.client;
import java.util.List;
import java.rmi.RemoteException;

/** Interface for remote object on a server.
  * All remote objects must implement an interface
  * that extends java.rmi.Remote.
*/
public interface Yggdrasill extends java.rmi.Remote {
	/** 
	 * Signatures of the methods that remote server will offer to a client.
	 * All remote methods must indicate they they may throw a
	 * java.rmi.RemoteException which may occur due to a 
	 * networking problem.
	 */
	public List sendRespond(String httpRequest) throws RemoteException;
}
