package de.adorsys.aspsp.xs2a.integtest.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class Response<T> {
    String code;
    Map<String, String> header;
    T body;
}
