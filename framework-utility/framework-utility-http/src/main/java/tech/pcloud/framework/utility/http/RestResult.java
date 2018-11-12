package tech.pcloud.framework.utility.http;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestResult<T> {
    private Integer code;
    private String message;
    private T data;
}