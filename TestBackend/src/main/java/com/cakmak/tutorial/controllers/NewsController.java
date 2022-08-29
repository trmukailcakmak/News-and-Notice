package com.cakmak.tutorial.controllers;

import com.cakmak.tutorial.models.NewsEntity;
import com.cakmak.tutorial.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    NewsRepository newsRepository;

    @GetMapping("/news")
    public ResponseEntity<List<NewsEntity>> getAllNews(@RequestParam(required = false) String title) {
        try {
            List<NewsEntity> news = new ArrayList<>();

            if (title == null)
                newsRepository.findAll().forEach(news::add);
            else
                newsRepository.findByTitleContaining(title).forEach(news::add);

            if (news.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(news, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/news/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<NewsEntity> getNewsById(@PathVariable("id") long id) {
        Optional<NewsEntity> newsData = newsRepository.findById(id);

        if (newsData.isPresent()) {
            return new ResponseEntity<>(newsData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/news")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<NewsEntity> createNews(@RequestBody NewsEntity news) {
        try {
             news = newsRepository
                    .save(new NewsEntity(news.getTitle(), news.getDescription(), false));
            return new ResponseEntity<>(news, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/news/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<NewsEntity> updateNews(@PathVariable("id") long id, @RequestBody NewsEntity willNewsData) {
        Optional<NewsEntity> oldNewsData = newsRepository.findById(id);

        if (oldNewsData.isPresent()) {
            NewsEntity news = oldNewsData.get();
            news.setTitle(willNewsData.getTitle());
            news.setDescription(willNewsData.getDescription());
            news.setPublished(willNewsData.isPublished());
            return new ResponseEntity<>(newsRepository.save(news), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/news/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteNews(@PathVariable("id") long id) {
        try {
            newsRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/news")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllNews() {
        try {
            newsRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/news/published")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<NewsEntity>> findByPublished() {
        try {
            List<NewsEntity> news = newsRepository.findByPublished(true);

            if (news.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(news, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
