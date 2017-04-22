package clouddrive.testdata.generator.common;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Document;

/**
 * Author : gnand
 * This class will get the latitude and longitude values.
 */
public class LatitudeAndLongitudeWithPincode
{
	private static final Logger LOGGER = Logger.getLogger(LatitudeAndLongitudeWithPincode.class.getName());
	private static String GOOGLE_LOCATION_API = "http://maps.googleapis.com/maps/api/geocode/xml?address=";
  
  public static List<String[]> getCoordinates(String[] addresses) throws Exception{
	  List<String[]> getCoordinates = new ArrayList<String[]>();
	  for(String location: addresses){
		  getCoordinates.add(getLatLongPositions(location));
	  }
	  return getCoordinates;
  }

  public static String[] getLatLongPositions(String address) throws Exception {
    int responseCode = 0;
    String api = GOOGLE_LOCATION_API + URLEncoder.encode(address, "UTF-8") + "&sensor=true";
    URL url = new URL(api);
    HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
    httpConnection.connect();
    responseCode = httpConnection.getResponseCode();
    if(responseCode == 200) {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();;
      Document document = builder.parse(httpConnection.getInputStream());
      XPathFactory xPathfactory = XPathFactory.newInstance();
      XPath xpath = xPathfactory.newXPath();
      XPathExpression expr = xpath.compile("/GeocodeResponse/status");
      String status = (String)expr.evaluate(document, XPathConstants.STRING);
      if(status.equals("OK")) {
         expr = xpath.compile("//geometry/location/lat");
         String latitude = (String)expr.evaluate(document, XPathConstants.STRING);
         expr = xpath.compile("//geometry/location/lng");
         String longitude = (String)expr.evaluate(document, XPathConstants.STRING);
         LOGGER.info(address + "\nlatitude = " + latitude);
         LOGGER.info("longitude =" + longitude + "\n");
         return new String[] {latitude, longitude};
      }
      else {
         throw new Exception("Error from the API - response status: "+status);
      }
    }
    return null;
  }
}
