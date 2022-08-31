package com.cakmak.tutorial.models.entity;

import com.cakmak.tutorial.models.entity.ActivityEntity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("NOTICE")
public class NoticeEntity extends ActivityEntity {
    public NoticeEntity() {
        super();
    }

    public NoticeEntity(String title, String description, boolean published) {
        super(title,description,published);
    }
}
