package focus.app.havewemet;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationComparer {

    private transient static String TAG = "locationComparer";
    static Gson GSON = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create();

    // Accuracy is how many deciamal places are used in lat lon comparisons: 4 is 10 meters
    //10 meters
    private transient static int ACCURACY = 4;

    String personOneFilePath;
    String personTwoFilePath;
    private HashMap<LatLon, ArrayList<Long>> results;

    public HashMap<LatLon, ArrayList<Long>> getResults() {
        return results;
    }

    public String serialize() {
        Log.i(TAG, "serializing");
        Log.i(TAG, new StringBuilder(this.results.size()).toString());
        return GSON.toJson(this);
    }

    public static LocationComparer buildFrom(String json) {
        return GSON.fromJson(json, LocationComparer.class);
    }

    public LocationComparer(String personOneFilePath, String personTwoFilePath, HashMap<LatLon, ArrayList<Long>> results) {
        this.personOneFilePath = personOneFilePath;
        this.personTwoFilePath = personTwoFilePath;
        this.results = results;
    }

    public LocationComparer(String personOneFilePath, String personTwoFilePath) {
        this.personOneFilePath = personOneFilePath;
        this.personTwoFilePath = personTwoFilePath;
        results = new HashMap<LatLon, ArrayList<Long>>();
    }

    public void compareLocations() throws IOException {
        Log.i(TAG, "compareLocations START");
        HashMap<LatLon, ArrayList<Long>> similarLocations = new HashMap<>();
        similarLocations = getLocationHistory(this.personOneFilePath, this.personTwoFilePath);
        Log.i(TAG, "compareLocations END with size " + similarLocations.size());
        results = similarLocations;
    }

    private HashMap<LatLon, ArrayList<Long>> getLocationHistory(String filePathOne, String filePathTwo) throws IOException {
        HashMap<LatLon, ArrayList<Long>> sharedLocationHistory = new HashMap<LatLon, ArrayList<Long>>();
        HashMap<LatLon, ArrayList<Long>> locationHistoryOne = getLocations(filePathOne);
        Log.i(TAG, "one total: " + locationHistoryOne.size());
        HashMap<LatLon, ArrayList<Long>> locationHistoryTwo = getLocations(filePathTwo);
        Log.i(TAG, "two total: " + locationHistoryTwo.size());

        for (Map.Entry<LatLon, ArrayList<Long>> entryOne : locationHistoryOne.entrySet()) {

            //Finding similar locations
            if (locationHistoryTwo.get(entryOne.getKey()) != null
                    && (entryOne.getKey().activity.equals("still") || entryOne.getKey().activity.equals("onFoot"))) {
                sharedLocationHistory.put(entryOne.getKey(), entryOne.getValue());
            }
        }

        Log.i(TAG, "shared! total: " + sharedLocationHistory.size());
        return sharedLocationHistory;
    }

    private HashMap<LatLon, ArrayList<Long>> getLocations(String filePath) throws IOException {
        HashMap<LatLon, ArrayList<Long>> sharedLocationHistory = new HashMap<LatLon, ArrayList<Long>>();
        File file = new File(filePath);

        InputStream inputStream = new FileInputStream(file);

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        reader.beginObject();
        reader.nextName();
        reader.beginArray();

        while (reader.hasNext()) {
            Location gsonLocation = GSON.fromJson(reader, Location.class);

            String latString = "" + gsonLocation.getLatitudeE7();
            String lonString = "" + gsonLocation.getLongitudeE7();
            String activity = "";
            if (gsonLocation.getActivitys() != null && gsonLocation.getActivitys().iterator().next().getActivities() != null) {
                activity = gsonLocation.getActivitys().iterator().next().getActivities().iterator().next().getType();
            }

            latString = latString.substring(0, latString.length() - ACCURACY);
            lonString = lonString.substring(0, lonString.length() - ACCURACY);

            LatLon latLon = new LatLon(Integer.parseInt(latString), Integer.parseInt(lonString), activity);

            if(sharedLocationHistory.get(latLon) == null) {
                ArrayList<Long> arrayList = new ArrayList<Long>();
                arrayList.add(Long.parseLong(gsonLocation.getTimestampMs()));
                sharedLocationHistory.put(latLon, arrayList);
            } else {
                sharedLocationHistory.get(latLon).add(Long.parseLong(gsonLocation.getTimestampMs()));
            }
        }
        reader.endArray();

        reader.endObject();
        reader.close();

        return sharedLocationHistory;
    }
}
