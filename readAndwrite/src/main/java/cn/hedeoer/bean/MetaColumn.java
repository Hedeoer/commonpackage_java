package cn.hedeoer.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaColumn {

  @JsonProperty("name")
  private String columnName;
  @JsonProperty("type")
  private String columnType;

  public void MetaColumn(String columnName, String columnType) {
      this.columnName = columnName;
      this.columnType = columnType;
  }

  public void MetaColumn() {}

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    @Override
    public String toString() {
        return "MetaColumn{" +
                "columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                '}';
    }
}
