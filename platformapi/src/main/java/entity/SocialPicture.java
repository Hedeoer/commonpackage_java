package entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class SocialPicture {
    private String source;
    private String fetchDateTime;
    private String originalUrl;
    private String ossUrl;
}
