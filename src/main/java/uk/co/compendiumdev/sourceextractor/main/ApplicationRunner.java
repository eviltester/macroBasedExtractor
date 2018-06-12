package uk.co.compendiumdev.sourceextractor.main;

import uk.co.compendiumdev.sourceextractor.DirectoryProcessor;
import uk.co.compendiumdev.sourceextractor.MacroFilesProcessor;
import uk.co.compendiumdev.sourceextractor.MacroFilesProcessorBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ApplicationRunner {

    public static void main(String [] args) throws IOException {

        System.out.println("Supported Export Macros: start, end, startignorefor, endignorefor, getnextlines");
        System.out.println("Supported Import Macros: importhere, startimporthere, stopimporthere");

        if(args.length == 0 || args.length > 1){
            System.err.println("Only 1 argument allowed, Argument[0] must be a path to a property file\n");
            System.err.println("With the format\n");
            System.err.println("input_directory=/testdata/input\n");
            System.err.println("output_directory=/testdata/output\n");
            System.err.println("source_output_directory=/testdata/source\n");
            System.err.println("matching_files=^.*\\.txt$\n");
            System.err.println("macro_prefix=!---=\n");
            System.err.println("macro_postfix=---!\n");
            System.err.println("# do we want to execute the import macros?\n");
            System.err.println("process_imports=false\n");
            System.err.println("# do we want to execute the export macros?\n");
            System.err.println("process_exports=true\n");
            System.err.println("# do we want to output source without macros?\n");
            System.err.println("output_source_without_macros=true\n");
            System.exit(1);
        }

        String propertyFilePath = args[0];
        File propertyFile = new File(propertyFilePath);
        if(!propertyFile.exists()) {
            System.err.println("Could not find property file:");
            System.err.println(propertyFilePath + " does not exist");
            System.exit(1);
        }



        Properties properties = new Properties();
        properties.load(new FileInputStream(propertyFile));

        DirectoryProcessor dp = new DirectoryProcessor(properties.getProperty("input_directory"));
        List<String> filesToProcess = dp.findFilesMatching(properties.getProperty("matching_files"));

        // export macros
        // start, end, startignorefor, endignorefor, getnextlines
        // write the content of the macro to the outputDirectory
        // and export the files without the macros
        MacroFilesProcessorBuilder mpb = MacroFilesProcessor.with()
                .filePaths(filesToProcess)
                .outputDirectory(properties.getProperty("output_directory"))
                .macroStart(properties.getProperty("macro_prefix"))
                .macroEnd(properties.getProperty("macro_postfix"))
                .codeOutputDirectory(properties.getProperty("source_output_directory"))
                .inputRootPath(properties.getProperty("input_directory"))
                .processImports(properties.getProperty("process_imports", "false"))
                .processExports(properties.getProperty("process_exports", "true"))
                .outputWithoutMacros(properties.getProperty("output_source_without_macros", "true"));

        Set<Map.Entry<Object, Object>> values = properties.entrySet();

        // add extra files individually
        List<String> additionalFiles = new ArrayList<>();

        for(Map.Entry v : values){
            String key = (String)v.getKey();
            String value = (String)v.getValue();

            if(key.contentEquals("additionalFile")){
                additionalFiles.add(value);
            }
        }
        mpb.filePaths(additionalFiles);

        MacroFilesProcessor mp = mpb.create();

        mp.generateOutputFiles();
    }

}
