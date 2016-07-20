package co.jmills.xapk.model;

import java.io.File;

public class XAPKFile {

	private String path;
	private long bytes;
	private String packageName;
	private int versionCode;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
		
		String packageName, versionCode;
		File file = new File(this.path);
		
		packageName = this.path.substring(file.getParent().length() + 1, this.path.length());
		packageName = packageName.replace("main.", "");
		packageName = packageName.replace("patch.", "");
		versionCode = packageName.substring(0, packageName.indexOf("."));
		packageName = packageName.substring(packageName.indexOf(".") + 1);
		
		// Parse the integer
		int iVersionCode;
		try {
			iVersionCode = Integer.parseInt(versionCode);
		} catch (NumberFormatException e) {
			iVersionCode = 0;
		}
		
		setPackageName(packageName);
		setVersionCode(iVersionCode);
	}
	public long getBytes() {
		return bytes;
	}
	public void setBytes(long bytes) {
		this.bytes = bytes;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public void increaseVersionCode() {
		setVersionCode(getVersionCode() + 1);
	}
}
