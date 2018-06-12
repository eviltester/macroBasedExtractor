package uk.co.compendiumdev.sourceextractor;

public class TestDataGenerator {


    private final static String macroStartPrefix = "!---=";
    private final static String macroEndPrefix = "=---!";

    public static String getMacroStartPrefix(){
        return macroStartPrefix;
    }

    public static String getMacroEndPrefix(){
        return macroEndPrefix;
    }

    public static String generateCodeWithNoMacros() {
        StringBuilder parseThis = new StringBuilder();

        String ls = System.getProperty("line.separator");

        parseThis.append("public void driverIsTheKey(){" +ls);
        parseThis.append("WebDriver driver = new HtmlUnitDriver();" +ls);
        parseThis.append("driver.get(\"http://www.compendiumdev.co.uk/selenium\");" +ls);
        parseThis.append("assertEquals(true, driver.getTitle().startsWith(\"Selenium Simplified\"));" +ls);
        parseThis.append("}");

        return parseThis.toString();
    }
}
