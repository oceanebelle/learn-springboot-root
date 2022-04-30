package com.oceanebelle.learn.mongo.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix="db")
public class DBProperties {
    private String servers;
    private String name;
    private String authDB;
    private String password;
}
