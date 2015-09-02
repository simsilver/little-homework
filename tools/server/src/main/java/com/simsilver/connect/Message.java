package com.simsilver.connect;

import com.simsilver.tools.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 */
public class Message {

    public enum TYPE {
        HEART_BEAT(10),
        ANS_HEART_BEAT(11),
        MAX_RESERVED_TYPE(1000),
        NORMAL(MAX_RESERVED_TYPE.ID + 1),
        NORMAL2(selfInc()),
        NORMAL3(selfInc()),
        NORMAL4(selfInc()),
        NORMAL5(selfInc());

        public final int ID;
        public static final TYPE[] M_LIST;
        private static int mStaticTypeID = 0;
        private static HashMap<Integer, Integer> mMap;

        static {
            mMap = new HashMap<>();
            M_LIST = TYPE.values();
            for (TYPE t : M_LIST) {
                mMap.put(t.ID, t.ordinal());
            }
        }

        TYPE(int i) {
            ID = i;
            setInc(i);
        }

        private static int selfInc() {
            return mStaticTypeID + 1;
        }

        private static void setInc(int id) {
            assert (id > mStaticTypeID);
            mStaticTypeID = id;
        }

        public static TYPE getById(int id) {
            return M_LIST[mMap.get(id)];
        }
    }

    public final byte[] mData;
    public final long mID;
    public final TYPE mType;
    private long mThreadID;
    private static long mStaticID;

    private static Logger logger = LoggerFactory.getLogger(Message.class);

    private Message(long id, TYPE type, byte[] data) {
        mID = id;
        mThreadID = Thread.currentThread().getId();
        mType = type;
        mData = data;
    }

    public static Message getHeartBeat() {
        String title = "Heart Beats at " + Calendar.getInstance().getTime().toString();
        Message msg = new Message(increaseID(), TYPE.HEART_BEAT, title.getBytes());
        msg.mThreadID = Thread.currentThread().getId();
        return msg;
    }

    public static Message obtainFromInputStream(DataInputStream in) throws IOException {
        long id = in.readLong();
        int type = in.readInt();
        long threadID = in.readLong();
        int len = in.readInt();
        byte[] data = Utils.readInputStream(in, len);
        Message msg = new Message(id, TYPE.getById(type), data);
        msg.mThreadID = threadID;
        return msg;
    }

    public void sendToOutputStream(DataOutputStream out) throws IOException {
        out.writeLong(mID);
        out.writeInt(mType.ID);
        out.writeLong(mThreadID);
        out.writeInt(mData.length);
        out.write(mData);
    }

    public static Message obtainNormalMsg(byte[] data) {
        Message msg2 = new Message(increaseID(), TYPE.NORMAL, data);
        return msg2;
    }

    private static synchronized long increaseID() {
        return mStaticID++;
    }

    public Message replyMsg(byte[] data) {
        Message msg;
        switch (mType) {
            case HEART_BEAT:
                msg = new Message(mID, TYPE.ANS_HEART_BEAT, data);
                break;
            default:
                msg = new Message(mID, TYPE.NORMAL, data);
                break;
        }
        return msg;
    }
}
