package uk.co.compendiumdev.sourceextractor.unit;


import uk.co.compendiumdev.sourceextractor.main.ApplicationRunner;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

public class MainTest {

    @Ignore("only use this for debugging, it is not part of the build")
    @Test
    public void debugMain() throws IOException {
        //String []args = {"D:/Users/Alan/Documents/development/java/javaForTesters/tools/javaForTesters.properties"};
        // live test
        String []args = {"D:/Users/Alan/Documents/xp-dev/books/JavaForTesters_BookCode/tools/javaForTesters.properties"};

        ApplicationRunner.main(args);
    }

    @Ignore("only use this for debugging, it is not part of the build")
    @Test
    public void debugMainTracks() throws IOException {
        //String []args = {"D:/Users/Alan/Documents/development/java/javaForTesters/tools/javaForTesters.properties"};
        // live test
        String []args = {"D:/Users/Alan/Documents/xp-dev/eLearning/tracksApiCaseStudy/text/build/extractcode.properties"};

        ApplicationRunner.main(args);
    }

}
