//package
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
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.codec.binary.Base64;
import static org.apache.commons.lang.StringEscapeUtils.escapeXml;
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

   
        YggdrasillMimes yMimes = new YggdrasillMimes(ext);
        String mime = yMimes.getMime();
        String title = yMimes.getName();
        String type = yMimes.getType();
        String parse = yMimes.getParse();
        boolean binary = yMimes.getBinary();
        
        switch(http) {
            case "GET":
                if(!binary && parse.equals("markup")) {
                    try {
                       File html = new File("www//" + request);    
                       Document doc = Jsoup.parse(html, "UTF-8", "");
                       title = "[" + doc.title() + "]";
                       String img = doc.select("img").attr("src");
                       byte[] imgBytes = Files.toByteArray(new File("www//" + img));
                       byte[] encImage = Base64.encodeBase64(imgBytes);
                       
                       String iExt = Files.getFileExtension("www//" + img);
                       yMimes.setExt(iExt);
                       String iMime = yMimes.getMime();
                       String iImg = new String(encImage);
                       
                       doc.select("img").attr("src", String.format("data:%s;base64,%s", iMime, iImg));
                       String strDoc = doc.html();
                       byte[] bytes = strDoc.getBytes();
                       if(bytes.length > 0) {
                           Files.write(bytes, new File("www//_parsed_.html"));        
                           Reader reader = new FileReader("www//_parsed_.html");
                           int data = reader.read();
                           while(data != -1) {
                               bytesList.add(data);
                               data = reader.read();
                           }
                           reader.close();
                           bytesList.add(0, "HTTP/1.1 200 OK\n");
                           bytesList.add(1, binary);
                           bytesList.add(2, title);
                           bytesList.add(3, mime);
                           bytesList.add(4, type);
                       }
                    }
                    catch(IOException e) {
                      //System.out.println("Exception non-binary 1");
                      //System.out.println(e);
                    }
                }
                else if(!binary && parse.equals("pretty")) {
                    try {
                        String document = "";  
                        List<String> file = Files.readLines(new File("www//" + request), Charsets.UTF_8);     
                        for(int i = 0; i < file.size(); i++) {
                            document += escapeXml(file.get(i)) + "\n";
                        }    
                        String output = String.format("<script src=\"https://google-code-prettify.googlecode.com/svn/loader/"
                        + "run_prettify.js\"></script>\n<pre class=\"prettyprint linenums\">%s</pre>", document);
                        byte[] bytes = output.getBytes();
                        if(bytes.length > 0) {
                           Files.write(bytes, new File("www//_parsed_.html"));        
                           Reader reader = new FileReader("www//_parsed_.html");
                           int data = reader.read();
                           while(data != -1) {
                               bytesList.add(data);
                               data = reader.read();
                           }
                           reader.close();
                           bytesList.add(0, "HTTP/1.1 200 OK\n");
                           bytesList.add(1, binary);
                           bytesList.add(2, title);
                           bytesList.add(3, mime);
                           bytesList.add(4, type);
                        }
                    }
                    catch(IOException e)
                    {
                        // ...
                    }
                }
                else if(!binary)
                {
                    try {
                        Reader reader = new FileReader("www//" + request);
                        int data = reader.read();
                        while(data != -1) {
                            bytesList.add(data);
                            data = reader.read();
                        }
                        reader.close();
                        bytesList.add(0, "HTTP/1.1 200 OK\n");
                        bytesList.add(1, binary);
                        bytesList.add(2, title);
                        bytesList.add(3, mime);
                        bytesList.add(4, type);
                    }
                    catch(IOException e) {
                        //System.out.println("Exception non-binary 1");
                        //System.out.println(e);
                    }
                }
                else {
                   try {
                       byte[] bytes = Files.toByteArray(new File("www//" + request));
                       for(int i = 0; i < bytes.length; i++) {
                           bytesList.add(bytes[i]);
                       }
                       bytesList.add(0, "HTTP/1.1 200 OK\n");
                       bytesList.add(1, binary);
                       bytesList.add(2, title);
                       bytesList.add(3, mime);
                       bytesList.add(4, type);
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
                yMimes.setExt("html");
                title = "[404: Not Found]";
                mime = yMimes.getMime();
                type = yMimes.getType();
                parse = yMimes.getParse();
                binary = yMimes.getBinary();
                
                Reader reader = new FileReader("www//_notfound_.html");
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
            bytesList.add(3, mime);
            bytesList.add(4, type);
        }
        System.out.println(bytesList.get(0));
        
        return bytesList;
    }
    
    /** Handle an HTTP request and respond to it **/
    public List sendRespond(String httpRequest)
    {
        System.out.println(httpRequest);
        
        String httpCommand = "(^\\w{3,4}) (\\/{0,1}\\w*\\.{0,1})(\\w{0,20}) (HTTP/1.1)";
        
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
