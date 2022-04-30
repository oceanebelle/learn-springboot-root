package com.oceanebelle.learn.mongo.db;

import org.junit.ClassRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@SpringBootTest
@ContextConfiguration(initializers = DBTest.MongoInitializer.class)
@Testcontainers
public abstract class DBTest {
    private static final String USER = "root";
    private static final String PASS = "admin";
    public static final String MONGO_SERVICE = "mongo";
    private static final int MONGO_PORT = 27017;

    @ClassRule
    public static DockerComposeContainer env = new DockerComposeContainer(new File("src/test/resources/docker-compose-db.yml"))
            .withExposedService(MONGO_SERVICE, MONGO_PORT, Wait.forListeningPort());

    static {
        env.start();
    }

    public static class MongoInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "db.servers=" + env.getServiceHost(MONGO_SERVICE, MONGO_PORT) + ":" +
                            env.getServicePort(MONGO_SERVICE, MONGO_PORT),
                    "db.name=" + USER,
                    "db.password=" + PASS,
                    "db.authDB=admin"
            );
        }
    }
}
