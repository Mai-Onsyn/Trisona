package mai_onsyn.trisona.core.message;

public class UniversalPath extends Message {

    private String netWorkUrl = "";
    private String nativePath = "";

    public void setNativePath(String nativePath) {
        this.nativePath = nativePath;
    }

    public void setNetWorkUrl(String netWorkUrl) {
        this.netWorkUrl = netWorkUrl;
    }

    public String getNetWorkUrl() {
        return netWorkUrl;
    }

    public String getNativePath() {
        return nativePath;
    }

    public UniversalPath(String netWorkUrl, String nativePath) {
        this.netWorkUrl = netWorkUrl;
        this.nativePath = nativePath;
    }

    public UniversalPath() {
        this(null, null);
    }

    public UrlPath getPath() {
        return getPath(PathType.NATIVE);
    }

    public UrlPath getPath(PathType type) {
        return switch (type) {
            case NETWORK -> {
                if (netWorkUrl != null && !netWorkUrl.isEmpty())
                    yield new UrlPath(netWorkUrl, PathType.NETWORK);
                else
                    yield new UrlPath(null, PathType.NULL);
            }
            case NATIVE -> {
                if (nativePath != null && !nativePath.isEmpty())
                    yield new UrlPath(nativePath, PathType.NATIVE);
                else
                    yield new UrlPath(null, PathType.NULL);
            }
            default -> new UrlPath(null, PathType.NULL);
        };
    }

    public record UrlPath(String url, PathType type) {}

    public enum PathType {
        NETWORK, NATIVE, NULL
    }

    @Override
    public String toString() {
        return String.format("[Network: %s, Native: %s]", netWorkUrl, nativePath);
    }
}
