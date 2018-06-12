package uk.co.compendiumdev.sourceextractor;

import java.util.ArrayList;
import java.util.List;

public class MacroFinder {
    private String startPrefix;
    private String endPrefix;
    private String stringToParse = null;
    private Boolean hasStringBeenParsed = false;
    private String[] lines;
    private List<SourceMacro> foundMacros;
    private List<ParsedLine> parsedLines;

    private String LINE_ENDING = System.lineSeparator();
    private String fileToProcess;

    public MacroFinder(String macroStartPrefix, String macroEndPrefix) {
        this.startPrefix = macroStartPrefix;
        this.endPrefix = macroEndPrefix;
        this.fileToProcess="";
    }

    public String getStartPrefix(){
        return startPrefix;
    }

    public String getEndPrefix(){
        return endPrefix;
    }

    public MacroFinder setStringToParse(String s) {
        if(s==null)
            throw new IllegalArgumentException("String must not be null");

        this.stringToParse = s;
        hasStringBeenParsed = false;
        return this;
    }

    public int countMacros() {
        assertWeHaveParsedStringFirst();
        return foundMacros.size();
    }

    private void assertWeHaveParsedStringFirst() {
        if(!stringHasBeenParsed())
            throw new IllegalStateException("Need to parse the string first");
    }

    public int lineCount(){
        assertWeHaveParsedStringFirst();
        return lines.length;
    }

    public boolean stringHasBeenParsed() {
        return hasStringBeenParsed;
    }

    public MacroFinder parseString() {
        lines = stringToParse.split(LINE_ENDING);
        foundMacros = new ArrayList<SourceMacro>();
        parsedLines = new ArrayList<ParsedLine>();

        int lineCount = 1;
        for(String aline : lines){
            ParsedLine parsedLine = new ParsedLine(lineCount, ParsedLine.LineType.SOURCE,aline);

            String [] macros = aline.split(this.startPrefix);
            // 0 code before macro, 1 macro (including code after macro, 2 macro, 3 macro
            // assume 1 macro per line for now
            parsedLine.setLineContentBeforeMacro(macros[0]);


            for(int macroLinePartCount = 0; macroLinePartCount<macros.length; macroLinePartCount++ ){

                if(macroLinePartCount>0){ // skip the code before the macro
                    String []macroBody = macros[macroLinePartCount].split(this.endPrefix);
                    String foundMacro = macroBody[0];
                    SourceMacro macro = new SourceMacro(foundMacro,lineCount);
                    foundMacros.add(macro);
                    macro.isInFile(fileToProcess);
                    parsedLine.setLineType(ParsedLine.LineType.MACRO);
                    parsedLine.hasMacro(macro);
                }
            }

            parsedLine.setLineEnding(LINE_ENDING);

            if(lineCount==lines.length)
                parsedLine.setLineEnding("");

            parsedLines.add(parsedLine);
            lineCount++;
        }

        hasStringBeenParsed = true;
        return this;
    }

    public SourceMacro getMacro(int macroIndex) {
        assertWeHaveParsedStringFirst();
        return foundMacros.get(macroIndex);
    }

    public List<SourceMacro> getFoundMacros(){
        assertWeHaveParsedStringFirst();
        return foundMacros;
    }

    public List<ParsedLine> getParsedLines() {
        assertWeHaveParsedStringFirst();
        return parsedLines;
    }

    public void processingFile(String fileToProcess) {
        this.fileToProcess = fileToProcess;
    }
}
