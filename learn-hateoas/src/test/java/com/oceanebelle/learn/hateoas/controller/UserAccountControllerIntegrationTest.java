package com.oceanebelle.learn.hateoas.controller;

import brave.Tracer;
import com.oceanebelle.learn.hateoas.controller.config.LoggingWebRequestInterceptor;
import com.oceanebelle.learn.hateoas.controller.dto.UserDTO;
import com.oceanebelle.learn.hateoas.service.UserService;
import com.oceanebelle.learn.logging.plugin.LogCaptorAppender;
import com.oceanebelle.learn.logging.test.LogHelper;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
public class UserAccountControllerIntegrationTest {

    @MockBean
    private UserService mockUserService;

    @MockBean
    private Tracer mockTracer;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        // N.B. Let Restassured manage minimum setup required for MockMvc
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        LogHelper.captureLogFor(UserAccountController.class, LoggingWebRequestInterceptor.class);
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
        assertThat(logCaptor.getLogCount()).isEqualTo(4);
        assertThat(logCaptor.getLog(2)).matches("action=ACCESS method=getAllUsers state=COMPLETE size=1 tookMs=\\d+");
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
        assertThat(logCaptor.getLogCount()).isEqualTo(4);
        assertThat(logCaptor.getLog(2)).matches("action=ACCESS method=getAllUsers state=COMPLETE size=0 tookMs=\\d+");
    }

    @Test
    public void getUser() {
        Mockito.when(mockUserService.getUser(eq("1"))).thenReturn(Optional.of(UserDTO.builder().name("one").id("1").build()));

        // TODO: Why is the hateoas json schema different between unit test and integrated test???
        given().when()
                .get("/users/1")
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON);
                // TODO: produces "_links"
                //.body(matchesJsonSchemaInClasspath("user.schema.json"));

        LogCaptorAppender logCaptor = LogHelper.getLogCaptor();
        assertThat(logCaptor.getLogCount()).isEqualTo(4);
        assertThat(logCaptor.getLog(2)).matches("action=ACCESS method=getUser state=COMPLETE found=true id=1 tookMs=\\d+");
    }

    @Test
    public void getUserNotFound() {
        Mockito.when(mockUserService.getUser(eq("1"))).thenReturn(Optional.empty());

        given().when()
                .get("/users/1")
                .then().log().all()
                .statusCode(404);

        LogCaptorAppender logCaptor = LogHelper.getLogCaptor();
        assertThat(logCaptor.getLogCount()).isEqualTo(4);
        assertThat(logCaptor.getLog(2)).matches("action=ACCESS method=getUser state=COMPLETE found=false id=1 tookMs=\\d+");
    }

}
