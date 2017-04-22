package clouddrive.testdata.filetypes;

/**
 * Author : gnand
 * 
 */

public class FileProperties {

	public String folderLocation;
	public String dateTime;
	public String fileType;
	
	
	public FileProperties(String folderLocation,
			String dateTime,
			String fileType) {
		this.folderLocation = folderLocation;
		this.dateTime = dateTime;
		this.fileType = fileType;
	}
	
	public String getFolderLocation() {
		return folderLocation;
	}

	public void setFolderLocation(String folderLocation) {
		this.folderLocation = folderLocation;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
