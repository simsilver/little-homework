package com.simsilver.connect;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

class MyThread extends Thread {
    private static Logger logger = LoggerFactory.getLogger(MyThread.class);
    @Override
    public void run() {
        Random rand = new Random(System.nanoTime());
        long lastTime = System.nanoTime();
        boolean breakMark = false;
        while (true) {
            try {
                long now = System.nanoTime();
                TimeUnit timeUnit = TimeUnit.SECONDS;
                if(now - lastTime > timeUnit.toNanos(20)) {
                    ConnectDist.heartBeat();
                    lastTime = now;
                    continue;
                }
                int type = rand.nextInt(3);
                switch (type) {
                    case 0:
                        break;
                    case 1:
                        int len = rand.nextInt(4096);
                        byte[] data = new byte[len];
                        rand.nextBytes(data);
                        byte[] dataRet = ConnectDist.send(data, 5000);
                        if(dataRet == null) {
                            logger.warn("Thread Quit for send Error");
                            breakMark = true;
                            break;
                        }
                        int len2 = Integer.parseInt(new String(dataRet));
                        if (len != len2) {
                            logger.warn("Thread Quit for Error Data");
                            breakMark = true;
                        }
                        break;
                    default:
                        break;

                }
                if(breakMark) {
                    break;
                }
                breakMark = false;
                Thread.sleep(rand.nextInt(2000));
            } catch (Exception e) {
                logger.warn("Thread " + getId() + " exception " + e.getMessage());
                break;
            }
        }
    }
}