package br.poker.bot.input;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextProcessorTest {
    @Test
    public void testNullsDiff() {
        //Both Null
        String s1 = null;
        String s2 = null;

        TextProcessor tp = new TextProcessor();
        String diff = tp.getDiff(s1, s2);
        assertEquals(null, diff);

        //Both Empty Strings
        s1 = "";
        s2 = "";

        diff = tp.getDiff(s1, s2);
        assertEquals(null, diff);

        //First String null
        s1 = null;
        s2 = "a";

        diff = tp.getDiff(s1, s2);
        assertEquals("a", diff);
    }

    @Test
    public void testEqualsDiff() {
        String s1 = "line 1\n"
                + "line2\n"
                + "line3\n";

        String s2 = "line 1\n"
                + "line2\n"
                + "line3\n";


        TextProcessor tp = new TextProcessor();
        String diff = tp.getDiff(s1, s2);

        assertEquals(null, diff);
    }

    @Test
    public void testOneLineDiff() {
        String s1 = "line 1\n"
                + "line2\n"
                + "line3\n";

        String s2 = "line 1\n"
                + "line2\n"
                + "line3\n"
                + "line4";


        TextProcessor tp = new TextProcessor();
        String diff = tp.getDiff(s1, s2);

        assertEquals("line4", diff);
    }

    @Test
    public void testMultipleLinesDiff() {
        String s1 = "line 1\n"
                + "line2\n"
                + "line3\n";

        String s2 = "line 1\n"
                + "line2\n"
                + "line3\n"
                + "line4\n"
                + "This is not a joke, this is a test! \n"
                + "Haha";


        TextProcessor tp = new TextProcessor();
        String diff = tp.getDiff(s1, s2);

        assertEquals("line4\nThis is not a joke, this is a test! \nHaha", diff);
    }
}
