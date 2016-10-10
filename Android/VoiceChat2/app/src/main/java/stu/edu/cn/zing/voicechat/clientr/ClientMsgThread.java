package stu.edu.cn.zing.voicechat.clientr;

import android.os.Bundle;
import android.os.Message;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import stu.edu.cn.zing.voicechat.AudioFile;
import stu.edu.cn.zing.voicechat.MyMessage;
import stu.edu.cn.zing.voicechat.activity.AudioActivity;
import stu.edu.cn.zing.voicechat.tools.Time;

/**
 * Created by Administrator on 2016/9/7.
 */
public class ClientMsgThread extends Thread {


    private ObjectInputStream ois = null;

    public ClientMsgThread(ObjectInputStream ois) {
        this.ois = ois;
    }

    @Override
    public void run() {
        System.out.println("接收数据线程启动，等待接收");
        while (true) {
            try {
                MyMessage myMessage = (MyMessage)ois.readObject();
                System.out.println("接收到服务器传来数据，数据类型：" + myMessage.getMsgType());

                int msgType = myMessage.getMsgType();
                String content = myMessage.getMsgContent();
                String sendName = myMessage.getSendName();

                if (msgType == MyMessage.MSG_LOGIN) {
                    Message message = new Message();
                    message.what = 3;
                    Bundle bundle = new Bundle();
                    bundle.putString("text",content);
                    message.setData(bundle);
                    AudioActivity.handler.sendMessage(message);
                } else if (msgType == MyMessage.MSG_TEXT) {
                    Message message = new Message();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("name",sendName);
                    bundle.putString("text",content);
                    message.setData(bundle);
                    AudioActivity.handler.sendMessage(message);
                } else if (msgType == MyMessage.MSG_AUDIO_MARK && content.equals("start")) {
                    receiveAudio(sendName, Float.valueOf(myMessage.getTip()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveAudio(String username, float time) {

        FileOutputStream fos = null;
        String dirPath = AudioFile.RECEIVE_AUDIO_DIR_PATH;

        //创建文件夹
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            String userName = username;
            System.out.println(userName + " " + time);
            String filePath = dirPath + "/"  + Time.getTime() + userName + ".amr";
            fos = new FileOutputStream(new File(filePath));
            byte[] inputByte;
            System.out.println("开始接受文件");
            while (true) {
                MyMessage myMessage = (MyMessage) ois.readObject();
                System.out.println(myMessage.getMsgType());
                if (myMessage.getMsgType() == MyMessage.MSG_AUDIO_MARK
                        && myMessage.getMsgContent().equals("end")) {
                    break;
                }
                int length = Integer.valueOf(myMessage.getTip());

                inputByte = myMessage.getMsgContentBinary().clone();
                System.out.println("接收长度 " + length);
                fos.write(inputByte, 0, length);
                fos.flush();
            }
            System.out.println("完成接收");
            System.out.println("文件保存路径：" + filePath);

            //显示语音
            Message message = new Message();
            message.what = 2;
            Bundle bundle = new Bundle();
            bundle.putString("name",userName);
            bundle.putString("filePath",filePath);
            bundle.putFloat("time", time);
            System.out.println(filePath);
            message.setData(bundle);
            AudioActivity.handler.sendMessage(message);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
