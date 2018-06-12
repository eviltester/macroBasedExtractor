package uk.co.compendiumdev.sourceextractor;


import java.util.ArrayList;
import java.util.List;

public class MacroFilesProcessorBuilder {

    private boolean outputWithoutMacros;
    private boolean processExports;
    private List<String> filesToProcess;
    private String outputPath;
    private String macroStartPrefix;
    private String macroEndPrefix;
    private String codeOutputPath;
    private String inputRoot;
    private boolean processImports;

    public MacroFilesProcessorBuilder(){
        // setup the defaults
        this.filesToProcess = new ArrayList<String>();
        this.outputPath = System.getProperty("user.dir") + "/output";
        this.codeOutputPath = System.getProperty("user.dir") + "/raw_output_source_code";
        macroStartPrefix = "!---=";
        macroEndPrefix = "=---!";
        this.inputRoot="";
        this.processImports = false;
        this.processExports = true; // because our existing properties expect it to be true
        this.outputWithoutMacros = true; // because our existing properties expect it to be true
    }

    public MacroFilesProcessor create() {
        return new MacroFilesProcessor(this.filesToProcess,
                                        this.outputPath,
                                        macroStartPrefix,
                                        macroEndPrefix,
                                        codeOutputPath,
                                        inputRoot,
                                        this.processImports,
                                        this.processExports,
                                        this.outputWithoutMacros);
    }

    public MacroFilesProcessorBuilder filePaths(List<String> filesToProcess) {
        if(filesToProcess!=null) {
            this.filesToProcess.addAll(filesToProcess);
        }
        return this;
    }

    public MacroFilesProcessorBuilder outputDirectory(String outputPath) {
        this.outputPath = outputPath;
        return this;
    }


    public MacroFilesProcessorBuilder macroStart(String macroStartPrefix) {
        this.macroStartPrefix = macroStartPrefix;
        return this;
    }

    public MacroFilesProcessorBuilder macroEnd(String macroEndPrefix) {
        this.macroEndPrefix = macroEndPrefix;
        return this;
    }

    public MacroFilesProcessorBuilder codeOutputDirectory(String codeOutputPath) {
        this.codeOutputPath = codeOutputPath;
        return this;
    }

    public MacroFilesProcessorBuilder inputRootPath(String inputRootPath) {
        this.inputRoot = inputRootPath;
        return this;
    }

    public MacroFilesProcessorBuilder processImports(String property){
        if("true".equals(property)) {
            this.processImports = true;
        }else{
            this.processImports = false;
        }
        return this;
    }

    public MacroFilesProcessorBuilder processExports(String property){
        if("true".equals(property)) {
            this.processExports = true;
        }else{
            this.processExports = false;
        }
        return this;
    }

    public MacroFilesProcessorBuilder outputWithoutMacros(String property){
        if("true".equals(property)) {
            this.outputWithoutMacros = true;
        }else{
            this.outputWithoutMacros = false;
        }
        return this;
    }
}
