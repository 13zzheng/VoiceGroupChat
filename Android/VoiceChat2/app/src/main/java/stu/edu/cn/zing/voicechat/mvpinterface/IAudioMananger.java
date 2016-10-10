package stu.edu.cn.zing.voicechat.mvpinterface;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface IAudioMananger {

    void startRecoder();

    String stopRecoder();

    void cancelRecoder();


    void palyAudio (String filePath);

    void stopPlay();

    boolean isPlaying();

    float getTime();
}
