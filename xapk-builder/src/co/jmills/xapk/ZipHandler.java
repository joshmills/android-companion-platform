package co.jmills.xapk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import co.jmills.xapk.model.XAPKFile;

/**
 * 
 * @author Josh Mills
 */
public class ZipHandler {

	/**
	 * Unzip the contents of the input file, to the output directory.
	 * @param inputZipFile
	 * @param outputFolder
	 */
	public static XAPKFile unZip(String inputZipFile, String outputFolder) {
		byte[] buffer = new byte[1024];
		
		try {
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}
			
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(inputZipFile));
			ZipEntry zipEntry = zipInputStream.getNextEntry();
			
			while (zipEntry != null) {
				String fileName = zipEntry.getName();
				File newFile = new File(outputFolder + File.separator + fileName);
				
				// Log the result
				System.out.println("file unzip : " + newFile.getAbsoluteFile());
				
				new File(newFile.getParent()).mkdirs();
				
				FileOutputStream fileOutputStream = new FileOutputStream(newFile);
				
				int len;
				while ((len = zipInputStream.read(buffer)) > 0) {
					fileOutputStream.write(buffer, 0, len);
				}
				
				fileOutputStream.close();
				zipEntry = zipInputStream.getNextEntry();
			}
			
			zipInputStream.closeEntry();
			zipInputStream.close();
			
			System.out.println("Done");
			
			XAPKFile loadedFile = new XAPKFile();
			loadedFile.setPath(inputZipFile);
			loadedFile.setBytes(new File(inputZipFile).length());
			return loadedFile;
			
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Zip the files in the input directory to a .obb file.
	 * @param inputFolder
	 */
	public static XAPKFile zip(String inputFolder, String packageName, int versionCode) {
		byte[] buffer = new byte[1024];
		
		File inputFile = new File(inputFolder);
		
		List<String> fileList = generateFileList(inputFolder, new File(inputFolder));
		String outputDirectory = inputFile.getParent() + File.separator + "main." + versionCode + "." + packageName + ".obb";
		
		try {
			
			FileOutputStream fileOutputStream = new FileOutputStream(outputDirectory);
			ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
			
			System.out.println("Output to Zip : " + outputDirectory);
			
			for (String file : fileList) {
				
				System.out.println("File added : " + file);
				
				ZipEntry zipEntry = new ZipEntry(file);
				zipOutputStream.putNextEntry(zipEntry);
				
				FileInputStream fileInputStream = new FileInputStream(inputFolder + File.separator + file);
				
				int len;
				while ((len = fileInputStream.read(buffer)) > 0) {
					zipOutputStream.write(buffer, 0, len);
				}
				
				fileInputStream.close();
			}
			
			zipOutputStream.closeEntry();
			zipOutputStream.close();
			
			System.out.println("Done");
			
			XAPKFile xapkFile = new XAPKFile();
			xapkFile.setPath(outputDirectory);
			xapkFile.setBytes(new File(outputDirectory).length());
			return xapkFile;
			
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			return null;
		}
	}

	private static List<String> generateFileList(String sourceFolder, File file) {
		
		List<String> fileList = new ArrayList<>();
		
		if (file.isFile()) {
			if (file.getName().compareTo(".DS_Store") != 0) {
				fileList.add(generateZipEntry(sourceFolder, file.getAbsoluteFile().toString()));	
			}
		}
		
		if (file.isDirectory()) {
			String[] subFiles = file.list();
			for (String fileName : subFiles) {
				fileList.addAll(generateFileList(sourceFolder, new File(file, fileName)));
			}
		}
		
		return fileList;
	}
	
	private static String generateZipEntry(String sourceFolder, String file) {
		return file.substring(sourceFolder.length() + 1, file.length());
	}
}
