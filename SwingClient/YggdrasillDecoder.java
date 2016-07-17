//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import com.google.common.io.Files;
import com.google.common.primitives.Bytes;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

@SuppressWarnings("unchecked")
public class YggdrasillDecoder {
    public String decodeResponse(List response) {
        boolean binary = (boolean)response.get(1);
        String mimeType = (String)response.get(3);
        String type = (String)response.get(4);
        if(!binary && type.equals("document")) {
            String decoded = "";
            for(int i = 5; i < response.size(); i++) {
                int b = (int)response.get(i);
                char c = (char)b;
                decoded += c;
            }
            return decoded;
        }
        else if(binary && type.equals("image")) {
            List bytes = new ArrayList();
            for(int i = 5; i < response.size(); i++) {
                bytes.add(response.get(i));
            }
            List<Byte> bytesList = bytes;
            byte[] decodedBytes = Bytes.toArray(bytesList);
            String fn = RandomStringUtils.randomNumeric(6);
            try {
                Files.write(decodedBytes, new File(String.format("cache/%s", fn)));
            }
            catch(IOException ioe) {
                System.out.println("Problem caching image to file:");
                System.out.println(ioe);
            }
            return String.format("<img src=\"file:%s/cache/%s\">", System.getProperty("user.dir"), fn);
        }
        else {
            return "not implemented!";
        }
    }

    public String processHtml(String rawHtml) {
        Document doc = Jsoup.parse(rawHtml, "UTF-8");
        Elements images = doc.getElementsByTag("img");
        for(Element el: images) {
            String img = el.attr("src");
            String[] hb = img.split(",", 2);
            byte[] decodedBytes = Base64.decodeBase64(hb[1]);
            String fn = RandomStringUtils.randomNumeric(6);
            try {
                Files.write(decodedBytes, new File(String.format("cache/%s", fn)));
            }
            catch(IOException ioe) {
                System.out.println("Problem caching image to file:");
                System.out.println(ioe);
            }
            finally {
                el.attr("src", String.format("file://%s/cache/%s", System.getProperty("user.dir"), fn));  
            }
         }
         return doc.html();
    }
}
