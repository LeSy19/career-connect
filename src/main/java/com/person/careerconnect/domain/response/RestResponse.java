package com.person.careerconnect.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private String error;

    //Message có thể là array list
    private Object message;
    private T data;
}
