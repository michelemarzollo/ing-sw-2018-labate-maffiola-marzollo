package it.polimi.se2018.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClaParserTest {

    /**
     * Tests the case in which an invalid argument is passed
     */
    @Test
    public void testParseInValidArgument() {
        ClaParser parser = new ClaParser();
        String[] args = new String[1];
        args[0] = "-server";
        parser.parse(args);
        assertFalse(parser.isCli());
        assertFalse(parser.isGui());
        assertFalse(parser.isServer());
        assertTrue(parser.isError());
        assertFalse(parser.isHelp());
    }

    /**
     * Tests the case where all the possible arguments are passed in a correct way
     */
    @Test
    public void testParseAllValidArguments() {
        ClaParser parser = new ClaParser();
        String[] args = new String[3];
        args[0] = "--config";
        args[1] = "//path";
        args[2] = "--cli";
        parser.parse(args);
        assertFalse(parser.isError());
        assertTrue(parser.isCli());
    }

    /**
     * Tests the case where all the possible arguments are passed in a correct way,
     * there is an incorrect one. The {@code isError} attribute must be true.
     * Nothing can be said about the other flags.
     */
    @Test
    public void testParseAllValidArgumentsExceptOne() {
        ClaParser parser = new ClaParser();
        String[] args = new String[4];
        args[0] = "--server";
        args[1] = "-cli";
        args[2] = "--gui";
        args[3] = "--help";
        parser.parse(args);
        assertTrue(parser.isError());
    }

    /**
     * Tests the case of a correct configuration
     */
    @Test
    public void testCorrectConfig(){
        ClaParser parser = new ClaParser();
        String[] args = new String[3];
        args[0] = "--config";
        args[1] = "\\Desktop\\User";
        args[2] = "--gui";
        parser.parse(args);
        assertEquals( "\\Desktop\\User", parser.getConfigLocation());
        assertFalse(parser.isError());
    }

    /**
     * Tests the case of a wrong configuration
     */
    @Test
    public void testInCorrectConfig(){
        ClaParser parser = new ClaParser();
        String[] args = new String[1];
        args[0] = "--config \\Desktop\\User thirdArgument";
        parser.parse(args);
        assertTrue(parser.isError());
        assertEquals(null, parser.getConfigLocation());
    }

    /**
     * Tests the case of an wrong configuration
     */
    @Test
    public void testConfigNoAddress(){
        ClaParser parser = new ClaParser();
        String[] args = new String[1];
        args[0] = "--config";
        parser.parse(args);
        assertTrue(parser.isError());
        assertEquals(null, parser.getConfigLocation());
    }
}