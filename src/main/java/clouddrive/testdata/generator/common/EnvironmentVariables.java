package clouddrive.testdata.generator.common;
/**
 * Author : gnand
 * 
 */
import java.util.ArrayList;
import java.util.List;

public class EnvironmentVariables {
	
	private static EnvironmentVariables environmentVariables = new EnvironmentVariables();
	
	public static String BASE_IMAGES_PATH;
	public static String OUTPUT_PATH;
	public static String BASE_DOCS_PATH;
	public static int NUM_PARENT_FOLDERS;
	public static int NUM_SUBFOLDER_FOLDERS;
	public static int NUM_FILES_IN_SUBFOLDER;
	public static String NUM_OF_FILES_TO_BE_DYNAMIC;
	public static String IS_FLAT_HIERARCHY;
	public static String IMAGE_RESIZE_REQ;
	public static int IMG_MIN_WIDTH;
	public static int IMG_MIN_HEIGHT;
	public static int IMG_MAX_WIDTH;
	public static int IMG_MAX_HEIGHT;
	public static String FILE_TYPE;
	public static String FOLDER_NAME;
	public static String SUBFOLDER_NAME;
	public static final List<String> IMAGE_PATHS = new ArrayList<String>();
	public static final String USER_HOME_PATH = System.getProperty("user.home");
	public static String DOCS_FORMAT;
	public static String[] DOCS_FORMATS;
	public static String IMAGE_FORMAT;
	public static String[] IMAGE_FORMATS;
	public static String[] COORDINATES;
	public static String IS_DYNAMIC_FILENAME;
	public static List<String[]> COORDINATES_LIST;
	
	public EnvironmentVariables() {}
	

	 public static EnvironmentVariables getInstance() {
	      return environmentVariables;
	   }
	 
	 
	
}
