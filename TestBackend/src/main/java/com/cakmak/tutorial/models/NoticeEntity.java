package com.cakmak.tutorial.models;

import javax.persistence.*;

@Entity
@DiscriminatorValue("NOTICE")
public class NoticeEntity extends ActivityEntity{
    public NoticeEntity() {
        super();
    }

    public NoticeEntity(String title, String description, boolean published) {
        super(title,description,published);
    }
}
