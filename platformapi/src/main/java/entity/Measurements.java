package entity;


import cn.idev.excel.annotation.ExcelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Measurements {
    @ExcelProperty("城市名称")
    private String city;
    @ExcelProperty("气温")
    private Double measurement;
    @ExcelProperty("城市标识")
    private Integer cityHash;
}