package io.stpettersens.yggdrasill.server;
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) Sam Saint-Pettersen.
*/
import net.sf.lipermi.net.Server;
import net.sf.lipermi.handler.CallHandler;

public class YggdrasillServer {

    public static void main(String args[]) {
        
        String CONFIG_TYPE = "xml";
        
        try {
            Server server = new Server();
            
            if(args.length > 0) {
                CONFIG_TYPE = args[0];
            }
            
            YggdrasillImpl yggdrasillService = new YggdrasillImpl(CONFIG_TYPE);
            
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
