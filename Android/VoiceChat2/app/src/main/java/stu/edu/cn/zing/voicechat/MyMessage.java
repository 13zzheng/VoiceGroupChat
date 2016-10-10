package stu.edu.cn.zing.voicechat;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/19.
 */
public class MyMessage implements Serializable{

    private static final long serialVersionUID = 1L;

    public static final int MSG_AUDIO = 1;
    public static final int MSG_LOGIN = 2;
    public static final int MSG_TEXT = 3;
    public static final int MSG_AUDIO_MARK= 4;

    private int msgType = -1;


    private String sendName = "";

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    private String msgContent = "";

    private String tip = "";

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    private byte[] msgContentBinary;

    public byte[] getMsgContentBinary() {
        return msgContentBinary;
    }

    public void setMsgContentBinary(byte[] msgContentBinary) {
        this.msgContentBinary = msgContentBinary;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
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

    public MyMessage(int msgType, String msgContent) {
        this.msgType = msgType;
        this.msgContent = msgContent;
    }

    public MyMessage() {

    }

    public MyMessage(int msgType, byte[] msgContentBinary) {
        this.msgType = msgType;
        this.msgContentBinary = msgContentBinary;
    }

}
