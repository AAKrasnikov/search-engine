import java.util.Objects;

public class SitePage {
    private String patch;
    private int code;
    private String content;

    public SitePage (String patch, int code, String content) {
        this.patch = patch;
        this.code = code;
        this.content = content;
    }

    public String getPatch() {
        return patch;
    }

    public int getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SitePage sitePage = (SitePage) o;
        return Objects.equals(patch, sitePage.patch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patch);
    }
}
