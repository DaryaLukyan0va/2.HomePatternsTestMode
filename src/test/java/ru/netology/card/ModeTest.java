package ru.netology.card;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.card.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.card.DataGenerator.Registration.getUser;
public class ModeTest {

    @BeforeEach
    void setup() {
        Configuration.headless = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void registeredUser() {

        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        $("[name=login]").setValue(registeredUser.getLogin());
        $("[name=password]").setValue(registeredUser.getPassword());
        $(".button__text").click();
        $(".App_appContainer__3jRx1 h2.heading").shouldHave(matchText("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void notRegisteredUser() {
        DataGenerator.RegistrationDto notRegisteredUser = getUser("active");
        $("[name=login]").setValue(notRegisteredUser.getLogin());
        $("[name=password]").setValue(notRegisteredUser.getPassword());
        $(".button__text").click();
        $("[data-test-id='error-notification']").shouldHave(Condition.text("Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void userStatusBlocked() {
        DataGenerator.RegistrationDto blockedUser = getRegisteredUser("blocked");
        $("[name=login]").setValue(blockedUser.getLogin());
        $("[name=password]").setValue(blockedUser.getPassword());
        $(".button__text").click();
        $(".notification_visible[data-test-id=error-notification]").shouldHave(Condition.matchText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void invalidLogin() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        String wrongLogin = DataGenerator.getLogin();
        $("[name=login]").setValue(wrongLogin);
        $("[name=password]").setValue(registeredUser.getPassword());
        $(".button__text").click();
        $(".notification_visible[data-test-id=error-notification]").shouldHave(Condition.matchText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void invalidPassword() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        String wrongPassword = DataGenerator.getPassword();
        $("[name=login]").setValue(registeredUser.getLogin());
        $("[name=password]").setValue(wrongPassword);
        $(".button__text").click();
        $(".notification_visible[data-test-id=error-notification]").shouldHave(Condition.matchText("Ошибка! Неверно указан логин или пароль"));
    }
}
