package com.oceanebelle.learn.hateoas.controller;

import com.oceanebelle.learn.hateoas.controller.dto.UserDTO;
import com.oceanebelle.learn.hateoas.service.UserService;
import com.oceanebelle.learn.kafka.plugin.LogCaptorAppender;
import com.oceanebelle.learn.kafka.test.LogHelper;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UserAccountControllerTest {

    @Mock
    private UserService mockUserService;

    @InjectMocks
    private UserAccountController service;

    @BeforeEach
    public void setup() {
        // N.B. Let Restassured manage minimum setup required for MockMvc
        RestAssuredMockMvc.standaloneSetup(service);
        LogHelper.captureLogFor(UserAccountController.class);
    }

    @Test
    public void getAllUsers() {
        Mockito.when(mockUserService.getUsers()).thenReturn(List.of(UserDTO.builder().name("one").id("1").build()));

        given().when()
                .get("/users")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("allusers.schema.json"));

        LogCaptorAppender logCaptor = LogHelper.getLogCaptor();
        assertThat(logCaptor.getLogCount()).isEqualTo(2);
        assertThat(logCaptor.getLog(1)).matches("action=ACCESS method=getAllUsers state=COMPLETE size=1 tookMs=\\d+");
    }

    @Test
    public void getAllNoUsers() {
        given().when()
                .get("/users")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("allusers.schema.json"));

        LogCaptorAppender logCaptor = LogHelper.getLogCaptor();
        assertThat(logCaptor.getLogCount()).isEqualTo(2);
        assertThat(logCaptor.getLog(1)).matches("action=ACCESS method=getAllUsers state=COMPLETE size=0 tookMs=\\d+");
    }

    @Test
    public void getUser() {
        Mockito.when(mockUserService.getUser(eq("1"))).thenReturn(Optional.of(UserDTO.builder().name("one").id("1").build()));

        given().when()
                .get("/users/1")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("user.schema.json"));

        LogCaptorAppender logCaptor = LogHelper.getLogCaptor();
        assertThat(logCaptor.getLogCount()).isEqualTo(2);
        assertThat(logCaptor.getLog(1)).matches("action=ACCESS method=getUser state=COMPLETE found=true id=1 tookMs=\\d+");
    }

    @Test
    public void getUserNotFound() {
        Mockito.when(mockUserService.getUser(eq("1"))).thenReturn(Optional.empty());

        given().when()
                .get("/users/1")
                .then().log().all()
                .statusCode(404);

        LogCaptorAppender logCaptor = LogHelper.getLogCaptor();
        assertThat(logCaptor.getLogCount()).isEqualTo(2);
        assertThat(logCaptor.getLog(1)).matches("action=ACCESS method=getUser state=COMPLETE found=false id=1 tookMs=\\d+");
    }

}
