package uk.co.compendiumdev.sourceextractor;

import java.util.List;

public class MacroProcessor {
    private MacroFinder finder;
    private boolean macrosHaveBeenProcessed;

    public MacroProcessor(MacroFinder finder) {
        this.finder = finder;
        macrosHaveBeenProcessed = false;

    }

    public int countOfMacros() {
        return finder.countMacros();
    }

    /**
     * for each found macro - process it
     */
    public void processAllMacros() {
        if(!finder.stringHasBeenParsed())
            finder.parseString();

        // now run through all macros and process them
        for(SourceMacro sm : finder.getFoundMacros()){
            sm.executeAgainst(finder.getParsedLines());
        }

        this.macrosHaveBeenProcessed = true;
    }

    public List<SourceMacro> getProcessedMacros(){
        if(!macrosHaveBeenProcessed)
            processAllMacros();

        return finder.getFoundMacros();
    }

    public SourceMacro getMacro(int i) {
        return finder.getMacro(i);
    }

    public String getResultOfMacro(int macroIndex) {
        SourceMacro sm = finder.getMacro(macroIndex);
        return sm.executeAgainst(finder.getParsedLines());
    }
}
