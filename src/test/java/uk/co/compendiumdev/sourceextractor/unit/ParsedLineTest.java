package uk.co.compendiumdev.sourceextractor.unit;

import uk.co.compendiumdev.sourceextractor.ParsedLine;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * parsed line represents a line in a parsed input
 * lineNumber, lineType(Source,Macro), lineText
 */
public class ParsedLineTest {

    @Test
    public void createParsedMacroLine(){
        ParsedLine pl = new ParsedLine(1, ParsedLine.LineType.MACRO, "getThisMacro(5)");
        assertEquals(1, pl.getLineNumber());
        assertEquals(ParsedLine.LineType.MACRO, pl.getLineType());
        assertEquals("getThisMacro(5)", pl.getLineText());
    }

    @Test
    public void createParsedSourceLine(){
        ParsedLine pl = new ParsedLine(2, ParsedLine.LineType.SOURCE, "     // this is a line of source");
        assertEquals(2, pl.getLineNumber());
        assertEquals(ParsedLine.LineType.SOURCE, pl.getLineType());
        assertEquals("     // this is a line of source", pl.getLineText());
    }

}
