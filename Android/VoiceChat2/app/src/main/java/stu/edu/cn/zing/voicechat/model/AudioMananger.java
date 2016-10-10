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
    private String filePath; //文件路径
    private String dirPath; // 目录路径
    private final String TAG="audio";


    private MediaRecorder mediaRecorder; //录音类
    private MediaPlayer mediaPlayer; //播放类

    private static final int MAX_LENGTH = 1000 * 60 *20; //最大录音时长

    private long time ;

    private long startTime;
    private long endTime;


    public AudioMananger() {
        //默认目录路径/sdcard/record/
        this.dirPath = AudioFile.SAVE_AUDIO_DIR_PATH;
    }

    public AudioMananger(String dirPath) {
        this.dirPath = dirPath;
    }


    /**
     * 开始录音 使用amr格式
     */
    @Override
    public void startRecoder() {
        try {
            //创建文件目录
            Log.i("dirPath",dirPath);
            File dir=new File(dirPath);
            if(!dir.exists()) {
                dir.mkdirs();
                Log.i("folder", "create folder");
            }

            //实例化MediaRecoder对象
            if (mediaRecorder == null) {
                mediaRecorder = new MediaRecorder();
            }

            //以当前时间为文件名并获得文件路径
            String name = Time.getTime();
            filePath=dirPath + "/" + name + ".amr"; //获得文件绝对路径


            //设置MediaRecoder
            mediaRecorder.setOutputFile(filePath); //设置输出文件

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); //设置音频源为麦克风

            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR); //设置输出格式

            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); //设置音频编码

            mediaRecorder.setMaxDuration(MAX_LENGTH); //设置最大时间

            //准备
            mediaRecorder.prepare();
            //开始
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
     * 停止录音（保存录音文件即发送语音）
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
     * 取消录音（不保存录音文件）
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
     * 播放语音文件
     * @param filePath 文件地址
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
     * 获取录音时长
     * @return
     */
    @Override
    public float getTime() {
        return Float.valueOf(time);
    }

    /**
     * 停止播放语音文件
     */
    @Override
    public void stopPlay() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    /**
     * 判断是否正在播放
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
