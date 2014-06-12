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
import org.apache.commons.codec.binary.Base64;

/** Yggdrasill implementation **/
public class YggdrasillImpl implements Yggdrasill 
{  
    private List handleRequest(String http, String param1, String param2, boolean binary)
    {
        List bytes = new ArrayList();
        switch(http) {
            case "GET":
                try {
                    Reader reader = new FileReader("c:\\www\\" + param1.substring(1));
                    int data = reader.read();
                    while(data != -1) {
                        bytes.add(data);
                        data = reader.read();
                    }
                    reader.close();
                }
                catch(IOException e) {
                    // ...
                }
             break;
        }
        if(bytes.size() <= 2) {
            try {
                Reader reader = new FileReader("c:\\www\\notfound.html");
                int data = reader.read();
                while(data != -1) {
                    bytes.add(data);
                    data = reader.read();
                }
                reader.close();
            }
            catch(IOException e) {
                // ...
            }
            bytes.add(0, "HTTP/1.1 404 NOT FOUND");
            bytes.add(1, binary);
        }
        else {
            bytes.add(0, "HTTP/1.1 200 OK");
            bytes.add(1, binary);
        }
        System.out.println(bytes.get(0));
        return bytes;
    }
    
    /** Handle an HTTP request and respond to it **/
    public List sendRespond(String httpRequest, boolean binary)
    {
        System.out.println(httpRequest);
        
        String httpCommand = "(^\\w{3,4}) (\\/{0,1}\\w*\\.{0,1}\\w{3,4}) (HTTP/1.1)";
        
        Pattern pattern = Pattern.compile(httpCommand);
        Matcher matcher = pattern.matcher(httpRequest);
        
        String http = ""; String param1 = ""; String param2 = "";
        while(matcher.find()) {
            http = matcher.group(1);
            param1 = matcher.group(2);
            param2 = matcher.group(3);
        }
        return handleRequest(http, param1, param2, binary);
    }
}
