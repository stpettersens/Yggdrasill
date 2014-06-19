/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import com.google.common.io.*;

/** Yggdrasill implementation **/
public class YggdrasillImpl implements Yggdrasill 
{  
    private List handleRequest(String http, String[] params)
    {
        List bytesList = new ArrayList();
        String request = params[0].substring(1) + params[1];
        String ext = params[1];
        boolean binary = false;
        if(ext.equals("jpg")) { binary = true; }
        switch(http) {
            case "GET":
                if(!binary) {
                    try {
                        Reader reader = new FileReader("c:\\www\\" + request);
                        int data = reader.read();
                        while(data != -1) {
                            bytesList.add(data);
                            data = reader.read();
                        }
                        reader.close();
                    }
                    catch(IOException e) {
                        // ...
                    }
                }
                else {
                   try {
                       byte[] bytes = Files.toByteArray(new File("c:\\www\\" + request));
                       for(int i = 0; i < bytes.length; i++) {
                           bytesList.add(bytes[i]);
                       }
                    }
                    catch(IOException e) {
                        // ...
                    }
                }
             break;
        }
        if(bytesList.size() <= 2) {
            try {
                Reader reader = new FileReader("c:\\www\\notfound.html");
                int data = reader.read();
                while(data != -1) {
                    bytesList.add(data);
                    data = reader.read();
                }
                reader.close();
            }
            catch(IOException e) {
                // ...
            }
            bytesList.add(0, "HTTP/1.1 404 NOT FOUND");
            bytesList.add(1, binary);
        }
        else {
            bytesList.add(0, "HTTP/1.1 200 OK");
            bytesList.add(1, binary);
        }
        return bytesList;
    }
    
    /** Handle an HTTP request and respond to it **/
    public List sendRespond(String httpRequest)
    {
        System.out.println(httpRequest);
        
        String httpCommand = "(^\\w{3,4}) (\\/{0,1}\\w*\\.{0,1})(\\w{3,4}) (HTTP/1.1)";
        
        Pattern pattern = Pattern.compile(httpCommand);
        Matcher matcher = pattern.matcher(httpRequest);
        
        String http = ""; String[] params = new String[3];
        while(matcher.find()) {
            http = matcher.group(1);
            params[0] = matcher.group(2);
            params[1] = matcher.group(3);
            params[2] = matcher.group(4);
        }
        
        return handleRequest(http, params);
    }
}
