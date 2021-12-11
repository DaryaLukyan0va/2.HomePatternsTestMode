package ru.netology.card;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;


import java.util.Locale;

public class DataGenerator {

    private static final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void sendRequest(RegistrationDto user) {
        RestAssured.given()
                .spec(requestSpecification)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getLogin() {
        String login = faker.name().firstName();
        return login;
    }

    public static String getPassword() {
        String password = faker.internet().password();
        return password;
    }

    public static class Registration {
        private Registration() {
        }


        public static RegistrationDto getUser(String status) {
            RegistrationDto user = new RegistrationDto(getLogin(),getPassword(), status);
            return user;
        }

        public static RegistrationDto getRegisteredUser(String status) {
            RegistrationDto registeredUser = getUser(status);
            DataGenerator.sendRequest(registeredUser);
            return registeredUser;
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
