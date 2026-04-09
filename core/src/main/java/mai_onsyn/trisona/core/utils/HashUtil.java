package mai_onsyn.trisona.core.utils;

public class HashUtil {

    public static long stringToNegativeLong(String input) {
        long hash = -3750763034362895579L;
        long prime = 1099511628211L;
        for (char c : input.toCharArray()) {
            hash = hash ^ c;
            hash *= prime;
        }

        return -(hash & Long.MAX_VALUE);
    }
}
