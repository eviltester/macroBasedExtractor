package uk.co.compendiumdev.sourceextractor.unit;


import uk.co.compendiumdev.sourceextractor.DirectoryProcessor;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DirectoryOfFilesContainingMacrosTest {

    String macroStartPrefix = "!---=";
    String macroEndPrefix = "=---!";

    @Test
    public void findFilesMatchingAnExtension() throws IOException {

        String rootPathForData = System.getProperty("user.dir") + "/src/test/resources/testdata/input";

        DirectoryProcessor dp = new DirectoryProcessor(rootPathForData);
        List<String> filesToProcess = dp.findFilesMatching("^.*\\.txt$");

        assertThat(filesToProcess.size(),is(6));

    }

    @Test
    public void findFilesMatchingMultipleExtensions() throws IOException {

        String rootPathForData = System.getProperty("user.dir") + "/src/test/resources/testdata/input";

        DirectoryProcessor dp = new DirectoryProcessor(rootPathForData);
        List<String> filesToProcess = dp.findFilesMatching("^.*\\.txt$", "^.*\\.java$", "^.*\\.xml$");

        assertThat(filesToProcess.size(),is(7));

    }

}
