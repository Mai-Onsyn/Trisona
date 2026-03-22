package mai_onsyn.trisona.core.message;

import java.util.List;

public class Artist extends Message {
    private String name;
    private List<String> otherNames;

    public Artist(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
