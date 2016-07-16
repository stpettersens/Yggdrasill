//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import com.google.common.io.Files;
import com.google.common.primitives.Bytes;

@SuppressWarnings("unchecked")
public class YggdrasillDecoder {
    public String decodeResponse(List response, String uri)
    {
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
            try {
                Files.write(decodedBytes, new File(String.format("cache/%s", uri)));
            }
            catch(IOException ioe) {
                System.out.println("Problem caching image to file:");
                System.out.println(ioe);
            }
            return String.format("<img src=file:%s/cache/%s", System.getProperty("user.dir"), uri);
        }
        else {
            return "not implemented!";
        }
    }
    
    public String processHtml(String rawHtml) {
        // TODO
        return "<!-- HTML was processed by Swing client -->\n" + rawHtml;
    }
}
