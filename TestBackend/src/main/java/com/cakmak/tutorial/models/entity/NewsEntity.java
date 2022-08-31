package com.cakmak.tutorial.models.entity;

import com.cakmak.tutorial.models.entity.ActivityEntity;

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
