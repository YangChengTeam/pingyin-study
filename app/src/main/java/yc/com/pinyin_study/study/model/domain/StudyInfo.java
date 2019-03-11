package yc.com.pinyin_study.study.model.domain;

/**
 * Created by wanglin  on 2018/10/30 17:05.
 * 发音感知 发音学习 发音练习 发音要领
 */
public class StudyInfo {
    /**
     * id : 51
     * video : http://py.upkao.com/a_1.mp4
     * voice : http://phonetic.upkao.com/video/8QZtHbJCX3.mp3
     * cover : http://phonetic.upkao.com/cover/Fpn88FGSTM.jpg
     * img : /Upload/Picture/2018-01-03/5a4c6b7a81fb4.png
     * name : a
     * desp : 发音时，嘴唇自然张大，舌放平，舌头中间微隆，声带颤动。
     * audio : null
     * desp_audio : http://phonetic.upkao.com/video/jN6ph4FdXJ.mp3
     * mouth_cover :
     * image : http://tic.upkao.com/Upload/20181113/5bea7bb74eb9d.png
     */

    private String id;
    private String video;
    private String voice;
    private String cover;
    private String img;
    private String name;
    private String desp;
    private Object audio;
    private String desp_audio;
    private String mouth_cover;
    private String image;
    private String hanzi;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public Object getAudio() {
        return audio;
    }

    public void setAudio(Object audio) {
        this.audio = audio;
    }

    public String getDesp_audio() {
        return desp_audio;
    }

    public void setDesp_audio(String desp_audio) {
        this.desp_audio = desp_audio;
    }

    public String getMouth_cover() {
        return mouth_cover;
    }

    public void setMouth_cover(String mouth_cover) {
        this.mouth_cover = mouth_cover;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHanzi() {
        return hanzi;
    }

    public void setHanzi(String hanzi) {
        this.hanzi = hanzi;
    }
}
