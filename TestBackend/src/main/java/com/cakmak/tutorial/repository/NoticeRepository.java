package com.cakmak.tutorial.repository;

import com.cakmak.tutorial.models.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
  List<NoticeEntity> findByPublished(boolean published);

  List<NoticeEntity> findByTitleContaining(String title);
}
