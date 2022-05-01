package com.oceanebelle.learn.mongo.db;

import com.mongodb.MongoException;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DBAdapterTest extends DBTest {

    @Autowired
    DBAdaptor adaptor;

    @Test
    @SneakyThrows
    public void shouldReturnVersions() throws IOException {
        assertThat(adaptor.getVersion()).isEqualTo("4.2.6");
    }

    @Test
    @SneakyThrows
    public void shouldReturnVersionsIfSlow() throws IOException {
        toxiproxyClient.getProxy(MONGO_SERVICE).toxics().latency("mongo_latency", ToxicDirection.UPSTREAM, 500);
        assertThat(adaptor.getVersion()).isEqualTo("4.2.6");
    }

    @Test
    @SneakyThrows
    public void shouldReturnVersionsIfTimeout() throws IOException {
        toxiproxyClient.getProxy(MONGO_SERVICE).toxics().timeout("mongo_timeout", ToxicDirection.UPSTREAM, 5);
        assertThatThrownBy(() -> adaptor.getVersion()).isInstanceOf(MongoException.class);
    }

    @AfterEach
    @SneakyThrows
    public void reset() {
        toxiproxyClient.reset();
    }
}
