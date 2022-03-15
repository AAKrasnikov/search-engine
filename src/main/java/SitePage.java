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

}
