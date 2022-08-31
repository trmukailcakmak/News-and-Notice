package com.cakmak.tutorial.controllers;

import com.cakmak.tutorial.models.core.ServiceResult;
import com.cakmak.tutorial.models.entity.NewsEntity;
import com.cakmak.tutorial.payload.request.news.NewsRequest;
import com.cakmak.tutorial.payload.response.news.NewsResponse;
import com.cakmak.tutorial.repository.NewsRepository;
import com.cakmak.tutorial.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    NewsService newsService;

    @GetMapping("/news")
    public ResponseEntity<List<NewsResponse>> getAllNews(@RequestParam(required = false) String title) {
        try {

            ServiceResult<List<NewsResponse>> serviceResult = newsService.getAllNews(title);

            if(serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getValue(), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(null,serviceResult.getStatus());
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/news/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<NewsResponse> getNewsById(@PathVariable("id") long id) {
        try{
            ServiceResult<NewsResponse> serviceResult = newsService.getNewsById(id);

            if (serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getValue(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/news/search/{search}")
    public ResponseEntity<List<NewsResponse>> getNewsBySearch(@PathVariable("search") String search) {
        try{
            ServiceResult<List<NewsResponse>> serviceResult = newsService.getNewsBySearch(search);

            if (serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getValue(), serviceResult.getStatus());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/news")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<NewsResponse> createNews(@RequestBody @Valid NewsRequest news) {
        try {
            ServiceResult<NewsResponse> serviceResult = newsService.createNews(news);

            if (serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getValue(), serviceResult.getStatus());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/news/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<NewsResponse> updateNews(@PathVariable("id") long id, @RequestBody NewsRequest updateDataNewsRequest) {
        try {
            ServiceResult<NewsResponse> serviceResult = newsService.updateNews(id,updateDataNewsRequest);

            if (serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getValue(), serviceResult.getStatus());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/news/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteNews(@PathVariable("id") long id) {
        try {
            ServiceResult<HttpStatus> serviceResult = newsService.deleteNews(id);

            if (serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getValue(), serviceResult.getStatus());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/news")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllNews() {
        try {
            ServiceResult<HttpStatus> serviceResult = newsService.deleteAllNews();

            if (serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getValue(), serviceResult.getStatus());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/news/published")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<NewsResponse>> findByPublished() {
        try {
            ServiceResult<List<NewsResponse>> serviceResult = newsService.findByPublished();

            if (serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getValue(), serviceResult.getStatus());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
