package uk.co.compendiumdev.sourceextractor.unit;

import uk.co.compendiumdev.sourceextractor.DirectoryProcessor;
import uk.co.compendiumdev.sourceextractor.MacroFilesProcessor;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.nio.file.StandardCopyOption.*;

/**
 * Created by Alan on 01/09/2015.
 */
public class ImportHereTest {

    @Test
    public void basicTest() throws IOException {

        String importFilesPath = new File(System.getProperty("user.dir"), "/src/test/resources/testdata/input/import").getAbsolutePath();

        Files.copy( new File(importFilesPath, "fileToImportTo.orig").toPath(),
                    new File(importFilesPath, "fileToImportTo.txt").toPath(),
                    REPLACE_EXISTING);

        DirectoryProcessor dp = new DirectoryProcessor(importFilesPath);
        List<String> filesToProcess = dp.findFilesMatching("^.*\\.txt$");

        MacroFilesProcessor mp = MacroFilesProcessor.with()
                .filePaths(filesToProcess)
                .macroStart("!---=")
                .macroEnd("=---!")
                .codeOutputDirectory(new File(System.getProperty("user.dir"), "/output").getAbsolutePath())
                .inputRootPath(importFilesPath)
                .processImports("true")
                .create();

        mp.generateOutputFiles();
    }

}
