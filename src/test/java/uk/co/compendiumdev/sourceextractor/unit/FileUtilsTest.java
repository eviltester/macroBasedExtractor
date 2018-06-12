package uk.co.compendiumdev.sourceextractor.unit;

import uk.co.compendiumdev.sourceextractor.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileUtilsTest {

    @Test
    public void canReadATestFile() throws IOException {
        String path = System.getProperty("user.dir") + "/src/test/resources/testdata/unitTestFile.txt";

        File file = new File(path);
        String ls = System.getProperty("line.separator");

        String expected = "line1" + ls + "line2" + ls;

        assertThat(FileUtils.readFile(file), is(expected));
    }
}
