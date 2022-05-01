package com.oceanebelle.learn.mongo.db;

import eu.rekawek.toxiproxy.model.ToxicDirection;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class DBAdapterTest extends DBTest {

    @Autowired
    DBAdaptor adaptor;

    @Test
    @SneakyThrows
    public void shouldReturnVersions() throws IOException {
        assertThat(adaptor.getVersion()).isEqualTo("4.2.6");

        toxiproxyClient.getProxy(MONGO_SERVICE).toxics().latency("mongo_latency", ToxicDirection.UPSTREAM, 60000);

        assertThat(adaptor.getVersion()).isEqualTo("4.2.6");

    }

    @AfterEach
    @SneakyThrows
    public void reset() {
        toxiproxyClient.getProxy(MONGO_SERVICE).toxics().getAll().clear();
    }
}
