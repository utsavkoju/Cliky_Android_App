package ca.cliky.cliky.objects;

/**
 * Created by utsav on 4/4/2016.
 */
public class ListDetail {
    public String title;
    public String detail;

    public ListDetail(String ttl, String dtl) {
        this.title = ttl;
        this.detail = dtl;
    }

    public void setTitle(String ttl) {
        this.title = ttl;
    }

    public void setDetail(String dtl) {
        this.detail = dtl;
    }
}
