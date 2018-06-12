package uk.co.compendiumdev.sourceextractor.cucumber;

import org.junit.Assert;
import uk.co.compendiumdev.sourceextractor.DirectoryProcessor;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import uk.co.compendiumdev.sourceextractor.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProcessMacrosStepDefinitions {

    String parseThis="";
    String macroStartPrefix="";
    String macroEndPrefix="";

    MacroProcessor processor;
    private String outputPath;
    private ArrayList<String> matchingRegexes;
    private String inputDirectory;
    private ArrayList<String> additionalFiles;

    @Given("^an input file which contains the following text$")
    public void an_input_file_which_contains_the_following_text(String fileText) throws Throwable {
        parseThis = fileText;
    }

    @When("^the macro prefix is \"([^\"]*)\"$")
    public void the_macro_prefix_is(String startPrefix) throws Throwable {
        macroStartPrefix = startPrefix;
    }

    @When("^the macro postfix is \"([^\"]*)\"$")
    public void the_macro_postfix_is(String endPostfix) throws Throwable {
        macroEndPrefix = endPostfix;
    }

    @Then("^the output of processing the macro is:$")
    public void the_output_of_processing_the_macro_is(String expectedResult) throws Throwable {

        MacroFinder finder = new MacroFinder(macroStartPrefix,macroEndPrefix);
        finder.setStringToParse(parseThis.toString()).parseString();

        processor = new MacroProcessor(finder);
        processor.processAllMacros();

        // quick hack to get running on mac when written on windows - no time to investigate
        Assert.assertEquals("expected processed macro to be correct",
                expectedResult.replace("\r\n", System.lineSeparator() ),
                processor.getResultOfMacro(0).replace("\r\n", System.lineSeparator()));
    }

    @And("^the macro output name is \\\"([^\\\"]*)\\\"$")
    public void the_macro_output_name_is(String outputName) throws Throwable {
        Assert.assertEquals(processor.getMacro(0).getOutputName(), outputName);
    }


    @Given("^a file path of \"([^\"]*)\"$")
    public void a_file_path_of(String fileNameInResourcesDir) throws Throwable {

        File inputFile = new File(System.getProperty("user.dir") + "/src/test/resources/", fileNameInResourcesDir);
        parseThis = FileUtils.readFile(inputFile);

    }

    @Given("^an input directory path of \"([^\"]*)\"$")
    public void an_input_directory_path_of(String inputDir) throws Throwable {

        inputDirectory = System.getProperty("user.dir") + "/src/test/resources/" + inputDir;
    }

    @Given("^an output file path of \"([^\"]*)\"$")
    public void an_output_file_path_of(String outputFilePath) throws Throwable {
        outputPath = System.getProperty("user.dir") + "/src/test/resources/" + outputFilePath;
    }

    @Given("^an extra file of \"([^\"]*)\"$")
    public void an_extra_file_of(String filePath) throws Throwable {
        if(additionalFiles == null){
            additionalFiles = new ArrayList<String>();
        }
        additionalFiles.add(System.getProperty("user.dir") + "/src/test/resources/" + filePath);
    }

    @Given("^a file matching regular expression of \"([^\"]*)\"$")
    public void a_file_matching_regular_expression_of(String matchingFileRegex) throws Throwable {
        if(matchingRegexes == null){
            matchingRegexes = new ArrayList<String>();
        }
        matchingRegexes.add(matchingFileRegex);
    }

    @Then("^the output directory will contain the following files:$")
    public void the_output_directory_will_contain_the_following_files(DataTable fileDetails) throws Throwable {

        DirectoryProcessor dp = new DirectoryProcessor(inputDirectory);
        List<String> filesToProcess = dp.findFilesMatching(matchingRegexes.toArray(new String[matchingRegexes.size()]));

        MacroFilesProcessorBuilder mpb = MacroFilesProcessor.with()
                .filePaths(filesToProcess)
                .outputDirectory(outputPath)
                .macroStart(macroStartPrefix)
                .macroEnd(macroEndPrefix);

        mpb.filePaths(additionalFiles);

        MacroFilesProcessor mp = mpb.create();
        mp.generateOutputFiles();


        boolean processingHeader = true;
        for(List<String> fileNameDetails : fileDetails.raw()){
            if(!processingHeader){
                String fileName = fileNameDetails.get(0);

                assertThat("expected to find file " + fileName, new File(outputPath, fileName).exists(), is(true));
            }

            processingHeader=false;
        }


    }
}
