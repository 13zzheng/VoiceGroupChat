package stu.edu.cn.zing.voicechat;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MyMessage implements Serializable{

    private static final long serialVersionUID = 1L;

    public static final int MSG_AUDIO = 1; //消息类型，语音消息
    public static final int MSG_LOGIN = 2; //消息类型，登录消息
    public static final int MSG_TEXT = 3; //消息类型，文本消息
    public static final int MSG_AUDIO_MARK= 4;// 消息类型，语音消息的标识

    private String tip = ""; //额外附带的消息，这里也可以把所有消息放在一起然后通过分隔符区分
    private int msgType = -1; //消息类型
    private String sendName = ""; //发送者
    private String msgContent = ""; //消息内容String
    private byte[] msgContentBinary; //消息内容，用来传输文件


    // Getter Setter
    public String getTip() {
        return tip;
    }
    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public byte[] getMsgContentBinary() {
        return msgContentBinary;
    }

    public void setMsgContentBinary(byte[] msgContentBinary) {
        this.msgContentBinary = msgContentBinary;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public int getMsgType() {
        return msgType;
    }

    public String getSendName() {
        return sendName;
    }

    public String getMsgContent() {
        return msgContent;
    }


    /**
     * 构造方法
     * @param msgType 消息类型
     * @param msgContent 消息内容String
     */
    public MyMessage(int msgType, String msgContent) {
        this.msgType = msgType;
        this.msgContent = msgContent;
    }

    public MyMessage() {

    }

    /**
     * 构造方法
     * @param msgType 消息类型
     * @param msgContentBinary 消息内容，文件数据
     */
    public MyMessage(int msgType, byte[] msgContentBinary) {
        this.msgType = msgType;
        this.msgContentBinary = msgContentBinary;
    }
}
