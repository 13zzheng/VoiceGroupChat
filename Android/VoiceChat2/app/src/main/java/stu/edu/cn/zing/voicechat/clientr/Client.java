package stu.edu.cn.zing.voicechat.clientr;

import android.os.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import stu.edu.cn.zing.voicechat.MyMessage;

/**
 * Created by neng-zheng on 2016/6/22.
 */
public class Client {

    private Socket client;


    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

    private LinkedBlockingDeque<MyMessage> msgQueue = null;

    private ClientMsgThread clientMsgThread = null;

    private final int PORT = 4243;
    private final String SERVICE_ADDRESS = "192.168.191.1";

    public Client() {
        msgQueue = new LinkedBlockingDeque<>(100);
    }

    /**
     * 连接服务器
     */
    public void connect() {
        if (client == null ) {
            try {
                System.out.println("请求连接服务器...");
                client = new Socket(SERVICE_ADDRESS, PORT);
                System.out.println("连接服务器成功");
                oos = new ObjectOutputStream(client.getOutputStream());
                ois = new ObjectInputStream(client.getInputStream());
                clientMsgThread = new ClientMsgThread(ois);
                clientMsgThread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送请求连接的消息
     * @param name 登录姓名
     * @return
     */
    public boolean login(String name) {
        connect();
        boolean b =false;
        try {
            MyMessage myMessage = new MyMessage(MyMessage.MSG_LOGIN, name);
            oos.writeObject(myMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;

    }


    /**
     * 发送语音的消息
     * @param filePath
     * @param time
     */
    public void sendAudioFile(String filePath, float time) {
        connect();
        int length = 0;
        double sumL = 0;
        byte[] sendBytes;
        try {
            File file = new File(filePath);
            long l = file.length();
            //读取文件数据
            FileInputStream fis = new FileInputStream(file);
            sendBytes = new byte[1024];
            System.out.println("开始发送文件");
            //发送语音文件的标志信息，表示客户端接下来发送的是语音文件
            MyMessage startMessage = new MyMessage(MyMessage.MSG_AUDIO_MARK, "start");
            startMessage.setTip(String.valueOf(time));
            oos.writeObject(startMessage);
            oos.flush();
            //发送文件
            while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                MyMessage myMessage = new MyMessage(MyMessage.MSG_AUDIO, sendBytes.clone());
                System.out.println("发送长度 " + length);
                myMessage.setTip(String.valueOf(length));
                oos.writeObject(myMessage);
                oos.flush();
            }
            //文件发送完毕，发送结束标志信息
            MyMessage stopMessage = new MyMessage(MyMessage.MSG_AUDIO_MARK, "end");
            oos.writeObject(stopMessage);
            oos.flush();
            System.out.println("文件发送完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送文本信息
     * @param text 文本内容
     */
    public void sendText(String text) {
        connect();
        MyMessage myMessage = new MyMessage(MyMessage.MSG_TEXT, text);
        try {
            System.out.println("发送文本信息: " + text);
            oos.writeObject(myMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isClosed(){
        return client.isClosed();
    }


}
