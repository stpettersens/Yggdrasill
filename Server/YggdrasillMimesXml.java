//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@SuppressWarnings("unchecked")
public class YggdrasillMimesXml extends YggdrasillMimes {

    private Map<String, String> mimes; 
    private Map<String, List> attribs;
    
    protected void readMimesFile() {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new File("config//mimes.xml")); 
            
            Element root = doc.getRootElement();
            mimes = new HashMap<String, String>();
            attribs = new HashMap<String, List>();
            
            // Iterate through child elements of root ("mimes") and store key-value pairs.
            for(Iterator i = root.elementIterator(); i.hasNext();) {
                Element el = (Element)i.next();
                String fext = el.attributeValue("extension");
                mimes.put(fext, el.attributeValue("type"));
                List a = new ArrayList<String>();
                for(Iterator x = el.elementIterator(); x.hasNext();) {
                    Element cel = (Element)x.next();
                    a.add(cel.getText());
                }
                attribs.put(fext, a);
            }
        }
        catch(DocumentException e) {
            System.out.println("Problem loading mimes.xml configuration:");
            System.out.println(e);
        }
    }
    
    public YggdrasillMimesXml(String ext) {
        super(ext, "XML");
    }
    
    public String getMime() {
        return mimes.get(this.extension);
    }
    
    public String getName() {
        return attribs.get(this.extension).get(0).toString();
    }
    
    public String getType() {
        return attribs.get(this.extension).get(1).toString();
    }
    
    public String getParse() {
        return attribs.get(this.extension).get(2).toString();
    }
    
    public boolean getBinary() {
        return Boolean.parseBoolean(attribs.get(this.extension).get(3).toString());
    }
}
