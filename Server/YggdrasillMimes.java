//package
/*
    Yggdrasill
    RMI-based distributed HTTP.

    Copyright (c) 2014, 2016 Sam Saint-Pettersen.
*/

public abstract class YggdrasillMimes {
    protected String extension;
    
    public YggdrasillMimes(String ext) {
        setExt(ext);
        readMimesFile();
    }
    
    protected abstract void readMimesFile();
    
    public final void setExt(String ext) {
        this.extension = ext;
    }

    public abstract String getMime();
    public abstract String getName();
    public abstract String getType();
    public abstract String getParse();
    public abstract boolean getBinary();
}
