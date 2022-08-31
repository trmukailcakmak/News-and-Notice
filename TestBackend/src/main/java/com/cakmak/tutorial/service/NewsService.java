package com.cakmak.tutorial.service;

import com.cakmak.tutorial.exception.NotFoundException;
import com.cakmak.tutorial.mapper.NewsMapper;
import com.cakmak.tutorial.models.core.ServiceResult;
import com.cakmak.tutorial.models.entity.NewsEntity;
import com.cakmak.tutorial.payload.request.news.NewsRequest;
import com.cakmak.tutorial.payload.response.news.NewsResponse;
import com.cakmak.tutorial.repository.NewsRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.naming.NotContextException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    //NewsMapper INSTANCE_NEWS_MAPPER = Mappers.getMapper(NewsMapper.class);
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    NewsRepository newsRepository;

    public ServiceResult<List<NewsResponse>> getAllNews(String title) {
        try {
            List<NewsEntity> news = new ArrayList<>();

            if (title == null)
                newsRepository.findAll().forEach(news::add);
            else
                newsRepository.findByTitleContaining(title).forEach(news::add);

            if (news.isEmpty()) {
                return new ServiceResult<>(HttpStatus.NO_CONTENT, new NotFoundException("Not Found"));
            }

            return new ServiceResult<>(newsMapper.entityListToResponseList(news));
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


    public ServiceResult<NewsResponse> getNewsById(long id) {
        try {
            Optional<NewsEntity> newsData = newsRepository.findById(id);

            if (newsData.isPresent()) {
                return new ServiceResult<>(newsMapper.entityToResponse(newsData.get()));
            } else {
                return new ServiceResult<>(HttpStatus.NOT_FOUND,new NotFoundException("Not Found"));
            }
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


    public ServiceResult<List<NewsResponse>> getNewsBySearch(String search) {
        try {
            List<NewsEntity> newsEntityList = newsRepository.findByTitleLike("%"+search+"%");

            if (!newsEntityList.isEmpty()) {
                return new ServiceResult<>(newsMapper.entityListToResponseList(newsEntityList));
            } else {
                return new ServiceResult<>(HttpStatus.NOT_FOUND,new NotFoundException("Not Found"));
            }
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public ServiceResult<NewsResponse> createNews(NewsRequest newsRequest) {
        try {
            NewsEntity newsEntity = newsRepository
                    .save(newsMapper.requestToEntity(newsRequest));
            return new ServiceResult<>(newsMapper.entityToResponse(newsEntity));
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR,e);
        }
    }


    public ServiceResult<NewsResponse> updateNews( long id, NewsRequest updateDataNewsRequest) {
        try {
            Optional<NewsEntity> oldNewsData = newsRepository.findById(id);

            if (oldNewsData.isPresent()) {
                NewsEntity news = oldNewsData.get();
                news.setTitle(updateDataNewsRequest.getTitle());
                news.setDescription(updateDataNewsRequest.getDescription());
                news.setPublished(updateDataNewsRequest.getPublished());
                return new ServiceResult<>(newsMapper.entityToResponse(newsRepository.save(news)));
            } else {
                return new ServiceResult<>(HttpStatus.NOT_FOUND,new NotFoundException("Not Found"));
            }
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


    public ServiceResult<HttpStatus> deleteNews(long id) {
        try {
            newsRepository.deleteById(id);
            return new ServiceResult<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ServiceResult<HttpStatus> deleteAllNews() {
        try {
            newsRepository.deleteAll();
            return new ServiceResult<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ServiceResult<List<NewsResponse>> findByPublished() {
        try {
            List<NewsEntity> news = newsRepository.findByPublished(true);

            if (news.isEmpty()) {
                return new ServiceResult<>(HttpStatus.NO_CONTENT,new NotFoundException("Not Found"));
            }
            return new ServiceResult<>(newsMapper.entityListToResponseList(news));
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR,e);
        }
    }

}
