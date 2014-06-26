/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
//package io.stpettersen.yggdrasill.server;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import com.google.common.io.*;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.jsoup.parser.*;

/** Yggdrasill implementation **/
@SuppressWarnings("unchecked")
public class YggdrasillImpl implements Yggdrasill 
{  
    private List handleRequest(String http, String[] params)
    {
        List bytesList = new ArrayList();
        String request = params[0].substring(1) + params[1];
        String ext = params[1];
        String title = "";
        boolean binary = false;
        if(ext.equals("jpg")) {
            title = "[JPEG Image]";
            binary = true; 
        }
        switch(http) {
            case "GET":
                if(!binary) {
                    try {
                       File html = new File("c:\\www\\" + request);    
                       Document doc = Jsoup.parse(html, "UTF-8", "");
                       title = "[" + doc.title() + "]";
                       String img = doc.select("img").attr("src");
                       byte[] imgBytes = Files.toByteArray(new File("c:\\www\\" + img));
                       byte[] encImage = Base64.encodeBase64(imgBytes);
                       doc.select("img").attr("src", "data:image/jpg;base64," + new String(encImage));
                       String strDoc = doc.html();
                       byte[] bytes = strDoc.getBytes();
                       if(bytes.length > 0) {
                           Files.write(bytes, new File("c:\\www\\_parsed_.html"));        
                           Reader reader = new FileReader("c:\\www\\_parsed_.html");
                           int data = reader.read();
                           while(data != -1) {
                               bytesList.add(data);
                               data = reader.read();
                           }
                           reader.close();
                           bytesList.add(0, "HTTP/1.1 200 OK\n");
                           bytesList.add(1, binary);
                           bytesList.add(2, title);
                       }
                    }
                    catch(IOException e) {
                      //System.out.println("Exception non-binary");
                      //System.out.println(e);
                    }
                }
                else {
                   try {
                       byte[] bytes = Files.toByteArray(new File("c:\\www\\" + request));
                       for(int i = 0; i < bytes.length; i++) {
                           bytesList.add(bytes[i]);
                       }
                       bytesList.add(0, "HTTP/1.1 200 OK\n");
                       bytesList.add(1, binary);
                       bytesList.add(2, title);
                    }
                    catch(IOException e) {
                       //System.out.println("Exception binary");
                       //System.out.println(e);
                    }
                }
             break;
        }
        if(bytesList.size() <= 2) {
            try {
                title = "[404: Not Found]";
                binary = false;
                Reader reader = new FileReader("c:\\www\\_notfound_.html");
                int data = reader.read();
                while(data != -1) {
                    bytesList.add(data);
                    data = reader.read();
                }
                reader.close();
            }
            catch(IOException e) {
                //System.out.println("Exception bytesList <= 2");
                //System.out.println(e);
            }
            bytesList.add(0, "HTTP/1.1 404 NOT FOUND\n");
            bytesList.add(1, binary);
            bytesList.add(2, title);
        }
        System.out.println(bytesList.get(0));
        
        return bytesList;
    }
    
    /** Handle an HTTP request and respond to it **/
    public List sendRespond(String httpRequest)
    {
        System.out.println(httpRequest);
        
        String httpCommand = "(^\\w{3,4}) (\\/{0,1}\\w*\\.{0,1})(\\w{3,20}) (HTTP/1.1)";
        
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
