package mai_onsyn.trisona.core.data;

import org.jspecify.annotations.NonNull;

public record Date(long time) {

    public @NonNull String toString() {
        return new java.util.Date(time).toString();
    }
}
