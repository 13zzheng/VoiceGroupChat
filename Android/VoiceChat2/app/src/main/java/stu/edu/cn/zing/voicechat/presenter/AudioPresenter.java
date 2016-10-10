package stu.edu.cn.zing.voicechat.presenter;


import stu.edu.cn.zing.voicechat.mvpinterface.IAudioMananger;
import stu.edu.cn.zing.voicechat.mvpinterface.IAudioView;
import stu.edu.cn.zing.voicechat.model.User;
import stu.edu.cn.zing.voicechat.clientr.Client;
import stu.edu.cn.zing.voicechat.model.AudioMananger;

/**
 * Created by Administrator on 2016/9/5.
 */
public class AudioPresenter {
    private IAudioMananger iAudioMananger;
    private IAudioView iAudioView;
    private Client client;

    private final int SPACE = 100; //监听的时间间隔
    private boolean isRecoding = false; //是否在录音的状态
    private final int MAX_LEVEL = 7; //图片的最大等级



    public AudioPresenter(IAudioView iAudioView) {
        this.iAudioView = iAudioView;
        iAudioMananger = new AudioMananger();
        System.out.println("初始化Client");
        client = new Client();
    }

    //断开连接
    public void disconnect() {
        client.disconnect();
    }

    public void sendText(final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.sendText(text);
            }
        }).start();
    }

    public void palyAudio(String filePath) {
        iAudioMananger.palyAudio(filePath);
    }

    public boolean isPlaying() {

        return iAudioMananger.isPlaying();
    }
    public void stopPlay() {
        iAudioMananger.stopPlay();
    }

    public void login(final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.login(user.getUserName());
            }
        }).start();
    }

    //开始录音
    public void startRecoder() {
        //开始录音
        iAudioMananger.startRecoder();
        isRecoding = true;
        //显示录音中的对话框
        iAudioView.showDialog();

    }

    public void stopRecoder() {
        final String filePath = iAudioMananger.stopRecoder();
        int temp = Math.round(iAudioMananger.getTime() / 1000);
        final float time = temp;
        //连接服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.sendAudioFile(filePath, time);
            }
        }).start();
    }



}
