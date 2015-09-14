package com.simsilver.tools;

import junit.framework.TestCase;

/**
 */
public class UtilsTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetMD5() {
        String textSrc = "q4m'x68n6_YDB4ty8VC4&}wqBtn^68W";
        String textMd5 = "0c70bb931f03b75af1591f261eb77d0b";
        String out = Utils.getMD5(textSrc);
        assertEquals(textMd5, out);
    }

    public void testFindId() {
        String list = "123,23,12,1234,";
        assertEquals(true, Utils.findId("123",list));
        assertEquals(true, Utils.findId("23",list));
        assertEquals(true, Utils.findId("12",list));
        assertEquals(true, Utils.findId("1234",list));
        assertEquals(false, Utils.findId("4",list));
        assertEquals(false, Utils.findId("3",list));
        assertEquals(false, Utils.findId("2",list));
    }
}
