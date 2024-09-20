package customization;

/*
*
* jackson对不需要序列化的部分的处理
* 1. 标注在类上@JsonIgnoreProperties 例如 @JsonIgnoreProperties(value = { "intValue" }) 表示序列化时忽略intValue字段
* 2. 标注在字段上 @JsonIgnore
* 3. 标注某个类都不需要序列化 @JsonIgnoreType，或者配合 addMixInAnnotations方法表示某个类某种类型的字段都不需要序列化
* 4. 使用字段过滤器 @JsonFilter("myFilter")
* */
public class JacksonIgnoreProperties {
// todo 待续

}
