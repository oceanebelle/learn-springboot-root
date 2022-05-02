package com.oceanebelle.learn.kafka;

import eu.rekawek.toxiproxy.model.ToxicDirection;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

public class KafkaAdminTest extends KafkaTest {

    AdminClient admin;

    @BeforeEach
    @SneakyThrows
    private void setup() throws IOException {
        Properties props = new Properties();
        toxiproxyClient.reset();

        props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, getHostPost(KafkaTest.TOXIPROXY_SERVICE, KafkaTest.TOXIPROXY_KAFKA_PORT));
        props.setProperty(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "500");

        admin = AdminClient.create(props);
    }

    @Test
    @SneakyThrows
    public void getTopics() throws InterruptedException {
        var createTopic = admin.createTopics(Collections.singleton(new NewTopic("test" + System.currentTimeMillis(), 1, (short) 1)),
                        new CreateTopicsOptions().timeoutMs(10000))
                .all();
        createTopic.get();

        var topics = admin.listTopics().names();
        System.out.println(topics.get());
    }

    @Test
    @SneakyThrows
    public void testKafkaIsUp() {

        toxiproxyClient.getProxy("kafka").toxics().timeout("kafka_timeout", ToxicDirection.UPSTREAM, 2000);

        var createTopic = admin.createTopics(Collections.singleton(new NewTopic("tweet3", 1, (short) 1)),
                new CreateTopicsOptions().timeoutMs(1000))
                .all();

        try {
            createTopic.get();
        } catch (Throwable e) {
            System.out.println(e);
        }

    }

    @AfterEach
    @SneakyThrows
    public void clean() {
        toxiproxyClient.reset();
    }

}
