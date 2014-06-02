/*
 	Yggdrasill
 	RMI-based distributted HTTP.

 	Copyright (c) Sam Saint-Pettersen.
*/

/** Yggdrasill implementation */
public class YggdrasillImpl implements Yggdrasill 
{
	public String sendRespond(String httpRequest)
	{
		System.out.println(httpRequest);
		return "HTTP/1.1 200 OK";
	}
}
