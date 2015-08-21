package com.simsilver.tools;

import junit.framework.TestCase;

/**
 */
public class ID18Test extends TestCase {
    private String id17, id18;
    private char id17checksum;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        id18 = "410184194910015533";
        id17 = id18.substring(0, 17);
        id17checksum = id18.charAt(17);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        id18 = null;
        id17 = null;
        id17checksum = 0;
    }

    public void testCheckValid() {
        String baseTest = ID18.checkValid(id18);
        assertEquals(id18, baseTest);
        String baseTestWrong = ID18.checkValid(id17 + '2');
        assertEquals(id18, baseTestWrong);
        assertEquals(null,  ID18.checkValid(""));
        assertEquals(null,  ID18.checkValid(id18 + id18));
    }

    public void testGetValidateCode() {
        char c = ID18.getValidateCode(id17);
        assertEquals(id17checksum, c);
    }

    public void testGenerateValidID() {
        String id = ID18.generateValidID();
        String checkedId = ID18.checkValid(id);
        assertEquals(id,checkedId);
    }

    public void testMain() {
        ID18.main(new String[] {id18});
        ID18.main(new String[] {id17});
        ID18.main(new String[] {});
        ID18.main(new String[] {"NonsenseString"});
    }
}
