package uk.co.compendiumdev.sourceextractor;

import java.io.*;

public class ETML_FileHelper {

	public static String getAbsolutePathFrom(String basePath, String relativePath) {
		String []oldPath = basePath.replace("\\","/").split("/");
		String []newPath = relativePath.replace("\\","/").split("/");
		
		String absolutePath = "";
		
		int oldPathEndsAt = oldPath.length -1; // lose old filename
		int newPathStartsAt = 0;
		
		if(basePath.replace("\\", "/").endsWith("/")){
			// a directory
			oldPathEndsAt = oldPath.length;
		}
		
		while(newPath[newPathStartsAt].equals("..")){
			oldPathEndsAt -= 1;
			newPathStartsAt += 1;
		}
		
		int newPathCount = 0;
		for( ; newPathCount < oldPathEndsAt; newPathCount++){
			absolutePath += oldPath[newPathCount] + "/";
		}
		
		for( ; newPathStartsAt < newPath.length; newPathStartsAt ++){
			absolutePath += newPath[newPathStartsAt];
			
			if((newPathStartsAt+1) != newPath.length){
				absolutePath += "/";
			}
		}
		
		return absolutePath.replace("//", "/");
		
	}

	public static String asFolder(String outputFolder) {
		String outputPath=outputFolder.replace("\\", "/");
		
		if(!outputPath.endsWith("/")){
			outputPath += "/";
		}
		
		return outputPath;
	}	
	


	public static String removeFileName(String aPath) {
		String []oldPath = aPath.replace("\\","/").split("/");
		
		String absolutePath = "";
		
		int oldPathEndsAt = oldPath.length -1; // lose old filename
		
		int newPathCount = 0;
		for( ; newPathCount < oldPathEndsAt; newPathCount++){
			absolutePath += oldPath[newPathCount] + "/";
		}
		
		return absolutePath;
	}	
	
	public static String justFileName(String aPath) {
		String []oldPath = aPath.replace("\\","/").split("/");
		
		String fileName = "";
		
		return oldPath[oldPath.length-1];
	}
	
	public static String getFileNameOfSmallerImageFileIfItExists(String src, String rootImageDirectory) {
		
		long pngFileSize = 0;
		long jpgFileSize = 0;
		long gifFileSize = 0;
		
		String noExtension = src.substring(0, src.length()-3);
		
		boolean needSep=true;
		
		if(rootImageDirectory.endsWith("\\") || rootImageDirectory.endsWith("/"))
			needSep=false;
		
		if(src.startsWith("\\") || src.startsWith("/"))
			needSep =false;
		
		String sep = "/";
		
		if(!needSep)
			sep="";
		
		File file = new File(rootImageDirectory + sep + noExtension + "png");
		pngFileSize = file.length();

		file = new File(rootImageDirectory + sep + noExtension + "gif");
		gifFileSize = file.length();
		
		file = new File(rootImageDirectory + sep + noExtension + "jpg");
		jpgFileSize = file.length();
		
		String extension = "png";
		if(gifFileSize !=0){
			if(pngFileSize>gifFileSize){
				extension = "gif";
			}
		}else{
			if(jpgFileSize !=0){
				if(pngFileSize>jpgFileSize){
					extension = "jpg";
				}	
			}
		}
		
		return (noExtension + extension);
	}

}
