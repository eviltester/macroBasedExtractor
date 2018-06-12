package uk.co.compendiumdev.sourceextractor.unit;


import uk.co.compendiumdev.sourceextractor.MacroFinder;
import uk.co.compendiumdev.sourceextractor.MacroProcessor;
import uk.co.compendiumdev.sourceextractor.TestDataGenerator;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ProcessMacrosTest {

    String macroStartPrefix = "!---=";
    String macroEndPrefix = "=---!";

    String ls = System.lineSeparator();

    @Test
    public void processResultsOfAMacro(){

        StringBuilder parseThis = new StringBuilder(TestDataGenerator.generateCodeWithNoMacros());

        parseThis.insert(0,"//" + macroStartPrefix + "getNextLines(5)" + macroEndPrefix + ls);

        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        finder.setStringToParse(parseThis.toString()).parseString();

        MacroProcessor processor = new MacroProcessor(finder);
        assertEquals("Expected 1 macro to process", 1, processor.countOfMacros());
        processor.processAllMacros();

        assertEquals("getNextLines(5)",processor.getMacro(0).macroCode());

        assertEquals("expected processed macro to be initial test data",
                TestDataGenerator.generateCodeWithNoMacros(),
                processor.getResultOfMacro(0));
    }


    @Test
    public void startAndEndMacrosWithOneLineOfContent(){

        StringBuilder parseThis = new StringBuilder(TestDataGenerator.generateCodeWithNoMacros());

        parseThis.insert(0,"//" + macroStartPrefix + "start(testMacro)" + macroEndPrefix + ls);

        int firstLineEnd = parseThis.indexOf(ls,0);
        int secondLineEnd = parseThis.indexOf(ls,firstLineEnd+1);

        parseThis.insert(secondLineEnd+ ls.length(),"//" + macroStartPrefix + "end(testMacro)" + macroEndPrefix + ls);


        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        finder.setStringToParse(parseThis.toString()).parseString();

        MacroProcessor processor = new MacroProcessor(finder);
        assertEquals("Expected 2 macro to process", 2, processor.countOfMacros());
        processor.processAllMacros();

        assertEquals("expected processed macro to be initial test data",
                "public void driverIsTheKey(){"+ls,
                processor.getResultOfMacro(0));
    }

    @Test
    public void startAndEndMacrosIgnoreNestedMacros(){

        StringBuilder parseThis = new StringBuilder(TestDataGenerator.generateCodeWithNoMacros());

        parseThis.insert(0,"//" + macroStartPrefix + "start(testMacro)" + macroEndPrefix + ls);

        int firstLineEnd = parseThis.indexOf(ls,0);
        int secondLineEnd = parseThis.indexOf(ls,firstLineEnd+1);
        int thirdLineEnd = parseThis.indexOf(ls,secondLineEnd+1);
        int fourthLineEnd = parseThis.indexOf(ls,thirdLineEnd+1);

        parseThis.insert(fourthLineEnd+ ls.length(),"//" + macroStartPrefix + "end(testMacro)" + macroEndPrefix + ls);

        parseThis.insert(secondLineEnd+ ls.length(),"//" + macroStartPrefix + "getNextLines(2)" + macroEndPrefix + ls);


        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        finder.setStringToParse(parseThis.toString()).parseString();

        MacroProcessor processor = new MacroProcessor(finder);
        assertEquals("Expected 3 macro to process", 3, processor.countOfMacros());
        processor.processAllMacros();

        assertEquals("expected processed macro to be initial test data",
                "public void driverIsTheKey(){" + ls +
                "WebDriver driver = new HtmlUnitDriver();" + ls +
                "driver.get(\"http://www.compendiumdev.co.uk/selenium\");" + ls,
                processor.getResultOfMacro(0));
    }

}
