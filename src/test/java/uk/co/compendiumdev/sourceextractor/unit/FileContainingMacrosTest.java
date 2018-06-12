package uk.co.compendiumdev.sourceextractor.unit;


import uk.co.compendiumdev.sourceextractor.FileUtils;
import uk.co.compendiumdev.sourceextractor.MacroFinder;
import uk.co.compendiumdev.sourceextractor.MacroProcessor;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class FileContainingMacrosTest {

    String macroStartPrefix = "!---=";
    String macroEndPrefix = "=---!";

    @Test
    public void startAndEndMacrosIgnoreNestedMacros() throws IOException {

        String path = System.getProperty("user.dir") + "/src/test/resources/testdata/testFile.txt";

        File inputFile = new File(path);
        String ls = System.getProperty("line.separator");


        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        String fileText = FileUtils.readFile(inputFile);

        finder.setStringToParse(fileText.toString()).parseString();

        MacroProcessor processor = new MacroProcessor(finder);
        assertEquals("Expected 1 macro to process", 1, processor.countOfMacros());
        processor.processAllMacros();

        assertEquals("expected processed macro to be file test data",
        "        this.macroText = foundMacro;" + ls +
        "        this.foundOnLineNumber = lineCount;" + ls +
        "        this.parsed=false;" + ls +
        "        this.macroCommandText=\"\";" + ls
                                                    ,
                processor.getResultOfMacro(0));
    }

}
