package com.oceanebelle.learn.mongo.db.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity("persons")
public class Person {
    @Id
    private ObjectId id;
    private String name;
}
