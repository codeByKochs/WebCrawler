package crawler.logic;

import java.util.Objects;

public class Url {

    private String url;
    private String title;
    private int crawlDepth;

    public Url(String url, String title, int crawlDepth) {
        this.url = url;
        this.title = title;
        this.crawlDepth = crawlDepth;
    }

    public Url(String url, int crawlDepth) {
        this.url = url;
        this.crawlDepth = crawlDepth;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getCrawlDepth(){ return crawlDepth;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url1 = (Url) o;
        return (this.url.equals(url1.getUrl()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title);
    }
}
