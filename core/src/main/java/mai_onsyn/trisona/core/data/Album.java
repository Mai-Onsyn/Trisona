package mai_onsyn.trisona.core.data;

import mai_onsyn.trisona.core.message.UniversalPath;

import java.util.ArrayList;
import java.util.Arrays;

public class Album extends ArrayList<Long> {
    private long id = -1;
    private String name;
    private UniversalPath picUrl;

    public Album() {
        this.picUrl = new UniversalPath();
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPicUrlNet(String path) {
        this.picUrl.setNetWorkUrl(path);
    }
    public void setPicUrlNative(String path) {
        this.picUrl.setNativePath(path);
    }
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPicUrl() {
        return picUrl.getPath().url();
    }
    public String getPicUrlNet() {
        return picUrl.getNetWorkUrl();
    }
    public String getPicUrlNative() {
        return picUrl.getNativePath();
    }

    @Override
    public String toString() {
        return String.format(
                "Album{id=%s, name=%s, picUrl=%s, content=%s}",
                id, name, picUrl, Arrays.toString(super.toArray())
        );
    }
}
