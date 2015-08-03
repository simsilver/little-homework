package com.simsilver.tools;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;


/**
 */
public class TextTest extends TestCase {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    public void testShow() {
        String text = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Text.show(text);
    }
}
