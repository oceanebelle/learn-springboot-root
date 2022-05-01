package com.oceanebelle.learn.mongo.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class SimpleTestContainerTest {

    private static final int MONGO_PORT = 27017;

    @Container
    private MongoDBContainer mongoDBContainer = new MongoDBContainer(
            DockerImageName.parse("mongo:4.2.6"));

    @BeforeEach
    void setup() {

        mongoDBContainer.withStartupCheckStrategy(new MinimumDurationRunningStartupCheckStrategy(Duration.ofMinutes(1)));
        mongoDBContainer.start();
    }

    @Test
    void test1() {
        System.out.print("port:");
        System.out.println(mongoDBContainer.getMappedPort(MONGO_PORT));
        assertThat(mongoDBContainer.isRunning()).isTrue();
    }

    @Test
    void test2() {
        System.out.print("port:");
        System.out.println(mongoDBContainer.getMappedPort(MONGO_PORT));
        assertThat(mongoDBContainer.isRunning()).isTrue();
    }

    @AfterEach
    void cleanup() {

        mongoDBContainer.stop();
    }

}