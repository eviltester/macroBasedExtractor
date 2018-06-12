package uk.co.compendiumdev.sourceextractor.unit;

import uk.co.compendiumdev.sourceextractor.FileUtils;
import uk.co.compendiumdev.sourceextractor.MacroFinder;
import uk.co.compendiumdev.sourceextractor.MacroRemover;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class RemoveMacrosFromSource {

    String macroStartPrefix = "!---=";
    String macroEndPrefix = "=---!";

    @Test
    public void canRemoveAllMacrosFromSource() throws IOException {

        String path = System.getProperty("user.dir") + "/src/test/resources/testdata/testFile.txt";

        File inputFile = new File(path);
        String ls = System.getProperty("line.separator");


        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        String fileText = FileUtils.readFile(inputFile);

        finder.setStringToParse(fileText.toString()).parseString();

        MacroRemover remover = new MacroRemover(finder);
        String justTheSource = remover.justTheSource();

        System.out.print(justTheSource);
    }
}
