/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.util.List;

public class YggdrasillDecoder {

    public YggdrasillDecoder()
    {
    }
    public String decodeResponse(List response)
    {
        boolean binary = (boolean)response.get(1);
        if(!binary) {
            String decoded = "";
            for(int i = 2; i < response.size(); i++) {
                int b = (int)response.get(i);
                char c = (char)b;
                decoded += c;
            }
            return decoded;
        }
        return "binary not implemented.";
    }
}
