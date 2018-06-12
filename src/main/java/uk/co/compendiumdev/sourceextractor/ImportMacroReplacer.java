package uk.co.compendiumdev.sourceextractor;

public class ImportMacroReplacer {

    private final MacroFinder finder;

    public ImportMacroReplacer(MacroFinder finder) {
        this.finder = finder;
    }

    public String replaceAllImports() {

        StringBuilder source = new StringBuilder();

        boolean processingAnImport = false;
        String importFileContents = "";

        // if it is an importHere then output a startImportHere, the text, then a stopImportHere
        // if it is a startImportHere then skip until we find a stopImportHere then output a startImportHere, the text, then a stopImportHere

        for( ParsedLine aLine : finder.getParsedLines()){
            if(aLine.isMacro()){
                if(aLine.asMacro().isInputMacro()){

                    // if it is importHere then it is easy
                    if("importhere".contentEquals(aLine.asMacro().getCommand().toLowerCase())){
                        importFileContents = aLine.asMacro().executeAgainst(finder.getParsedLines());
                        outputStartEndImportHereBlock(source, aLine, importFileContents);
                        importFileContents = "";
                    }

                    // if it is startImportHere then
                    if("startimporthere".contentEquals(aLine.asMacro().getCommand().toLowerCase())){
                        processingAnImport = true;
                        importFileContents = aLine.asMacro().executeAgainst(finder.getParsedLines());

                    }
                    // do the import and store the text, but don't write it out until we find a endImportHere
                    if("endimporthere".contentEquals(aLine.asMacro().getCommand().toLowerCase())){
                        processingAnImport = false;
                        outputStartEndImportHereBlock(source, aLine, importFileContents);
                        importFileContents ="";
                    }

                }
            }else{
                if(!processingAnImport) {
                    source.append(aLine.getLineText() + aLine.getLineEnding());
                }
            }
        }

        return source.toString();
    }

    private void outputStartEndImportHereBlock(StringBuilder source, ParsedLine aLine, String inputString) {
        // need to get the comment chars for the line
        source.append(  aLine.getLineContentBeforeMacro() +
                        finder.getStartPrefix() +
                        "startImportHere(" +
                        aLine.asMacro().getParams()[0] +
                        ")" +
                        finder.getEndPrefix() +
                        aLine.getLineEnding());

        source.append(inputString);

        source.append(  aLine.getLineContentBeforeMacro() +
                finder.getStartPrefix() +
                "endImportHere(" +
                aLine.asMacro().getParams()[0] +
                ")" +
                finder.getEndPrefix() +
                aLine.getLineEnding());
    }
}
