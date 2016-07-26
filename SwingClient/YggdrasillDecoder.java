//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.common.io.Files;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

@SuppressWarnings("unchecked")
public class YggdrasillDecoder {

    private String cacheFile(byte[] bytes, String fn) {
        try {
            File f = new File(String.format("cache/%s", fn));
            if(!f.exists() && !f.isDirectory()) {
                Files.write(bytes, f);
            }
        }
        catch(IOException ioe) {
            System.out.println("Problem caching file:");
            System.out.println(ioe);
        }
        Path path = Paths.get(String.format("%s/cache/%s", System.getProperty("user.dir"), fn));
        return String.format("file:%s", path.toString().replace("\\", "/"));
    }

    public String decodeResponse(List response, String uri) {
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
            return String.format("<img src=\"%s\">", cacheFile(Bytes.toArray(bytes), uri));
        }
        else {
            return "Not implemented!";
        }
    }

    public String processHtml(String rawHtml) {
        Document doc = Jsoup.parse(rawHtml, "UTF-8");
        BaseEncoding base64 = BaseEncoding.base64();
        Elements images = doc.getElementsByTag("img");
        for(Element el: images) {
            String img = el.attr("src");
            String[] hb = img.split(",", 2);
            String fn = el.attr("name");
            el.removeAttr("name");
            el.attr("src", cacheFile(base64.decode(hb[1]), fn));
         }
         return doc.html();
    }
}
