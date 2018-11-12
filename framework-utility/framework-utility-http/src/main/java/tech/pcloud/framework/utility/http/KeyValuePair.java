package tech.pcloud.framework.utility.http;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeyValuePair {
    private String name;
    private String value;
}
