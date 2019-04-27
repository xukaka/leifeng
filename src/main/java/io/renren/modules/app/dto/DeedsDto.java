package io.renren.modules.app.dto;

import java.io.Serializable;

/**
 * @author huangshishui
 * @date 2019/4/26 0:49
 **/
public class DeedsDto implements Serializable {

    private String title;

    private String context;

    private String deedsId;

    private int readCount;

    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getDeedsId() {
        return deedsId;
    }

    public void setDeedsId(String deedsId) {
        this.deedsId = deedsId;
    }

}
