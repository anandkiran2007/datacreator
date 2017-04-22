package clouddrive.testdata.generator;


/**
 * Author : gnand
 * 
 */


import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.imageio.ImageIO;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import clouddrive.testdata.filetypes.FileProperties;
import clouddrive.testdata.filetypes.FileTypes;
import clouddrive.testdata.filetypes.ImageProperties;
import clouddrive.testdata.generator.common.CommonUtils;
import clouddrive.testdata.generator.common.EnvironmentVariables;
import clouddrive.testdata.generator.common.ImageDateUpdate;
import clouddrive.testdata.generator.common.LatitudeAndLongitudeWithPincode;
import java.util.logging.Logger;

public class CreateFolders extends Thread
{
	private static final Logger LOGGER = Logger.getLogger(CreateFolders.class.getName());
	private static List<ImageProperties> values = new ArrayList<ImageProperties>();
	private static List<FileProperties> fileValues = new ArrayList<FileProperties>();
	private static int NUMBER_OF_IMAGES;
	private static int NUMBER_OF_FILES;
	private static String SAMPLE_TEXTFILE;
	private static final int IMAGES = 60;
	private static final int FILES = 40;


	public static void main(String[] args) throws ImageReadException, ParseException, InterruptedException, ExecutionException{
		LOGGER.info("Reading config property file");
		Properties prop= CommonUtils.getPropValues();
		EnvironmentVariables.BASE_IMAGES_PATH = EnvironmentVariables.USER_HOME_PATH + prop.getProperty("BASE_IMAGES_PATH").trim();
		EnvironmentVariables.OUTPUT_PATH = EnvironmentVariables.USER_HOME_PATH + prop.getProperty("OUTPUT_PATH").trim();
		EnvironmentVariables.BASE_DOCS_PATH = EnvironmentVariables.USER_HOME_PATH + prop.getProperty("BASE_DOCS_PATH").trim();
		EnvironmentVariables.NUM_PARENT_FOLDERS = Integer.parseInt(prop.getProperty("NUM_PARENT_FOLDERS").trim());
		EnvironmentVariables.NUM_SUBFOLDER_FOLDERS = Integer.parseInt(prop.getProperty("NUM_SUBFOLDER_FOLDERS").trim());
		EnvironmentVariables.NUM_FILES_IN_SUBFOLDER = Integer.parseInt(prop.getProperty("NUM_FILES_IN_SUBFOLDER").trim());
		EnvironmentVariables.IMAGE_RESIZE_REQ = prop.getProperty("IMAGE_RESIZE_REQ").trim();
		EnvironmentVariables.IMG_MIN_WIDTH = Integer.parseInt(prop.getProperty("IMG_MIN_WIDTH").trim());
		EnvironmentVariables.IMG_MIN_HEIGHT = Integer.parseInt(prop.getProperty("IMG_MIN_HEIGHT").trim());
		EnvironmentVariables.IS_FLAT_HIERARCHY = prop.getProperty("IS_FLAT_HIERARCHY").trim();
		EnvironmentVariables.IMG_MAX_WIDTH = Integer.parseInt(prop.getProperty("IMG_MAX_WIDTH").trim());
		EnvironmentVariables.IMG_MAX_HEIGHT = Integer.parseInt(prop.getProperty("IMG_MAX_HEIGHT").trim());
		EnvironmentVariables.NUM_OF_FILES_TO_BE_DYNAMIC = prop.getProperty("NUM_OF_FILES_TO_BE_DYNAMIC").trim();
		EnvironmentVariables.FILE_TYPE = prop.getProperty("FILE_TYPE").trim();
		EnvironmentVariables.FOLDER_NAME = prop.getProperty("FOLDER_NAME").trim();
		EnvironmentVariables.SUBFOLDER_NAME = prop.getProperty("SUBFOLDER_NAME").trim();
		EnvironmentVariables.DOCS_FORMAT = prop.getProperty("DOCS_FORMAT").trim();
		EnvironmentVariables.IMAGE_FORMAT = prop.getProperty("IMAGE_FORMAT").trim();
		EnvironmentVariables.IS_DYNAMIC_FILENAME = prop.getProperty("IS_DYNAMIC_FILENAME").trim();
		EnvironmentVariables.DOCS_FORMATS = EnvironmentVariables.DOCS_FORMAT.split(",");
		EnvironmentVariables.IMAGE_FORMATS = EnvironmentVariables.IMAGE_FORMAT.split(",");
		EnvironmentVariables.COORDINATES = prop.getProperty("LOCATIONS").split(",");
		if(!EnvironmentVariables.FILE_TYPE.toLowerCase().equals(FileTypes.File.toString())) {
			try {
				EnvironmentVariables.COORDINATES_LIST = LatitudeAndLongitudeWithPincode
						.getCoordinates(EnvironmentVariables.COORDINATES);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(EnvironmentVariables.FILE_TYPE.toLowerCase().equals(FileTypes.File.toString()) 
				|| (EnvironmentVariables.FILE_TYPE.toLowerCase().equals(FileTypes.Mixed.toString()))) {
			SAMPLE_TEXTFILE = CommonUtils.readFile(EnvironmentVariables.BASE_DOCS_PATH);
		}

		EnvironmentVariables.DOCS_FORMAT = prop.getProperty("DOCS_FORMAT").trim();
		try{
			createTestFolders();
		}catch(Exception ex){
			LOGGER.severe("setup issue, check the paths configure in properties file");
		}
		processInputs();
	}
	public static void createTestFolders() throws ImageReadException, ParseException {
		CommonUtils.getAllFilesUnderFolder(EnvironmentVariables.BASE_IMAGES_PATH);
		CommonUtils.createFolder(EnvironmentVariables.OUTPUT_PATH);

		for(int i=1; i <=EnvironmentVariables.NUM_PARENT_FOLDERS; i ++) {
			String folderName = EnvironmentVariables.FOLDER_NAME+"-" + i;
			CommonUtils.createFolder(EnvironmentVariables.OUTPUT_PATH+folderName);
		}

		for(int i=1; i <=EnvironmentVariables.NUM_PARENT_FOLDERS; i ++) {
			String subFoldersPath = "";
			String parentFolderName = EnvironmentVariables.FOLDER_NAME+"-"+i;
			String subFolderName = "F-" + i;
			if(EnvironmentVariables.NUM_SUBFOLDER_FOLDERS !=0) {
				for(int j=1; j <=EnvironmentVariables.NUM_SUBFOLDER_FOLDERS; j ++) {
					String folderName = subFolderName + "-"+EnvironmentVariables.SUBFOLDER_NAME+"-" + j;
					if(EnvironmentVariables.IS_FLAT_HIERARCHY.toLowerCase().equals("true")){
						subFoldersPath =  "/" + folderName;	
					} else {
						subFoldersPath = subFoldersPath + "/" + folderName;
					}
					createTestData(EnvironmentVariables.OUTPUT_PATH+parentFolderName+subFoldersPath);
				}
			}else if(EnvironmentVariables.NUM_SUBFOLDER_FOLDERS == 0 && EnvironmentVariables.NUM_FILES_IN_SUBFOLDER > 0) {
				createTestData(EnvironmentVariables.OUTPUT_PATH+parentFolderName);
			}
		}
	}

	public static void createTestData(String folderPath) throws ImageReadException, ParseException {
		try{
			CommonUtils.createSubFolder(folderPath);
			int numOfTestImages = EnvironmentVariables.IMAGE_PATHS.size();
			int numofFiles;
			if(EnvironmentVariables.NUM_OF_FILES_TO_BE_DYNAMIC.toLowerCase().equals("true") 
					&& EnvironmentVariables.NUM_FILES_IN_SUBFOLDER > 0) {
				numofFiles = CommonUtils.randInt(1,EnvironmentVariables.NUM_FILES_IN_SUBFOLDER);
			}else{
				numofFiles = EnvironmentVariables.NUM_FILES_IN_SUBFOLDER;
			}
			if(EnvironmentVariables.FILE_TYPE.toLowerCase().equals(FileTypes.Mixed.toString())){
				numberOfFileUnderEachCategory(numofFiles);
				createImages(folderPath,NUMBER_OF_IMAGES,numOfTestImages);
				createFiles(folderPath, NUMBER_OF_FILES);
			}else{
				if(EnvironmentVariables.FILE_TYPE.toLowerCase().equals(FileTypes.Image.toString())){
					createImages(folderPath,numofFiles,numOfTestImages);
				}else{
					createFiles(folderPath, numofFiles);
				}
			}
			LOGGER.info("Number of files in -" + folderPath + " = " + numofFiles);
		}catch(IOException e){
			LOGGER.info(e.getMessage());
		}
	}	

	public static void processInputs()
			throws InterruptedException, ExecutionException {
		int threads = Runtime.getRuntime().availableProcessors();
		LOGGER.info("Using = "+ threads + " threads for data generation");
		ExecutorService service = Executors.newFixedThreadPool(threads);
		List<Future<Object>> futures = new ArrayList<Future<Object>>();
		for (final ImageProperties input : values) {
			Callable<Object> callable = new Callable<Object>() {
				public Object call() throws Exception {
					BufferedImage originalImage = ImageIO.read(new File(input.getOriginalImagePath()));
					IImageMetadata metadata = Sanselan.getMetadata(new File(input.getOriginalImagePath()));
					int getImageRGB = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
					BufferedImage resizeImagePng = CommonUtils.resizeImage(originalImage,
							EnvironmentVariables.IMAGE_RESIZE_REQ,
							getImageRGB,
							input.getImageWidth(),input.getImageHeight());
					ImageIO.write(resizeImagePng, input.imageType, input.getSourceFile());
					if((!input.imageType.equals("png")) 
							&& (!input.imageType.equals("bmp"))) {
						new ImageDateUpdate().modifyCreationTime((JpegImageMetadata) metadata, 
								input.getSourceFile(), 
								input.getDestinationFile(), 
								input.getDateTime(),
								EnvironmentVariables.COORDINATES_LIST.get(input.getImageCordinate()));
						input.getSourceFile().delete(); 
					}
					resizeImagePng.flush();
					originalImage.flush();
					metadata = null;
					return null;
				}
			};
			futures.add(service.submit(callable));
		}
		for (final FileProperties input : fileValues) {
			Callable<Object> callable = new Callable<Object>() {
				public Object call() throws Exception {
					Writer writer = null;

					if(input.getFileType().equals("docx")){

					}
					if(!input.getFileType().equals("pdf")){
						try{
							writer = new BufferedWriter(new OutputStreamWriter(
									new FileOutputStream(input.getFolderLocation()+"/filename"
											+input.getDateTime()+"."+input.getFileType()), "utf-8"));
							writer.write(SAMPLE_TEXTFILE + "date" + input.getDateTime());
							writer.close();

						} catch (Exception e) {
							LOGGER.info(input.getFolderLocation()+"/"+"filename"+input.getDateTime()+"."+input.getDateTime());
							LOGGER.info("file not created"+e.toString());
						}
					}else{
						CommonUtils.pdfCreator(input.getFolderLocation()+"/"+"filename"+input.getDateTime()+"."+input.getFileType(),SAMPLE_TEXTFILE+input.getDateTime());
					}
					writer.flush();
					writer = null;
					return null;
				}
			};
			futures.add(service.submit(callable));
		}
		service.shutdown();
	}

	public static void createImages(String folderPath,double totalImages, int numOfImages) throws ImageReadException, IOException, ParseException{
		String newDate = "";
		for(int i =1; i <=totalImages; i ++){
			Integer offset = CommonUtils.randInt(-100000,-3);
			int coordinatesSize = EnvironmentVariables.COORDINATES_LIST.size();
			int coordinate = CommonUtils.randInt(0,coordinatesSize-1);
			int imageIndex = CommonUtils.randInt(1,numOfImages-1);
			String originalImagePath = EnvironmentVariables.IMAGE_PATHS.get(imageIndex);
			IImageMetadata metadata = Sanselan.getMetadata(new File(EnvironmentVariables.IMAGE_PATHS.get(imageIndex)));
			int imageFormat = CommonUtils.randInt(0,3);
			String imageType = EnvironmentVariables.IMAGE_FORMATS[imageFormat];
			int width = CommonUtils.randInt(EnvironmentVariables.IMG_MIN_WIDTH,EnvironmentVariables.IMG_MAX_WIDTH);
			int height = CommonUtils.randInt(EnvironmentVariables.IMG_MIN_HEIGHT,EnvironmentVariables.IMG_MAX_HEIGHT);
			String getDateFromImage = ImageDateUpdate.getDateTime((JpegImageMetadata) metadata, offset, offset);
			newDate = getDateFromImage.replace(":", "-");
			File sourceFile = new File(folderPath+"/"+newDate+"_"+i+"."+imageType);
			File destinationFile = new File(folderPath+"/"+newDate+"_new_"+i+"."+imageType);	
			values.add(new ImageProperties(metadata,
					originalImagePath,
					width, 
					height, 
					imageType,
					sourceFile,
					destinationFile,
					getDateFromImage,
					coordinate));
		}
	}

	public static void createFiles(String folderPath, double totalFiles) throws ImageReadException, ParseException{
		for(int i =1; i <=totalFiles; i ++){
			Integer offset = CommonUtils.randInt(-100000,-3);
			String dateBefore30Days = ImageDateUpdate.getDateTime1(offset,offset).replace(":", "-");
			String newDataValue = i+"_";
			if(EnvironmentVariables.IS_DYNAMIC_FILENAME.toLowerCase().equals("false")){
				newDataValue = i+"_";
			}else{
				newDataValue = dateBefore30Days;
			}
			int intFileType = CommonUtils.randInt(0,EnvironmentVariables.DOCS_FORMATS.length-1);
			String fileType = EnvironmentVariables.DOCS_FORMATS[intFileType];

			fileValues.add(new FileProperties(folderPath,
					newDataValue,
					fileType));
		}
	}

	public static void numberOfFileUnderEachCategory(int numberOfFiles){
		NUMBER_OF_IMAGES = getValue(numberOfFiles,IMAGES);
		NUMBER_OF_FILES = getValue(numberOfFiles,FILES);
	}

	public static int getValue(int actualValue,int percentage){
		double value = (double) actualValue / 100;
		return (int) (value*percentage);
	}
}
