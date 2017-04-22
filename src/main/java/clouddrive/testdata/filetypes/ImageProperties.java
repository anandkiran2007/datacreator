package clouddrive.testdata.filetypes;

/**
 * Author : gnand
 * 
 */

import java.io.File;
import org.apache.sanselan.common.IImageMetadata;

public class ImageProperties {
	
	public IImageMetadata metadata;
	public String originalImagePath;
	public int imageWidth;
	public int imageHeight;
	public String imageType;
	public File sourceFile;
	public File destinationFile;
	public String dateTime;
	public Integer imageCordinate;

	
	public ImageProperties(
			IImageMetadata metadata,
			String originalImagePath,
			int imageWidth, 
			int imageHeight, 
			String imageType,
			File sourceFile,
			File destinationFile,
			String dateTime,
			Integer imageCordinate){
		this.metadata			= metadata;
		this.originalImagePath	= originalImagePath;
		this.imageWidth			= imageWidth;
		this.imageHeight		= imageHeight;
		this.imageType 		 	= imageType;
		this.sourceFile 		= sourceFile;
		this.destinationFile 	= destinationFile;
		this.dateTime			= dateTime;
		this.imageCordinate		= imageCordinate;
	}
	
	public IImageMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(IImageMetadata metadata) {
		this.metadata = metadata;
	}

	public String getOriginalImagePath() {
		return originalImagePath;
	}

	public void setOriginalImagePath(String originalImagePath) {
		this.originalImagePath = originalImagePath;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	public File getDestinationFile() {
		return destinationFile;
	}

	public void setDestinationFile(File destinationFile) {
		this.destinationFile = destinationFile;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public Integer getImageCordinate() {
		return imageCordinate;
	}

	public void setImageCordinate(Integer imageCordinate) {
		this.imageCordinate = imageCordinate;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
}
