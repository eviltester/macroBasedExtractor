package uk.co.compendiumdev.sourceextractor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectoryProcessor {


    private final File rootPath;
    private List<String> filesToFind;
    private List<String> filesToSkip;

    public DirectoryProcessor(String rootPathForData) {
        rootPath = new File(rootPathForData);
    }

    public List<String> findFilesMatching(String fileMatchingRegex) {
        this.filesToFind = new ArrayList<String>();
        this.filesToSkip = new ArrayList<String>();
        findFilesMatchingForThisFolder(rootPath, fileMatchingRegex, filesToFind, filesToSkip);

        return filesToFind;
    }

    public List<String> getSkippedFiles(){
        return filesToSkip;
    }

    public List<String> findFilesMatching(String... matchingRegexExpresssions) {

        this.filesToFind = new ArrayList<String>();
        this.filesToSkip = new ArrayList<String>();

        for(String fileMatchingRegex : matchingRegexExpresssions){
            findFilesMatchingForThisFolder(rootPath, fileMatchingRegex, filesToFind, this.filesToSkip);
        }

        return filesToFind;
    }

    private void findFilesMatchingForThisFolder(File thisFolder, String matchingEndRegex, List<String> files, List<String> filesToSkip) {

        // for each folder scan the files

        System.out.println("Scanning " + thisFolder.getName());

        File[] filesInfolder = thisFolder.listFiles();

        Map<String,File> subFolders = new HashMap<String, File>();
        Map<String,File> matchingFiles = new HashMap<String, File>();

        for(File aFile : filesInfolder){

            if(aFile.isDirectory()){
                subFolders.put(aFile.getName(),aFile);
            }else{
                String aFileName = aFile.getName();
                if(aFileName.matches(matchingEndRegex)){
                    System.out.println("Found " + aFile.getAbsolutePath());
                    matchingFiles.put(aFileName,aFile);
                    files.add(aFile.getAbsolutePath());
                }else{
                    filesToSkip.add(aFile.getAbsolutePath());
                }
            }
        }

        for(Map.Entry aFolder : subFolders.entrySet()){
            findFilesMatchingForThisFolder((File) aFolder.getValue(), matchingEndRegex, files, filesToSkip);
        }

    }


}
