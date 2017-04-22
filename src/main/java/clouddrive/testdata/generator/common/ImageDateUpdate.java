package clouddrive.testdata.generator.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.*;
import java.util.Calendar;
import java.util.Date;

import org.apache.sanselan.*;
import org.apache.sanselan.common.*;
import org.apache.sanselan.formats.jpeg.*;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.*;
import org.apache.sanselan.formats.tiff.constants.*;
import org.apache.sanselan.formats.tiff.write.*;
public class ImageDateUpdate {

	/**
	 * Author : gnand
	 * This class is helper for modifying the metadata of the Images
	 */

	public static String getDateTime(JpegImageMetadata metadata,Integer hourOffset, Integer minOffset) throws ImageReadException, ParseException{
		Date old_create_date;
		TiffField old_create_date_field = null;
		try{
			old_create_date_field = metadata.findEXIFValue(TiffOutputField.EXIF_TAG_CREATE_DATE);
		}catch(Exception ex) {
			//System.out.println("No Metadata found in the image, trying create new metadata");
		}
		DateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		if(old_create_date_field != null){
			old_create_date = formatter.parse(old_create_date_field.getValue().toString());
		} else {
			old_create_date = new Date();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(old_create_date);

		cal.add(Calendar.HOUR, hourOffset);
		cal.add(Calendar.MINUTE, minOffset);

		String new_date_time_orig_field = formatter.format(cal.getTime());
		return new_date_time_orig_field;

	}

	public static String getDateTime1(Integer hourOffset, Integer minOffset) throws ImageReadException, ParseException{
		Date old_create_date;
		DateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		old_create_date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(old_create_date);
		cal.add(Calendar.HOUR, hourOffset);
		cal.add(Calendar.MINUTE, minOffset);
		String new_date_time_orig_field = formatter.format(cal.getTime());
		return new_date_time_orig_field;
	}


	public boolean modifyCreationTime(JpegImageMetadata metadata, File srcFile, File dstFile, String dateTime,String[] coordinates) throws ParseException {
		OutputStream os = null;
		TiffOutputSet outputSet = null;   
		TiffImageMetadata exif = null;
		try {
			if(metadata != null){
				exif = metadata.getExif();
				outputSet = exif.getOutputSet();    
			}
			if (null == outputSet) {
				outputSet = new TiffOutputSet();
			}
			final double longitude = Double.parseDouble(coordinates[1]); 
			final double latitude = Double.parseDouble(coordinates[0]);
			outputSet.setGPSInDegrees(longitude, latitude);
			TiffOutputField new_date_time_orig_field = new TiffOutputField(TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL, TiffConstants.FIELD_TYPE_ASCII, dateTime.length(), dateTime.getBytes());
			TiffOutputField new_create_date_field = new TiffOutputField(TiffConstants.EXIF_TAG_CREATE_DATE, TiffConstants.FIELD_TYPE_ASCII, dateTime.length(), dateTime.getBytes());
			TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
			exifDirectory.removeField(TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);       
			exifDirectory.removeField(TiffConstants.EXIF_TAG_CREATE_DATE);      
			exifDirectory.add(new_date_time_orig_field);
			exifDirectory.add(new_create_date_field);
			os = new FileOutputStream(dstFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			new ExifRewriter().updateExifMetadataLossless(srcFile, bos, outputSet);
			os.close();
			os = null;
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		catch (ImageReadException e) {
			System.out.println(e.getMessage());
		}
		catch (ImageWriteException e) {
			System.out.println(e.getMessage());
		}
		finally {
			if (os != null) {
				try{
					os.close();
				}catch (IOException e){
				}
			}
		}
		return true;
	}

	// helper-methods below ------------------------------------------------------------
	public static void printExifDateProperties(IImageMetadata metadata, String additionalText){
		if (metadata instanceof JpegImageMetadata) {
			JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			System.out.println(additionalText);
			printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
			printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_CREATE_DATE);
		}
	}

	public static void printTagValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo) {
		TiffField field = jpegMetadata.findEXIFValue(tagInfo);
		if (field == null) {
			System.out.println("(" + tagInfo.name + " not found.)");
		}
		else {
			System.out.println("" + tagInfo.name + ": " + field.getValueDescription());
		}
	}

}
