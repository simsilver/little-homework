package com.simsilver.connect;

import com.simsilver.tools.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 *
 */
public class Message {
    public static final int MAX_RESERVED_ID = 1000;
    public static final int MSG_NORMAL_MSG = 1001;


    public static final int RESERVED_HEART_BEAT_ID = 10;
    public static final int RESERVED_ANS_HB_ID = 11;
    public final byte[] mData;
    public final long mID;
    public final int mType;
    private long mThreadID;
    private static long mStaticID;

    private static Logger logger = LoggerFactory.getLogger(Message.class);

    private Message(long id, int type, byte[] data) {
        mID = id;
        mThreadID = Thread.currentThread().getId();
        mType = type;
        mData = data;
    }

    public static Message getHeartBeat() {
        String title = "Heart Beats at " + Calendar.getInstance().getTime().toString();
        Message msg = new Message(increaseID(), RESERVED_HEART_BEAT_ID, title.getBytes());
        msg.mThreadID = Thread.currentThread().getId();
        return msg;
    }

    public static Message obtainFromInputStream(DataInputStream in) throws IOException {
        long id = in.readLong();
        int type = in.readInt();
        long threadID = in.readLong();
        int len = in.readInt();
        byte[] data = Utils.readInputStream(in, len);
        Message msg = new Message(id, type, data);
        msg.mThreadID = threadID;
        return msg;
    }

    public void sendToOutputStream(DataOutputStream out) throws IOException {
        out.writeLong(mID);
        out.writeInt(mType);
        out.writeLong(mThreadID);
        out.writeInt(mData.length);
        out.write(mData);
    }

    public static Message obtainNormalMsg(byte[] data) {
        Message msg2 = new Message(increaseID(), MSG_NORMAL_MSG, data);
        return msg2;
    }

    private static synchronized long increaseID() {
        return mStaticID++;
    }

    public Message replyMsg() {
        Message msg;
        logger.trace("Coming Thread {} MSG", mThreadID);
        switch (mType) {
            case RESERVED_HEART_BEAT_ID:
                msg = new Message(mID, RESERVED_ANS_HB_ID, Calendar.getInstance().getTime().toString().getBytes());
                break;
            default:
                msg = new Message(mID, MSG_NORMAL_MSG, Integer.toString(mData.length).getBytes());
                break;
        }
        return msg;
    }
}
