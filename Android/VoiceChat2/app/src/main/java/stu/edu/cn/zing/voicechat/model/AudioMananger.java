package stu.edu.cn.zing.voicechat.model;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import stu.edu.cn.zing.voicechat.AudioFile;
import stu.edu.cn.zing.voicechat.mvpinterface.IAudioMananger;
import stu.edu.cn.zing.voicechat.tools.Time;

/**
 * Created by Administrator on 2016/9/4.
 */
public class AudioMananger implements IAudioMananger {
    private String filePath; //�ļ�·��
    private String dirPath; // Ŀ¼·��
    private final String TAG="audio";


    private MediaRecorder mediaRecorder; //¼����
    private MediaPlayer mediaPlayer; //������

    private static final int MAX_LENGTH = 1000 * 60 *20; //���¼��ʱ��

    private long time ;

    private long startTime;
    private long endTime;


    public AudioMananger() {
        //Ĭ��Ŀ¼·��/sdcard/record/
        this.dirPath = AudioFile.SAVE_AUDIO_DIR_PATH;
    }

    public AudioMananger(String dirPath) {
        this.dirPath = dirPath;
    }


    /**
     * ��ʼ¼�� ʹ��amr��ʽ
     */
    @Override
    public void startRecoder() {
        try {
            //�����ļ�Ŀ¼
            Log.i("dirPath",dirPath);
            File dir=new File(dirPath);
            if(!dir.exists()) {
                dir.mkdirs();
                Log.i("folder", "create folder");
            }

            //ʵ����MediaRecoder����
            if (mediaRecorder == null) {
                mediaRecorder = new MediaRecorder();
            }

            //�Ե�ǰʱ��Ϊ�ļ���������ļ�·��
            String name = Time.getTime();
            filePath=dirPath + "/" + name + ".amr"; //����ļ�����·��


            //����MediaRecoder
            mediaRecorder.setOutputFile(filePath); //��������ļ�

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); //������ƵԴΪ��˷�

            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR); //���������ʽ

            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); //������Ƶ����

            mediaRecorder.setMaxDuration(MAX_LENGTH); //�������ʱ��

            //׼��
            mediaRecorder.prepare();
            //��ʼ
            mediaRecorder.start();
            Log.i("start", "startRecoder");
            startTime = System.currentTimeMillis();


        }catch (IllegalStateException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }

    }

    /**
     * ֹͣ¼��������¼���ļ�������������
     * @return
     */
    @Override
    public String stopRecoder() {
        mediaRecorder.stop();
        endTime = System.currentTimeMillis();
        time = endTime - startTime;
        mediaRecorder.reset();
        mediaRecorder.release();
        Log.i("file Path", filePath);
        mediaRecorder = null;
        return filePath;

    }

    /**
     * ȡ��¼����������¼���ļ���
     */
    @Override
    public void cancelRecoder() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        filePath = "";
    }

    /**
     * ���������ļ�
     * @param filePath �ļ���ַ
     */
    @Override
    public void palyAudio(String filePath) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlay();
            }
        });

        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        }


    /**
     * ��ȡ¼��ʱ��
     * @return
     */
    @Override
    public float getTime() {
        return Float.valueOf(time);
    }

    /**
     * ֹͣ���������ļ�
     */
    @Override
    public void stopPlay() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    /**
     * �ж��Ƿ����ڲ���
     * @return
     */
    @Override
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }else {
            return false;
        }
    }
}
