package focus.app.havewemet;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        HashMap<LatLon, List<Long>> hashmapOne = new HashMap<LatLon, List<Long>>();
        HashMap<LatLon, List<Long>> hashmapTwo = new HashMap<LatLon, List<Long>>();
        LatLon latLon1 = new LatLon(1,2, "lul");
        LatLon latLon2 = new LatLon(1,2, "lul");

        hashmapOne.put(latLon1, new ArrayList<Long>());
        hashmapTwo.put(latLon2, new ArrayList<Long>());

        for (Map.Entry<LatLon, List<Long>> entryOne : hashmapOne.entrySet()) {

            //LEARN TO HASHCODE
            if (hashmapTwo.get(entryOne.getKey()) != null) {
                System.out.println("found");
            }
        }

        System.out.println("DONE");
        assertEquals(4, 2 + 2);
    }

    @Test
    public void kitchensink() throws Exception {
        HashMap<LatLon, List<Long>> hashmapOne = new HashMap<LatLon, List<Long>>();
        HashMap<LatLon, List<Long>> hashmapTwo = new HashMap<LatLon, List<Long>>();
        LatLon latLon1 = new LatLon(100,21232323, "sdf");
        LatLon latLon2 = new LatLon(100,21232323, "sf");

        hashmapOne.put(latLon1, new ArrayList<Long>());
        hashmapTwo.put(latLon2, new ArrayList<Long>());

        for (Map.Entry<LatLon, List<Long>> entryOne : hashmapOne.entrySet()) {

            //LEARN TO HASHCODE
            //System.out.println(entryOne.getKey());
            if (hashmapTwo.get(entryOne.getKey()) != null) {
                System.out.println("found");
            }
        }

        System.out.println("DONE");
        assertEquals(4, 2 + 2);
    }

//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + x;
//        result = prime * result + y;
//        return result;
//    }
}