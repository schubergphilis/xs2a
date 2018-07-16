package de.adorsys.aspsp.xs2a.integtest.model;

import lombok.Getter;

@Getter
public class TestData<T>{
    Request<T> request;
    Response response;
}
