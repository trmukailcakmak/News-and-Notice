package com.cakmak.tutorial.service;

import com.cakmak.tutorial.exception.NotFoundException;
import com.cakmak.tutorial.mapper.NewsMapper;
import com.cakmak.tutorial.mapper.NoticeMapper;
import com.cakmak.tutorial.models.core.ServiceResult;
import com.cakmak.tutorial.models.entity.NewsEntity;
import com.cakmak.tutorial.models.entity.NoticeEntity;
import com.cakmak.tutorial.payload.request.news.NewsRequest;
import com.cakmak.tutorial.payload.request.notice.NoticeRequest;
import com.cakmak.tutorial.payload.response.news.NewsResponse;
import com.cakmak.tutorial.payload.response.notice.NoticeResponse;
import com.cakmak.tutorial.repository.NewsRepository;
import com.cakmak.tutorial.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    @Autowired
    NoticeMapper noticeMapper;
    @Autowired
    NoticeRepository noticeRepository;

    public ServiceResult<List<NoticeResponse>> getAll(String title) {
        try {
            List<NoticeEntity> noticeList = new ArrayList<>();

            if (title == null)
                noticeRepository.findAll().forEach(noticeList::add);
            else
                noticeRepository.findByTitleContaining(title).forEach(noticeList::add);

            if (noticeList.isEmpty()) {
                return new ServiceResult<>(HttpStatus.NO_CONTENT, new NotFoundException("Not Found"));
            }

            return new ServiceResult<>(noticeMapper.entityListToResponseList(noticeList));
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


    public ServiceResult<NoticeResponse> getNewsById(long id) {
        try {
            Optional<NoticeEntity> noticeDataOptional = noticeRepository.findById(id);

            if (noticeDataOptional.isPresent()) {
                return new ServiceResult<>(noticeMapper.entityToResponse(noticeDataOptional.get()));
            } else {
                return new ServiceResult<>(HttpStatus.NOT_FOUND,new NotFoundException("Not Found"));
            }
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


    public ServiceResult<List<NoticeResponse>> searchByTitle(String title) {
        try {
            List<NoticeEntity> noticeList = noticeRepository.findByTitleLike("%"+title+"%");

            if (!noticeList.isEmpty()) {
                return new ServiceResult<>(noticeMapper.entityListToResponseList(noticeList));
            } else {
                return new ServiceResult<>(HttpStatus.NOT_FOUND,new NotFoundException("Not Found"));
            }
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public ServiceResult<NoticeResponse> create(NoticeRequest noticeRequest) {
        try {
            NoticeEntity noticeEntity = noticeRepository
                    .save(noticeMapper.requestToEntity(noticeRequest));
            noticeEntity.setActivityType("NOTICE");
            return new ServiceResult<>(noticeMapper.entityToResponse(noticeEntity));
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR,e);
        }
    }


    public ServiceResult<NoticeResponse> update( long id, NoticeRequest updateData) {
        try {
            Optional<NoticeEntity> oldData = noticeRepository.findById(id);

            if (oldData.isPresent()) {
                NoticeEntity noticeEntity = oldData.get();
                noticeEntity.setTitle(updateData.getTitle());
                noticeEntity.setDescription(updateData.getDescription());
                noticeEntity.setPublished(updateData.getPublished());
                return new ServiceResult<>(noticeMapper.entityToResponse(noticeRepository.save(noticeEntity)));
            } else {
                return new ServiceResult<>(HttpStatus.NOT_FOUND,new NotFoundException("Not Found"));
            }
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


    public ServiceResult<HttpStatus> delete(long id) {
        try {
            noticeRepository.deleteById(id);
            return new ServiceResult<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ServiceResult<HttpStatus> deleteAll() {
        try {
            noticeRepository.deleteAll();
            return new ServiceResult<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ServiceResult<List<NoticeResponse>> findByPublished() {
        try {
            List<NoticeEntity> noticeEntityList = noticeRepository.findByPublished(true);

            if (noticeEntityList.isEmpty()) {
                return new ServiceResult<>(HttpStatus.NO_CONTENT,new NotFoundException("Not Found"));
            }
            return new ServiceResult<>(noticeMapper.entityListToResponseList(noticeEntityList));
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR,e);
        }
    }

}
