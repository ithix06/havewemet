package focus.app.havewemet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activity_ {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("confidence")
    @Expose
    private Integer confidence;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

}
