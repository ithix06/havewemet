package focus.app.havewemet;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activity {

    @SerializedName("timestampMs")
    @Expose
    private String timestampMs;
    @SerializedName("activities")
    @Expose
    private List<Activity_> activities = null;

    public String getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(String timestampMs) {
        this.timestampMs = timestampMs;
    }

    public List<Activity_> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity_> activities) {
        this.activities = activities;
    }

}
