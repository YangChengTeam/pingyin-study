package yc.com.pinyin_study.error.model.bean;

/**
 * Created by wanglin  on 2019/3/7 17:02.
 */
public class ErrorInfo {


    /**
     * id : 3
     * img : http://zyl.wk2.com/uploads/20190307/7b0968e7f6508ae381a761416c381ed0.png
     */

    private String id;
    private String img;
    private String content;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}


