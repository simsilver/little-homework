package com.simsilver.connect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class SocketServer {
    public static final int PORT = 8888;//监听的端口号

    private static Logger logger = LoggerFactory.getLogger(SocketServer.class);

    public static void main(String[] args) {
        logger.trace("服务器启动...\n");
        SocketServer server = new SocketServer();
        server.init();
    }

    public void init() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                // 一旦有堵塞, 则表示服务器与客户端获得了连接
                Socket client = serverSocket.accept();
                logger.debug("Server: socket in, hash {}", client.hashCode());
                // 处理这次连接
                new HandlerThread(client);
            }
        } catch (Exception e) {
            logger.warn("服务器异常: " + e.getMessage());
        }
    }

    public Message doMessage(Message in) {
        return in.replyMsg(new byte[0]);
    }

    private class HandlerThread implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private final long waitTime = TimeUnit.SECONDS.toNanos(50);
        private long lastActiveTimeStamp;

        private boolean updateTimeStamp(boolean mark) {
            long now = System.nanoTime();
            if (now - lastActiveTimeStamp > waitTime) {
                logger.warn("长连接超时 " + lastActiveTimeStamp + " " + now);
                return false;
            }
            if (mark) {
                lastActiveTimeStamp = now;
            }
            return true;
        }

        public HandlerThread(Socket client) {
            socket = client;
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(this).start();
        }

        public void run() {
            try {
                lastActiveTimeStamp = System.nanoTime();
                while (updateTimeStamp(false)) {
                    if (in.available() > 0) {
                        Message msg = Message.obtainFromInputStream(in);
                        Message msg2 = doMessage(msg);
                        msg2.sendToOutputStream(out);
                    }
                    Thread.sleep(0);
                }
            } catch (Exception e) {
                logger.warn("服务器 run 异常: " + e.getMessage());
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        socket = null;
                        logger.warn("服务端 finally 异常:" + e.getMessage());
                    }
                }
            }
        }
    }
}