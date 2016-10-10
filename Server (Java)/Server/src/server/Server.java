package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Administrator on 2016/9/10.
 */
public class Server {

    private ServerSocket serverSocket = null;

    private ServerThread serverThread = null; //服务器线程，负责监听某个端口与客户端通信，（自写类）


    private final int PORT = 4243; //服务器监听端口


    //构造方法
    public Server() {
    }

    //启动一个服务器线程
    public void runServer() {
        if (serverSocket == null) {
            try {
                //监听端口PORT
                serverSocket = new ServerSocket(PORT);
                //启动服务器线程
                serverThread = new ServerThread(serverSocket);
                serverThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] arg) {
        Server server = new Server();
        server.runServer();

    }

}
