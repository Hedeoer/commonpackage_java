package cn.hedeoer.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseParseResult {
    @JsonProperty(value = "meta")
    private List<MetaColumn> MetaColumns;
    @JsonProperty(value = "data")
    private List<Measurement> Measurements;

    public ResponseParseResult() {
    }

    public ResponseParseResult(List<MetaColumn> metaColumns, List<Measurement> measurements) {
        MetaColumns = metaColumns;
        Measurements = measurements;
    }

    public List<MetaColumn> getMetaColumns() {
        return MetaColumns;
    }

    public void setMetaColumns(List<MetaColumn> metaColumns) {
        MetaColumns = metaColumns;
    }

    public List<Measurement> getMeasurements() {
        return Measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        Measurements = measurements;
    }

    @Override
    public String toString() {
        StringBuilder builder  = new StringBuilder();
        for (MetaColumn metaColumn : this.MetaColumns) {
            builder.append(metaColumn.toString());
        }

        for (Measurement measurement : Measurements) {
            builder.append(measurement.toString());
        }
        return builder.toString();
    }
}
