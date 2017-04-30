package focus.app.havewemet;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//import com.sun.tools.javac.util.Pair;

public class LocationHistory {

    private static Integer E7 = 10000000;

    @SerializedName("locations")
    @Expose
    private List<Location> locations = null;

    public LocationHistory(List<Location> locations) {
        this.locations = locations;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
