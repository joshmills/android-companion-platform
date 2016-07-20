package co.jmills.xapk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 
 * @author Josh Mills
 */
public class ZipHandler {
 
	private List<String> fileList;
	private String mInputZipFile;
	private String mOutputFolder;
	
	public ZipHandler(String inputZipFile, String outputZipFile) {
		this.mInputZipFile = inputZipFile;
		this.mOutputFolder = outputZipFile;
	}
	
	public void unZip() {
		unZip(mInputZipFile, mOutputFolder);
	}
	
	private void unZip(String inputZipFile, String outputFolder) {
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
			
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
