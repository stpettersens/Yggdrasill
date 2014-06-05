/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.util.List;

public class YggrasillDecoder {

    /**
     * Constructor for objects of class YggrasillDecoder
     */
    public YggrasillDecoder()
    {
    }
    public String decodeResponse(List response)
    {
        boolean binary = (boolean)response.get(1);
        if(!binary) {
            String decoded = "";
            for(int i = 2; i < response.size(); i++) {
                int b = (int) response.get(i);
                char c = (char) b;
                decoded += c;
            }
            System.out.println(decoded);
            return decoded;
        }
        return "binary not implemented.";
    }
}
