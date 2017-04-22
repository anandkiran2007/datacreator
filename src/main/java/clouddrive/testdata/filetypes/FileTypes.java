package clouddrive.testdata.filetypes;

/**
 * Author : gnand
 * 
 */

public enum FileTypes {
	File("File"),
	Image("Image"),
	Mixed("Mixed");

	private String value;

	FileTypes(final String value) {
		this.value = value.toLowerCase();
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.getValue();
	}
}
