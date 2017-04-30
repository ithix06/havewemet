package focus.app.havewemet;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationComparer locationComparer = getLocations();

        HashMap<LatLon, ArrayList<Long>> focusLatLons = locationComparer.getResults();

        LatLng latLatLng = new LatLng(47.6062, -122.3321);

        for (Map.Entry<LatLon, ArrayList<Long>> entry : focusLatLons.entrySet()) {
            double lat = entry.getKey().lat / 1000d;
            double lon = entry.getKey().lon / 1000d;
            String activity = entry.getKey().activity;


            LatLng latLon = new LatLng(lat, lon);
            if (activity.equals("still")) {
                mMap.addMarker(new MarkerOptions().position(latLon).title("TIMES : " + entry.getValue().size()));
            } else {
                mMap.addMarker(new MarkerOptions().position(latLon).title("TIMES : " + entry.getValue().size()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }

            latLatLng = latLon;
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLatLng));
    }

    private LocationComparer getLocations() {
        StringBuilder text = new StringBuilder();

        final String folder = Environment.getExternalStorageDirectory() + File.separator  + "havewemetoutput";
        File file = new File(folder, "results.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (Exception e) {
            Log.i(TAG, "EXCEPTION READING IN FILE: " + e);
        }

        Log.i(TAG, "LOADING: " + text.toString());
        return LocationComparer.buildFrom(text.toString());
    }
}
