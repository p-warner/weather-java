
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class ZipCode {
    private final boolean DEBUG = false;
    private final String ZIPS_FILENAME = "zips.csv";
    private HashMap<String, Double[]> zipsMap = new HashMap<String, Double[]>();

    /**
     * Load map on creation
     */
    ZipCode(){
        this.loadMap();
    }

    /**
     * Reads from zips.csv and loads the HashMap for quick lookups.
     */
    private void loadMap(){
        if(DEBUG){
            System.out.println(this.getClass().getSimpleName() + ":loadMap() Loading ZIP Map.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ZIPS_FILENAME))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null) {
                String[] raw = line.split(",");

                if(DEBUG){
                    System.out.println(this.getClass().getSimpleName() + ", Row " + row + ". Raw=" + line);
                }

                if(raw.length != 3){
                    if(DEBUG){
                        System.out.println(this.getClass().getSimpleName() + ":loadMap() row " + row +
                                ". Row length is not 3.");
                    }
                    return;
                }

                String key = raw[0];

                if(!Validate.isFiveDigits(key)){
                    if(DEBUG){
                        System.out.println(this.getClass().getSimpleName() + ":loadMap() row " + row +
                                ". Invalid ZIP.");
                    }

                    return;
                }

                Double lat, lng;
                try{
                    lat = Double.parseDouble(raw[1]);
                    lng = Double.parseDouble(raw[2]);
                }catch(Exception e){
                    System.out.println("Cannot parse lat or lng in row" + row);
                    return;
                }

                if(!Validate.isLat(lat)){
                    System.out.println("Invalid lat in " + row);
                    return;
                }

                if(!Validate.isLng(lng)){
                    System.out.println("Invalid lng in " + row);
                    return;
                }

                zipsMap.put(key, new Double[]{lat, lng});

                row++;
            }
        } catch (Exception e) {
            if(DEBUG){
                System.out.println(this.getClass().getSimpleName() + ":loadMap() Failed.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Get a lat and lng back from a ZIP.
     *
     * @param zip a proper 5 digit ZIP code
     * @return lat and lng
     */
    public Double[] getLatLng(String zip){
        if(!Validate.isFiveDigits(zip)){
            return null;
        }

        return zipsMap.get(zip);
    }

    public boolean isValidZip(String input){
        if(!Validate.isFiveDigits(input)){
            return false;
        }

        return zipsMap.containsKey(input);
    }
}
