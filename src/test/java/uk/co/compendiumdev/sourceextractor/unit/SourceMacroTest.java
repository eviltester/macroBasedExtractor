package uk.co.compendiumdev.sourceextractor.unit;

import uk.co.compendiumdev.sourceextractor.ParsedLine;
import uk.co.compendiumdev.sourceextractor.SourceMacro;
import uk.co.compendiumdev.sourceextractor.TestDataGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SourceMacroTest {

    @Test
    public void createASourceMacro(){
        SourceMacro sm = new SourceMacro("aMacro(3)",4);
        assertEquals("aMacro(3)", sm.macroCode());
        assertEquals(4, sm.foundOnLine());
        assertEquals("3", sm.getParams()[0]);
    }

    @Test
    public void canParseASourceMacroWithMultipleParams(){
        SourceMacro sm = new SourceMacro("aMacro(4, 3 , 6)",4);
        assertEquals("aMacro", sm.getCommand());
        assertEquals(3, sm.getParams().length);
        assertEquals("4", sm.getParams()[0]);
        assertEquals("3", sm.getParams()[1]);
        assertEquals("6", sm.getParams()[2]);
    }

    @Test
    public void processAMacro(){
        StringBuilder sourceBuilder = new StringBuilder(TestDataGenerator.generateCodeWithNoMacros());
        sourceBuilder.insert(0, TestDataGenerator.getMacroStartPrefix() + "getNextLines(5)" + TestDataGenerator.getMacroEndPrefix());

        List<ParsedLine> lines = new ArrayList<ParsedLine>();

        lines.add(new ParsedLine(1, ParsedLine.LineType.MACRO,
                                            TestDataGenerator.getMacroStartPrefix() +
                                            "getNextLines(1)" +
                                            TestDataGenerator.getMacroEndPrefix()));
        lines.add(new ParsedLine(2, ParsedLine.LineType.SOURCE, "this is the source line"));

        SourceMacro sm = new SourceMacro("getNextLines(1)", 1);

        assertEquals("this is the source line", sm.executeAgainst(lines));
    }


}
