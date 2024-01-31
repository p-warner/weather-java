import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URL;

public class NoaaApi implements Api {
	
    private final String DOMAIN = "https://api.weather.gov";
    private final String USER_AGENT = "Java Weather App, For learning purposes";
    
    /**
     * NOAA splits up the USA into a 2.5km square grid. We must use the lat-lng to find the gridX and 
     * gridY in addition to gridId. WIth those three things we can get a forecast.
     */
    private String gridId;
    private int gridX, gridY;
    
    
    @Override
    public boolean connect() {
        return false;
    }
    
    /**
     * Single point that will make a request.
     * 
     * @param method String an HTTP verb
     * @param endpoint Piece of URL to append to DOMAIN
     * @param payload Any payload. E.g. for POST
     * @return String The response content as a String
     * @throws IOException
     */
    private String request(String method, String endpoint, String payload) throws IOException {
    	URL obj = new URL(DOMAIN + endpoint);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod(method);
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		int responseCode = con.getResponseCode();
		
		StringBuffer response = new StringBuffer();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			
			in.close();
			
			return response.toString();

		} else {
			Logger.console(LogType.ERROR, this.getClass().getName(), "request()", "response code " + responseCode);
		}
		
		return null;
    }
    
    
    
    public String getForecast(String zip){
    	//split the zip into lat lng
        ZipCode zipCodes = new ZipCode();
        Double[] latlng = zipCodes.getLatLng(zip);

        try {
        	Logger.console(LogType.INFO, this.getClass().getName(), "getForecast()", "Getting weather for " + latlng[0] + "," + latlng[1]);
        	
        	//first get the gridX, gridY, gridId
        	//https://api.weather.gov/points/40.43,-78.43
			String response = this.request("GET", "/points/" + latlng[0] + "," + latlng[1], null);
			JSONObject json = parseSimpleJson(response);
			JSONObject child = (JSONObject) json.get("properties");

			this.gridId = child.get("gridId").toString();
			this.gridX = Integer.parseInt(child.get("gridX").toString());
			this.gridY = Integer.parseInt(child.get("gridY").toString());
			
			//Now we use id x and y in the endpoint for the actual forcast data (office is gridId)
			//https://api.weather.gov/gridpoints/{office}/{grid X},{grid Y}/forecast
			Logger.console(LogType.INFO, this.getClass().getName(), "getForecast()", "Endpoint " + "/gridpoints/" + this.gridId + "/" + this.gridX + "," + this.gridY + "/forecast");
			response = this.request("GET", "/gridpoints/" + this.gridId + "/" + this.gridX + "," + this.gridY + "/forecast", null);
			
			JSONObject forecastJson = (JSONObject) parseSimpleJson(response).get("properties");
			JSONArray periods = (JSONArray) forecastJson.get("periods");
			
			for(int i = 0; i < 6; i++) {
				JSONObject forecastToday = (JSONObject) periods.get(i);
				System.out.println(forecastToday.get("name") + ": " + forecastToday.get("detailedForecast"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return null;
    }

    /**
	 * JSON Parse
	 */
	private JSONObject parseSimpleJson(String json) {
		//json = "{\"key\":\"value\"}";
		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(json);
			JSONObject array = (JSONObject) obj;
			
			return array;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
