package clouddrive.testdata.generator.common;
/**
 * Author : gnand
 * 
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


public class CommonUtils {

	private static int i = 1; 
	private static final Logger LOGGER = Logger.getLogger(CommonUtils.class.getName());
	public CommonUtils comm = new CommonUtils();
	private CommonUtils() {}


	public static void createFolder(String path) {
		File file = new File(path);
		String folderName = folderName(path);
		if (!file.exists()) {
			if (file.mkdir()) {
				LOGGER.info(folderName +" = folder created successfully");
			} else {
				LOGGER.warning(folderName + "= folder is not created");
			}
		} else {
			LOGGER.info(folderName + " = folder exists");
		}
	}

	public static String folderName(String path) {
		String[] name = path.split("/");
		return name[name.length-1];
	}

	public static String folderNames(String path) {
		String[] name = path.split("/");
		String folders = "";
		for(String folderNames : name) {
			folders = folders +","+folderNames;
		}
		return folders;
	}

	public static void createSubFolder(String path) {
		File file = new File(path);
		String trimmedPath = path.replace(EnvironmentVariables.OUTPUT_PATH, "");
		String folderName = folderNames(trimmedPath);
		if (!file.exists()) {
			if (file.mkdirs()) {
				LOGGER.info(folderName + " = sub folder created successfully");
			} else {
				LOGGER.warning(folderName + " = sub folder is not created");
			}
		} else {
			LOGGER.info(folderName + " = sub folder exists");
		}
	}

	public static void getAllFilesUnderFolder(String path){
		File folder = new File(path);
		File[] files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				String name = pathname.getName().toLowerCase();
				return name.endsWith(".jpg") || name.endsWith(".jpeg") && pathname.isFile();
			}
		});
		for(File filePath: files ) {
			EnvironmentVariables.IMAGE_PATHS.add(filePath.toString());
		}
	}


	public static int randInt(int min, int max) {
		Random random = new Random();
		int randomNum = 0;
		try {
			randomNum = random.nextInt((max - min) + 1) + min;
		} catch (Exception ex){

		}
		return randomNum;
	}

	public static char randString() {
		Random r = new Random();
		String alphabet = "abcdefghijkllnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char alpha = 0;
		for (int i = 0; i < 52; i++) {
			alpha = (alphabet.charAt(r.nextInt(alphabet.length())));
		}
		return alpha;
	}

	public static Date getDate() {
		int diff = randInt(1,1000);
		Calendar cal = GregorianCalendar.getInstance();
		cal.add( Calendar.DAY_OF_YEAR, -1);
		LocalDate c = LocalDate.now().minusDays(diff);
		Date date = java.sql.Date.valueOf(c);
		return date;

	}

	public static BufferedImage resizeImage(BufferedImage originalImage, String imageResize, int type,int width, int height) {
		BufferedImage resizedImage = null;
		try{
			if(imageResize.equals("true")) {
				resizedImage = new BufferedImage(width, height, type);
			}else {
				resizedImage = new BufferedImage(originalImage.getWidth(),originalImage.getHeight(),type);
			}
			Graphics2D graphicImage = resizedImage.createGraphics();
			graphicImage.setFont(graphicImage.getFont().deriveFont(100f));
			if(imageResize.equals("true")) {
				graphicImage.drawImage(originalImage, 0, 0, EnvironmentVariables.IMG_MAX_WIDTH, EnvironmentVariables.IMG_MAX_HEIGHT, null);
			}else{
				graphicImage.drawImage(originalImage, 0, 0, originalImage.getWidth(), originalImage.getHeight(), null);	
			}
			graphicImage.setColor(Color.YELLOW);
			graphicImage.drawString("Image --" + i++, 100, 100);
			graphicImage.dispose();
		}catch(Exception ex){
			LOGGER.severe(ex.toString());
		}
		return resizedImage;
	}


	public static Date getNewDate() {
		Integer offset = randInt(-100000,-3);
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -offset);
		return  cal.getTime();
	}

	public static String getMD5(File file) {
		byte[] digest;

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			InputStream inputStream = new FileInputStream(file);

			DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
			byte[] buffer = new byte[1024];

			/*
            Read through all the bytes in the file 1024 at a time until we reach the end of the file. As these are read,
            they are added to the messageDigest by the DigestInputStream for processing later.
			 */
			while (digestInputStream.read(buffer, 0, 1024) != -1) ;

			digest = messageDigest.digest();

			inputStream.close();
			digestInputStream.close();
		} catch(IOException ioe) {
			throw new RuntimeException(ioe);
		} catch(NoSuchAlgorithmException nsae) {
			throw new RuntimeException(nsae);
		}

		return convertByteArrayToHexString(digest);
	}

	public static String convertByteArrayToHexString(byte[] arrayBytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < arrayBytes.length; i++) {
			stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return stringBuffer.toString();
	}

	public static Properties getPropValues() {
		InputStream inputStream;
		Properties prop = null;
		try {
			prop = new Properties();
			String propFileName = "config.properties";
			inputStream = new FileInputStream(propFileName);
			prop.load(inputStream);
		}catch(Exception ex){
			System.out.println("Exception" + ex.toString());
			return null;
		}
		return prop;
	}

	public static void pdfCreator(String fileName,String sampleFileText) {
		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
			document.open();
			document.add(new Paragraph("A Hello World PDF document." + sampleFileText));
			document.close();
			writer.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static String readFile(String path){
		String fileName = path + "sampleText.txt";
		String finalStr = "";
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
			while ((sCurrentLine = br.readLine()) != null) {
				finalStr = finalStr + sCurrentLine;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null){
					br.close();
				}
				if (fr != null){
					fr.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return finalStr;
	}
}
