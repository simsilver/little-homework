package com.simsilver.tools;

import junit.framework.TestCase;

import org.omg.CORBA.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 */
public class DictionaryTest extends TestCase {
    private Dictionary dict;
    private File mDataFile;
    private Random mRandom;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dict = new Dictionary();
        mRandom = new Random(System.nanoTime());
        mDataFile = new File(Utils.getPath("/"), "AreaCode.txt");
        if(!mDataFile.exists()) {
            mDataFile = new File(Utils.getPath("/AreaCode.txt"));
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        dict = null;
    }

    public void testSize() {
        dict.reset();
        assertEquals(dict.size(), 0);
        dict.insert("apple", "苹果");
        assertEquals(dict.size(), 1);
    }

    public void testReset() {
        dict.reset();
        assertEquals(dict.size(), 0);
    }

    private void insertCount(int count) {
        for (int i = 0; i < count; i++) {
            dict.insert(Utils.getMD5(String.format("%09d", i)), Integer.toString(i));
        }
    }
    public void testInsert() {
        dict.reset();
        int count = mRandom.nextInt(500) + 200;
        insertCount(count);
        assertEquals(dict.size(), count);
    }

    public void testSpeed() {
        assertEquals(true, mDataFile.exists());
        int bigCount = 50000;
        insertCount(bigCount);
        long time1 = System.nanoTime();
        String[] out = dict.getValue("6");
        long time2 = System.nanoTime();
        int ms = (int) ((time2 - time1) / 1000000);
        assertEquals("Found " + out.length + " Cost Time " + ms, true, ms < bigCount / 2500 + out.length / 1000);
    }

    public void testGetValues() {
        dict.reset();
        dict.insert("apple", "苹果");
        dict.insert("aspect", "方面");
        dict.insert("banana", "香蕉");
        String[] values = dict.getValue("a");
        assertNotNull(values);
        assertEquals(2, values.length);
        if ("苹果".equals(values[0])) {
            assertEquals("方面", values[1]);
        } else {
            assertEquals("苹果", values[1]);
        }
        String[] values2 = dict.getValue("c");
        assertNull(values2);
    }
}
