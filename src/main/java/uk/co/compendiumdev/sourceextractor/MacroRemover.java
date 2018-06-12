package uk.co.compendiumdev.sourceextractor;

/**
 * Remove the Macros from a Finder
 */
public class MacroRemover {
    private final MacroFinder finder;

    public MacroRemover(MacroFinder finder) {
        this.finder = finder;
    }

    public String justTheSource() {

        StringBuilder source = new StringBuilder();

        for( ParsedLine aLine : finder.getParsedLines()){
            if(aLine.isMacro()){
                // ignore line
            }else{
                source.append(aLine.getLineText() +  aLine.getLineEnding());
            }
        }

        return source.toString();

    }
}
