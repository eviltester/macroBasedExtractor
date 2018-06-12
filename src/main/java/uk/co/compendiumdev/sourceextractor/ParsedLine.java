package uk.co.compendiumdev.sourceextractor;

public class ParsedLine {
    private int lineNumber;
    private LineType lineType;
    private String lineText;
    private String lineEnding;
    private SourceMacro theMacro;
    private String lineContentBeforeMacro;

    public ParsedLine(int lineNumber, LineType lineType, String lineText) {
        this.lineNumber = lineNumber;
        this.lineType = lineType;
        this.lineText = lineText;
        this.lineEnding="";
        theMacro = null;
        this.lineContentBeforeMacro="";
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public LineType getLineType() {
        return lineType;
    }

    public boolean isMacro(){
        return lineType.equals(LineType.MACRO);
    }

    public boolean isSourceLine(){
        return lineType.equals(LineType.SOURCE);
    }

    public String getLineText() {
        return lineText;
    }

    public void setLineType(LineType aGivenLineType) {
        lineType = aGivenLineType;
    }

    public String getLineEnding() {
        return lineEnding;
    }

    public void setLineEnding(String lineEnding) {
        this.lineEnding = lineEnding;
    }

    public void hasMacro(SourceMacro macro) {
        this.theMacro = macro;
    }

    public SourceMacro asMacro(){
        return this.theMacro;
    }

    public void setLineContentBeforeMacro(String lineContentBeforeMacro) {
        this.lineContentBeforeMacro = lineContentBeforeMacro;
    }

    public String getLineContentBeforeMacro() {
        return this.lineContentBeforeMacro;
    }

    public enum LineType {SOURCE, MACRO}
}
