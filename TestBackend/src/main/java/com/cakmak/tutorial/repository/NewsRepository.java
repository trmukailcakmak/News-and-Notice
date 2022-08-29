package com.cakmak.tutorial.repository;

import com.cakmak.tutorial.models.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
  List<NewsEntity> findByPublished(boolean published);

  List<NewsEntity> findByTitleContaining(String title);
}
