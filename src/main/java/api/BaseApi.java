package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import utils.EnvProperties;

import static io.restassured.http.ContentType.JSON;

public abstract class BaseApi {

    protected RequestSpecification requestSpec() {
        EnvProperties.setUpInstance();
        return new RequestSpecBuilder()
                .setBaseUri(EnvProperties.BASE_URL + "api")
                .setContentType(JSON)
                .build();
    }
}
