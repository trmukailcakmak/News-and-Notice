package com.cakmak.tutorial.models;

import javax.persistence.*;

@Entity
@DiscriminatorValue("NEWS")
public class NewsEntity extends ActivityEntity {
    public NewsEntity() {
        super();
    }

    public NewsEntity(String title, String description, boolean published) {
        super(title,description,published);
    }
}
