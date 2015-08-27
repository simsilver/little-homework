package com.simsilver.connect;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 */
public class ConnectDist {
    private static Socket mSocket;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Thread mSendThread;
    private static BlockingQueue<Message> mSendQueue;
    private static Thread mReadThread;
    private static Thread mProcessThread;
    private static BlockingQueue<Message> mReadQueue;
    private static ConcurrentHashMap<Long, Object> mNotifyMap;
    private static boolean mRequireQuitMark = false;

    static {
        connect();
        process();
    }

    private static void connect() {
        try {
            mSocket = new Socket("127.0.0.1", SocketServer.PORT);
            out = new DataOutputStream(mSocket.getOutputStream());
            in = new DataInputStream(mSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void process() {
        mSendQueue = new LinkedBlockingQueue<>();
        mReadQueue = new LinkedBlockingQueue<>();
        mNotifyMap = new ConcurrentHashMap<>();
        mSendThread = new Thread() {
            @Override
            public void run() {
                while (!mRequireQuitMark) {
                    Message msg;
                    while ((msg = mSendQueue.poll()) != null) {
                        try {
                            msg.sendToOutputStream(out);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        mSendThread.start();

        mReadThread = new Thread() {
            @Override
            public void run() {
                while (!mRequireQuitMark) {
                    try {
                        while (in.available() > 0) {
                            Message msg = Message.obtainFromInputStream(in);
                            mReadQueue.put(msg);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mReadThread.start();

        mProcessThread = new Thread() {
            @Override
            public void run() {
                while (!mRequireQuitMark) {
                    Message msg = mReadQueue.poll();
                    if (msg != null) {
                        Object obj = mNotifyMap.put(msg.mID, msg);
                        synchronized (obj) {
                            obj.notify();
                        }
                    }
                }
            }
        };
        mProcessThread.start();
    }

    public static Message send(Message msg, long mills) throws InterruptedException {
        mSendQueue.put(msg);
        byte[] obj = new byte[0];
        mNotifyMap.put(msg.mID, obj);
        try {
            synchronized (obj) {
                obj.wait(mills);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Object obj2 = mNotifyMap.get(msg.mID);
        if (obj2 instanceof Message) {
            Message msg2 = (Message) obj2;
            mNotifyMap.remove(msg.mID);
            return msg2;
        } else {
            mNotifyMap.remove(msg.mID);
        }
        return null;
    }

    public static byte[] send(byte[] data, long mills) throws InterruptedException {
        Message msg = Message.obtainNormalMsg(data);
        Message msg2 = send(msg, mills);
        if (msg2 != null) {
            return msg2.mData;
        }
        return null;
    }

    public static void heartBeat() throws Exception {
        Message msg = Message.getHeartBeat();
        Message msg2 = send(msg, 2000);
        if (msg2 != null && msg2.mType == Message.TYPE.ANS_HEART_BEAT) {
            return;
        }
        throw new Exception("HEART BEAT ERROR");
    }

    public static void main(String[] args) {
        int threadNum = 200;
        boolean alive = true;
        Thread[] mThreadList = new Thread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            mThreadList[i] = new MyThread();
            mThreadList[i].start();
        }
        while (alive) {
            alive = false;
            for (Thread one : mThreadList) {
                if (one.isAlive()) {
                    alive = true;
                    break;
                }
            }
            if (!mReadThread.isAlive() || !mSendThread.isAlive() || !mProcessThread.isAlive()) {
                alive = false;
            }
            if(!alive) {
                mRequireQuitMark = true;
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
