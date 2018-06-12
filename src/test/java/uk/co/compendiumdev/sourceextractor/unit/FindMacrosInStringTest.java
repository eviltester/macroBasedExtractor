package uk.co.compendiumdev.sourceextractor.unit;

import uk.co.compendiumdev.sourceextractor.MacroFinder;
import uk.co.compendiumdev.sourceextractor.ParsedLine;
import uk.co.compendiumdev.sourceextractor.TestDataGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertFalse;

public class FindMacrosInStringTest {

    String macroStartPrefix = "!---=";
    String macroEndPrefix = "=---!";
    String ls = System.lineSeparator();

    @Test
    public void cannotParseNullString(){
        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);

        try{
            finder.setStringToParse(null);
            Assert.fail("expected an illegal argument exception");
        }catch(IllegalArgumentException e){
            // ignore
        }
    }

    @Test
    public void weNeedToParseBeforeCallingParseDependantMethods(){
        StringBuilder parseThis = new StringBuilder(TestDataGenerator.generateCodeWithNoMacros());

        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        finder.setStringToParse(parseThis.toString());
        assertFalse("string should not have been parsed yet", finder.stringHasBeenParsed());

        try{
            finder.countMacros();
            fail("Should have thrown an exception on countMacros because we haven't parsed yet");
        }catch(IllegalStateException e){
            // ignore the exception
        }

        try{
            finder.lineCount();
            fail("Should have thrown an exception on lineCount because we haven't parsed yet");
        }catch(IllegalStateException e){
            // ignore the exception
        }

    }

    @Test
    public void whenNoMacrosThenCountIsZero(){

        StringBuilder parseThis = new StringBuilder(TestDataGenerator.generateCodeWithNoMacros());

        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        finder.setStringToParse(parseThis.toString()).parseString();

        assertEquals("There were no macros, count should be 0", 0, finder.countMacros());

    }

    @Test
    public void whenAMacroThenCountIsOne(){

        StringBuilder parseThis = new StringBuilder(TestDataGenerator.generateCodeWithNoMacros());
        parseThis.insert(0,"//" + macroStartPrefix + "getNextLines(5)" + macroEndPrefix + ls);

        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        finder.setStringToParse(parseThis.toString()).parseString();

        assertEquals("Expected different line count ", 6, finder.lineCount());
        assertEquals("There were macros, count should be 1", 1, finder.countMacros());
        assertEquals("getNextLines(5)",finder.getMacro(0).macroCode());

    }

    @Test
    public void foundMacrosCanTellYouLineFoundOn(){

        StringBuilder parseThis = new StringBuilder(TestDataGenerator.generateCodeWithNoMacros());
        parseThis.insert(0,"//" + macroStartPrefix + "getNextLines(5)" + macroEndPrefix + ls);

        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        finder.setStringToParse(parseThis.toString()).parseString();

        assertEquals("There were macros, count should be 1", 1, finder.countMacros());
        assertEquals("getNextLines(5)",finder.getMacro(0).macroCode());
        assertEquals("Should have been on first line", 1, (int)finder.getMacro(0).foundOnLine());

    }

    @Test
    public void finderCanReturnAListOfParsedStrings(){

        StringBuilder parseThis = new StringBuilder(TestDataGenerator.generateCodeWithNoMacros());
        parseThis.insert(0,"//" + macroStartPrefix + "getNextLines(5)" + macroEndPrefix + ls);

        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        List<ParsedLine> parsedLines =finder.setStringToParse(parseThis.toString()).parseString().getParsedLines();

        int lineCount=1;
        for(ParsedLine aParsedLine : parsedLines){
            if(lineCount==1){
                assertEquals("Expected Macro on line 1", aParsedLine.getLineType(), ParsedLine.LineType.MACRO);
            }else{
                assertEquals("Expected Source on line " + lineCount, aParsedLine.getLineType(), ParsedLine.LineType.SOURCE);
            }
            lineCount++;
        }

    }

    @Test
    public void startAndEndMacrosWithNoContent(){

        StringBuilder parseThis = new StringBuilder(TestDataGenerator.generateCodeWithNoMacros());

        parseThis.insert(0,"//" + macroStartPrefix + "end(testMacro)" + macroEndPrefix + ls);
        parseThis.insert(0,"//" + macroStartPrefix + "start(testMacro)" + macroEndPrefix + ls);


        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        finder.setStringToParse(parseThis.toString()).parseString();

        assertEquals("There were macros, count should be 2", 2, finder.countMacros());
        assertEquals("Should have been on first line", 1, (int)finder.getMacro(0).foundOnLine());
        assertEquals("Should have been on second line", 2, (int)finder.getMacro(1).foundOnLine());
        assertEquals("start(testMacro)",finder.getMacro(0).macroCode());
        assertEquals("end(testMacro)",finder.getMacro(1).macroCode());

    }

    @Test
    public void startAndEndMacrosWithOneLineOfContent(){

        StringBuilder parseThis = new StringBuilder(TestDataGenerator.generateCodeWithNoMacros());

        parseThis.insert(0,"//" + macroStartPrefix + "start(testMacro)" + macroEndPrefix + ls);

        int firstLineEnd = parseThis.indexOf(ls,0);
        int secondLineEnd = parseThis.indexOf(ls,firstLineEnd+1);

        parseThis.insert(secondLineEnd + ls.length(), "//" + macroStartPrefix + "end(testMacro)" + macroEndPrefix + ls);


        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        finder.setStringToParse(parseThis.toString()).parseString();

        assertEquals("There were macros, count should be 2", 2, finder.countMacros());
        assertEquals("Should have been on first line", 1, (int)finder.getMacro(0).foundOnLine());
        assertEquals("Should have been on third line", 3, (int)finder.getMacro(1).foundOnLine());
        assertEquals("start(testMacro)",finder.getMacro(0).macroCode());
        assertEquals("end(testMacro)",finder.getMacro(1).macroCode());

    }


}
