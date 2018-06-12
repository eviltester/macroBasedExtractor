package uk.co.compendiumdev.sourceextractor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceMacro {
    private int foundOnLineNumber;
    private String macroText;
    private boolean parsed=false;
    private String macroCommandText;
    String macroParams[];
    private String lastResult;
    private String foundInFile;

    public SourceMacro(String foundMacro, int lineCount) {
        this.macroText = foundMacro;
        this.foundOnLineNumber = lineCount;
        this.parsed=false;
        this.macroCommandText="";
        this.lastResult = "";
        this.foundInFile = "";
    }

    public String macroCode(){
        return this.macroText;
    }

    public int foundOnLine() {
        return foundOnLineNumber;
    }

    /**
     * given a list of ParsedLine(s) we should run the macro against that list
     * and return the result
     * @param parsedLines
     * @return String
     */
    public String executeAgainst(List<ParsedLine> parsedLines) {

        String result="";

        parse();

        switch(this.getCommand().toLowerCase()){
            case "getnextlines":
                result = getNextLines(parsedLines);
                break;
            case "start":
                result = getStartEnd(parsedLines);
                break;
            case "importhere":
                result = getImportHereContents();
                break;
            case "startimporthere":
                result = getStartEndImportHereContents(parsedLines);
                break;
            case "endimporthere":
                // we don't process an end macro, skip it
                break;
            case "end":
                // we don't process an end macro, skip it
                break;
            case "startignorefor":
                // we don't process a startignorefor, we handle that in situ
                break;
            case "endignorefor":
                // we don't process a endignorefor, we handle that in situ
                break;
            default:
                throw new IllegalArgumentException("Unknown Macro for executeAgainst '" + this.getCommand() + "' on line '" + this.foundOnLineNumber + "'");
        }

        lastResult = result;

        return result;
    }



    public String getLastProcessedResult(){
        return lastResult;
    }

    private String getStartEnd(List<ParsedLine> parsedLines) {
        String result = "";
        int lineCount=0;
        boolean foundEnd = false;
        boolean ignoreLinesOn = false; // we can ignore lines in situ

        for(ParsedLine currentLine : parsedLines){
            // only process lines after the macro itself
            if(lineCount>=foundOnLineNumber){

                if(currentLine.isMacro()){
                    // special case insitu macros
                    // end
                    // startignorefor
                    // endignorefor
                    String insituMacroName = currentLine.asMacro().getCommand().toLowerCase();
                    if(insituMacroName.equalsIgnoreCase("end")){
                        // if end for same argument as macro we are processing
                        if(currentLine.asMacro().getParams()[0].contentEquals(this.getParams()[0])){
                            foundEnd = true;
                           break;
                        }
                    }else{
                        if(insituMacroName.equalsIgnoreCase("startignorefor")){
                            // if we are supposed to ignore a block of code insitu
                            // then start ignore here
                            if(currentLine.asMacro().getParams()[0].contentEquals(this.getParams()[0])){
                                // if ignore blockname matches macro block name
                                ignoreLinesOn = true;
                            }
                        }else{
                            if(insituMacroName.equalsIgnoreCase("endignorefor")){
                                // if we are supposed to ignore a block of code insitu
                                // then end ignore here
                                if(currentLine.asMacro().getParams()[0].contentEquals(this.getParams()[0])){
                                    // if ignore blockname matches macro block name
                                    ignoreLinesOn = false;
                                }
                            }
                        }
                    }
                }

                if(currentLine.isMacro()){
                    // ignore nested macro lines
                }else{
                    if(!ignoreLinesOn){
                        result += parsedLines.get(lineCount).getLineText() + parsedLines.get(lineCount).getLineEnding();
                    }
                }
            }
            lineCount++;
        }

        if(ignoreLinesOn){
            System.out.println("WARNING: started ignoring lines but did not finish for macro [" + this.macroText + "] started on line " + this.foundOnLineNumber);
        }
        if(!foundEnd){
            System.out.println("WARNING: could not find end of macro [" + this.macroText + "] started on line " + this.foundOnLineNumber);
        }

        return result;
    }

    private String getNextLines(List<ParsedLine> parsedLines) {
        String result = "";
        int numberOfLines = Integer.valueOf(macroParams[0]);
        for(int lineCount=0; lineCount<parsedLines.size(); lineCount++){

            // only process lines after the macro itself
            ParsedLine currentLine = parsedLines.get(lineCount);
            if(lineCount>=foundOnLineNumber){ // && lineCount<=(foundOnLineNumber+numberOfLines))

                if(numberOfLines > 0){
                    if(currentLine.isMacro()){
                        // ignore nested macros although they are included in the line count
                    }else{
                        result += currentLine.getLineText() + currentLine.getLineEnding();
                    }
                }
                numberOfLines--;
            }
        }
        return result;
    }



    public String [] getParams(){
        if(!parsed)
            parse();

        return macroParams;
    }
    /** return the text of the command without any params or args */
    public String getCommand() {
        if(!parsed)
            parse();

        return macroCommandText;  //To change body of created methods use File | Settings | File Templates.
    }

    /** Crude, just get it done parser to handle macroName(arg1,arg2) */
    public void parse(){
        String parts[];

        if(!parsed){
            parts = this.macroText.split("\\(");
            this.macroCommandText = parts[0].trim();

            int closingBracePosition = parts[1].lastIndexOf(")");
            String theParams = parts[1].substring(0,closingBracePosition).trim();

            if(parts[1].contains(",")){
                macroParams = theParams.split(",");
                int paramIndex=0;
                for(String aParam : macroParams){
                    macroParams[paramIndex] = aParam.trim();
                    paramIndex++;
                }
            }else{
                // no param separator, must be a single param
                macroParams= new String [1];
                macroParams[0] = theParams;
            }
        }
        parsed=true;
    }

    public String getOutputName() {
        if(!parsed)
            parse();

        switch(this.getCommand().toLowerCase()){
            case "getnextlines":
                return this.getParams()[1];

            case "start":
                return this.getParams()[0];

            case "end":
            case "startignorefor":
            case "endignorefor":
            case "importhere":
            case "startimporthere":
            case "endimporthere":
                return "";

            default:
                throw new IllegalArgumentException("Unknown Macro for getOutputName '" + this.getCommand() + "' on line '" + this.foundOnLineNumber + "'");
        }
    }

    // return the name of the input file
    public String getInputName() {
        if(!parsed)
            parse();

        switch(this.getCommand().toLowerCase()){
            case "getnextlines":
            case "start":
            case "end":
            case "startignorefor":
            case "endignorefor":
                return "";

            case "importhere":
            case "startimporthere":
            case "endimporthere":
                return this.getParams()[0];

            default:
                throw new IllegalArgumentException("Unknown Macro for getInputName '" + this.getCommand() + "' on line '" + this.foundOnLineNumber + "'");
        }
    }

    public boolean isOutputMacro(){
        return !getOutputName().contentEquals("");
    }

    public boolean isInputMacro(){
        return !getInputName().contentEquals("");
    }

    private String getStartEndImportHereContents(List<ParsedLine> parsedLines) {
        int lineCount=0;
        boolean foundEnd = false;

        String result = getImportHereContents();

        for(ParsedLine currentLine : parsedLines){
            // only process lines after the macro itself
            if(lineCount>=foundOnLineNumber){

                if(currentLine.isMacro()){
                    // special case insitu macros
                    // endImportHere
                    String insituMacroName = currentLine.asMacro().getCommand().toLowerCase();
                    if(insituMacroName.equalsIgnoreCase("endimporthere")){
                        // if end for same argument as macro we are processing
                        if(currentLine.asMacro().getParams()[0].contentEquals(this.getParams()[0])){
                            foundEnd = true;
                            break;
                        }
                    }
                }
            }
            lineCount++;
        }

        if(!foundEnd){
            System.out.println("WARNING: could not find end of macro [" + this.macroText + "] started on line " + this.foundOnLineNumber);
        }

        return result;
    }

    public String getImportHereContents() {
        // read the file in the param and return its contents
        String importPath = this.getParams()[0];

        // if it has any %%valu%% then replace that with the system properties
        Pattern propertyExpansion = Pattern.compile("%%(.*)%%");
        Matcher propertyMatcher = propertyExpansion.matcher(importPath);
        if(propertyMatcher.matches()){
            String propertyName = propertyMatcher.group(1);
            String propertyValue = System.getProperty(propertyName);
            String childPathValue = importPath.replaceFirst("%%" + propertyName + "%%","");

            importPath = new File(propertyValue,childPathValue).getAbsolutePath();
        }else{
            // is it an absolute file?
            if(!new File(importPath).exists()) {
                // assume it is relative to current file
                File parentFile = new File(foundInFile);
                importPath = new File(parentFile.getParent(), importPath).getAbsolutePath();
            }
        }

        try {
            return FileUtils.readFile(new File(importPath));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not importHere '" +importPath + "' on line '" + this.foundOnLineNumber + "'", e);
        }

    }

    public void isInFile(String foundInFile) {
        this.foundInFile = foundInFile;
    }
}
