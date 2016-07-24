//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014 Sam Saint-Pettersen.
*/
import java.io.*;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;

public class YggdrasillMimes
{
    private JSONObject mimes;
    private String extension;

    private void readMimesFile()
    {
        try
        {
            String file = Files.toString(new File("config//mimes.json"), Charsets.UTF_8);
            this.mimes = (JSONObject)JSONValue.parse(file);
        }
        catch(IOException ioe)
        {
            System.out.println(ioe);
        }
    }

    private JSONObject getShard(String objectName)
    {
        String shardStr = "";
        try {
            shardStr = JSONValue.toJSONString(this.mimes.get(objectName));
        }
        catch(Exception e)
        {
            System.out.println("Error: Badly formatted JSON.");
            System.out.println(e);
        }
        return (JSONObject)JSONValue.parse(shardStr);
    }

    private String getPart(String part)
    {
        String partStr = JSONValue.toJSONString(getShard(this.extension).get(part));
        partStr = partStr.replaceAll("\\\\", ""); // Remove backslashes.
        return partStr.replaceAll("\"", ""); // Remove double quotes.
    }

    public YggdrasillMimes(String ext)
    {
        setExt(ext);
        readMimesFile();
    }

    public void setExt(String ext)
    {
        this.extension = ext;
    }

    public String getMime()
    {
        return getPart("mime");
    }

    public String getName()
    {
        return getPart("name");
    }

    public String getType()
    {
        return getPart("type");
    }

    public String getParse()
    {
        return getPart("parse");
    }

    public boolean getBinary()
    {
        return Boolean.parseBoolean(getPart("binary"));
    }
}
