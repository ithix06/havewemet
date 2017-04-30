package focus.app.havewemet;

import java.io.Serializable;

public class LatLon {
    int lat;
    int lon;
    String activity;

    public LatLon(int lat, int lon, String activity)  {
        this.lat = lat;
        this.lon = lon;
        this.activity = activity;
    }

    public int getLat() {
        return this.lat;
    }

    public int getLon() {
        return this.lon;
    }

    @Override
    public boolean equals(Object o) {
        if(this.lat == ((LatLon)o).getLat() && this.lon == ((LatLon)o).getLon()) {
            return  true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return lat * 31 + lon;
    }
}
