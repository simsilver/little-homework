package com.simsilver.tools;

import junit.framework.TestCase;

/**
 */
public class UtilsTest extends TestCase {
    private String textSrc, textMd5;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        textSrc = "q4m'x68n6_YDB4ty8VC4&}wqBtn^68W";
        textMd5 = "0c70bb931f03b75af1591f261eb77d0b";
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        textSrc = null;
        textMd5 = null;
    }

    public void testGetMD5() {
        String out = Utils.getMD5(textSrc);
        assertEquals(textMd5, out);
    }

}
