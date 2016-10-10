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

    private final int SPACE = 100; //������ʱ����
    private boolean isRecoding = false; //�Ƿ���¼����״̬
    private final int MAX_LEVEL = 7; //ͼƬ�����ȼ�



    public AudioPresenter(IAudioView iAudioView) {
        this.iAudioView = iAudioView;
        iAudioMananger = new AudioMananger();
        System.out.println("��ʼ��Client");
        client = new Client();
    }

    //�Ͽ�����
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

    //��ʼ¼��
    public void startRecoder() {
        //��ʼ¼��
        iAudioMananger.startRecoder();
        isRecoding = true;
        //��ʾ¼���еĶԻ���
        iAudioView.showDialog();

    }

    public void stopRecoder() {
        final String filePath = iAudioMananger.stopRecoder();
        int temp = Math.round(iAudioMananger.getTime() / 1000);
        final float time = temp;
        //���ӷ�����
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.sendAudioFile(filePath, time);
            }
        }).start();
    }



}
