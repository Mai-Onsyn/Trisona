package mai_onsyn.trisona.core.message;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Artist extends Message {
    private long id;
    private String name;
    private final List<String> alias;

    public Artist(long id, String name) {
        this.id = id;
        this.name = name;
        this.alias = new ArrayList<>();
    }

    public Artist(long id, String name, List<String> alias) {
        this.id = id;
        this.name = name;
        if (alias == null) this.alias = new ArrayList<>();
        else this.alias = alias;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getAlias() {
        return alias;
    }

    public void addOtherName(String name) {
        alias.add(name);
    }

    @Override
    public String toString() {
        return String.format(
                "Artist{id=%d, name=%s, alias=%s}",
                id, name, Arrays.toString(alias.toArray())
        );
    }
}
