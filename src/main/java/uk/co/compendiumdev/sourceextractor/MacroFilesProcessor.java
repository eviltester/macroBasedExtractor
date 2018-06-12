package uk.co.compendiumdev.sourceextractor;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

public class MacroFilesProcessor {

    private final List<String> filesToProcess;
    private final String snippetsOutputPath;
    private final String macroStartPrefix;
    private final String macroEndPrefix;
    private final String codeOutputPath;
    private final String inputRoot;
    private final boolean processImports;

    // where we backup files before running import macros
    private final String backupDirectory;
    private final boolean processExports;
    private final boolean outputSourceWithoutMacros;

    public MacroFilesProcessor(List<String> filesToProcess, String snippetsOutputPath, String macroStartPrefix, String macroEndPrefix, String codeOutputPath, String inputRoot, boolean processImports, boolean processExports, boolean outputSourceWithoutMacros) {
        this.filesToProcess = filesToProcess;
        this.snippetsOutputPath = snippetsOutputPath;
        this.macroStartPrefix = macroStartPrefix;
        this.macroEndPrefix = macroEndPrefix;
        this.codeOutputPath = codeOutputPath;
        this.inputRoot = inputRoot;
        // should we process the import macros or not
        this.processImports = processImports;
        this.processExports = processExports;
        this.outputSourceWithoutMacros = outputSourceWithoutMacros;

        // create backups folder under the input root
        backupDirectory = Paths.get(inputRoot, "macroBackups", System.currentTimeMillis() + "_bak").toFile().getAbsolutePath();
    }

    public static MacroFilesProcessorBuilder with() {
        return new MacroFilesProcessorBuilder();
    }

    public void generateOutputFiles() {

        ensureOutputDirectoryExists(snippetsOutputPath);
        ensureOutputDirectoryExists(codeOutputPath);

        // create a directory for backups of the import macro files
        ensureOutputDirectoryExists(backupDirectory);

        for(String fileToProcess : filesToProcess){
            MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
            finder.processingFile(fileToProcess);

            File inputFile = new File(fileToProcess);

            String fileText="";

            fileText = readFile(fileToProcess, inputFile);

            finder.setStringToParse(fileText.toString()).parseString();

            MacroProcessor processor = new MacroProcessor(finder);
            List<SourceMacro> macros = processor.getProcessedMacros();

            if(this.processExports) {
                // extract all the output macro contents to snippetsOutputPath
                for (SourceMacro macro : macros) {

                    if (macro.isOutputMacro()) {
                        String macroResult = macro.getLastProcessedResult();
                        File outputFile = new File(snippetsOutputPath, macro.getOutputName() + ".txt");

                        writeFile(fileToProcess, macroResult, outputFile);
                    }
                }
            }

            if(this.processImports) {
                // for each file, process the import macros
                if (inputRoot.length() > 4) {

                    // get just the folders and name of file, ignoring the parent folder
                    String filenameAndPath = fileToProcess.replace(inputRoot.replace("/", "\\"), "");

                    // create a backup of the file
                    File outputSourceFile = new File(backupDirectory, filenameAndPath + ".bak");
                    ensureOutputDirectoryExists(ETML_FileHelper.removeFileName(outputSourceFile.getPath()));
                    try {
                        Files.copy(new File(fileToProcess).toPath(), outputSourceFile.toPath(), COPY_ATTRIBUTES);
                    } catch (IOException e) {
                        System.out.println("*** ERROR, could not create backup file " + outputSourceFile.getAbsolutePath());
                        throw new RuntimeException("Error creating backup file for " + fileToProcess, e);
                    }

                    ImportMacroReplacer replacer = new ImportMacroReplacer(finder);
                    outputSourceFile = new File(fileToProcess);
                    ensureOutputDirectoryExists(ETML_FileHelper.removeFileName(outputSourceFile.getPath()));
                    writeFile(fileToProcess, replacer.replaceAllImports(), outputSourceFile);
                }
            }

            if(this.outputSourceWithoutMacros) {
                // now output the files without macros to codeOutputPath
                if (inputRoot.length() > 4) {
                    // get just the folders and name of file, ignoring the parent folder
                    String filenameAndPath;

                    if(fileToProcess.startsWith(inputRoot.replace("/", "\\"))) {
                        // only output files under the source root to a sub dir
                        filenameAndPath  = fileToProcess.replace(inputRoot.replace("/", "\\"), "");
                    }else{
                        // additional files copy to root
                        filenameAndPath =  new File(fileToProcess).getName();
                    }

                    MacroRemover remover = new MacroRemover(finder);
                    File outputSourceFile = new File(codeOutputPath, filenameAndPath);
                    ensureOutputDirectoryExists(ETML_FileHelper.removeFileName(outputSourceFile.getPath()));
                    writeFile(fileToProcess, remover.justTheSource(), outputSourceFile);
                }
            }
        }
}

    private void writeFile(String fileToProcess, String macroResult, File outputFile) {
        try {
            FileUtils.writeFile(outputFile, macroResult);
        } catch (IOException e) {
            System.out.println("Could not write File " + outputFile.getAbsolutePath());
            throw new RuntimeException("Error reading file " + fileToProcess, e);
        }
    }

    private String readFile(String fileToProcess, File inputFile) {
        String fileText;
        try{
            fileText = FileUtils.readFile(inputFile);
        } catch (IOException e) {
            System.out.println("Could not read File " + fileToProcess);
            throw new RuntimeException("Error reading file " + fileToProcess, e);
        }
        return fileText;
    }

    private void ensureOutputDirectoryExists(String pathToCreate) {

        File outputDirs =  new File(pathToCreate);
        if(!outputDirs.exists())
            outputDirs.mkdirs();

    }
}
