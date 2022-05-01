package com.oceanebelle.learn.mongo.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class DBAdapterTest extends DBTest {

    @Autowired
    DBAdaptor adaptor;

    @Test
    public void shouldReturnVersions() {
        assertThat(adaptor.getVersion()).isEqualTo("4.2.6");
    }
}
