package io.stpettersens.yggdrasill.server;
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.yaml.snakeyaml.Yaml;

@SuppressWarnings("unchecked")
public class YggdrasillMimesYaml extends YggdrasillMimes {
    
    private Map<String, String> mimes;
    private Map<String, List> mattribs;
    
    protected void readMimesFile() {
        try {
            Yaml yaml = new Yaml();
            mimes = new HashMap<String, String>();
            mattribs = new HashMap<String, List>();
            String doc = Files.toString(new File("config//mimes.yml"), Charsets.UTF_8);
            for(Object data: yaml.loadAll(doc)) {
                List a = (List)data;
                List attribs = new ArrayList<String>();
                for(Object kv: a) {
                    String[] val = kv.toString().split("=", 2);
                    attribs.add(val[1].replace("}", ""));
                }
                String key = attribs.get(0).toString();
                attribs.remove(0);
                mimes.put(key, attribs.get(0).toString());
                mattribs.put(key, attribs);
            }
        }
        catch(IOException ioe) {
            System.out.println("Problem loading mattribs.yml configuration:");
            System.out.println(ioe);
        }
    }
    
    public YggdrasillMimesYaml(String ext) {
        super(ext, "YAML");
    }
    
    public String getMime() {
        return mimes.get(this.extension);
    }
    
    public String getName() {
        return mattribs.get(this.extension).get(1).toString();
    }
    
    public String getType() {
        return mattribs.get(this.extension).get(2).toString();
    }
    
    public String getParse() {
        return mattribs.get(this.extension).get(3).toString();
    }
    
    public boolean getBinary() {
        return Boolean.parseBoolean(
        mattribs.get(this.extension).get(4).toString());
    }
}
