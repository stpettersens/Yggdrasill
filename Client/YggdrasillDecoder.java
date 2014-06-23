/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
//package io.stpettersen.yggdrasill.client;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.codec.binary.Base64;
import com.google.common.primitives.*;

@SuppressWarnings("unchecked")
public class YggdrasillDecoder {
    
    public String decodeResponse(List response)
    {
        boolean binary = (boolean)response.get(1);
        if(!binary) {
            String decoded = "";
            for(int i = 3; i < response.size(); i++) {
                int b = (int)response.get(i);
                char c = (char)b;
                decoded += c;
            }
            return decoded;
        }
        else {           
            List bytes = new ArrayList();
            for(int i = 3; i < response.size(); i++) {
                bytes.add(response.get(i));
            }
            List<Byte> bytesList = bytes;
            byte[] decodedBytes = Bytes.toArray(bytesList);
            byte[] encodedBytes = Base64.encodeBase64(decodedBytes);
            return new String("<img src=\"data:image/jpg;base64," + new String(encodedBytes) + "\"/>");
        }
    }
}
