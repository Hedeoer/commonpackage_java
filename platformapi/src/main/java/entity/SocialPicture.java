package entity;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialPicture {
    // 图片来源
    private String source;
    // 图片的爬取时间
    private String fetchDateTime;
    // 图片的原始链接
    private String originalUrl;
    // 图片上传oss后的链接，默认为null
    private String ossUrl;
}
