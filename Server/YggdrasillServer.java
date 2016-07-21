//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) Sam Saint-Pettersen.
*/
import net.sf.lipermi.net.Server;
import net.sf.lipermi.handler.CallHandler;

public class YggdrasillServer {

    public static void main(String args[])
    {
        try {
            Server server = new Server();
            YggdrasillImpl yggdrasillService = new YggdrasillImpl();
            
            // A call handler is always needed.
            CallHandler callHandler = new CallHandler();
            callHandler.registerGlobal(Yggdrasill.class, yggdrasillService);
            server.bind(4455, callHandler);
            
            System.out.println("Yggdrasill server ready...");
        }
        catch(Exception e) {
            System.out.println("\nServer problem:\n" + e);
        }
    }
}
