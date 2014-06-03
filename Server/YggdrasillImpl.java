/*
    Yggdrasill
    RMI-based distributted HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Yggdrasill implementation **/
public class YggdrasillImpl implements Yggdrasill 
{
    
    private enum HTTP {
        GET, HEAD, POST, PUT
    }
    
    private String handleRequest(String http, String param1)
    {
        System.out.println(http);
        System.out.println(param1);
        return "HTTP/1.1 200 OK";
    }
    
    /** Handle an HTTP request and respond to it **/
    public String sendRespond(String httpRequest)
    {
        System.out.println(httpRequest);
        
        String httpCommand = "(^\\w{3,4}) (\\/{0,1}\\w*\\.{0,1}\\w{3,4})";
        
        Pattern pattern = Pattern.compile(httpCommand);
        Matcher matcher = pattern.matcher(httpRequest);
        
        String http = ""; String param1 = "";
        while(matcher.find()) {
            http = matcher.group(1);
            param1 = matcher.group(2);
        }
        return handleRequest(http, param1);
    }
}















































