/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import com.google.common.primitives.*;

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
        else {
            List<Byte> list = response;
            byte[] decodedBytes = Bytes.toArray(list);
            byte[] encodedBytes = Base64.encodeBase64(decodedBytes);
            System.out.println(new String(decodedBytes));
            return new String("<img src=\"data:image/jpg;base64," + new String(encodedBytes) + "\"/>");
        }
    }
}
