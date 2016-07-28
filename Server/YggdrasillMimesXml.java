//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import java.io.File;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class YggdrasillMimesXml extends YggdrasillMimes {

    private Map<String, String> mimes; 
    private Map<String, String> names;
    
    protected void readMimesFile() {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new File("config//mimes.xml")); 
            
            Element root = doc.getRootElement();
            mimes = new HashMap<String, String>();
            names = new HashMap<String, String>();
            
            // Iterate through child elements of root ("mimes") and store key-value pairs.
            for(Iterator i = root.elementIterator(); i.hasNext();) {
                Element el = (Element)i.next();
                mimes.put(el.attributeValue("extension"), el.attributeValue("type"));
                //...
            }
        }
        catch(DocumentException e) {
            System.out.println("Problem loading mimes.xml configuration:");
            System.out.println(e);
        }
    }
    
    public YggdrasillMimesXml(String ext) {
        super(ext);
    }
    
    public String getMime() {
        return mimes.get(this.extension);
    }
    
    public String getName() {
        return "TODO";
    }
    
    public String getType() {
        return "TODO";
    }
    
    public String getParse() {
        return "TODO";
    }
    
    public boolean getBinary() {
        return true;
    }
}
