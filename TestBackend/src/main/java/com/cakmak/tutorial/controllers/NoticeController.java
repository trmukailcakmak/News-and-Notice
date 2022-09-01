package com.cakmak.tutorial.controllers;

import com.cakmak.tutorial.models.core.ServiceResult;
import com.cakmak.tutorial.models.entity.NewsEntity;
import com.cakmak.tutorial.payload.request.news.NewsRequest;
import com.cakmak.tutorial.payload.request.notice.NoticeRequest;
import com.cakmak.tutorial.payload.response.news.NewsResponse;
import com.cakmak.tutorial.payload.response.notice.NoticeResponse;
import com.cakmak.tutorial.repository.NewsRepository;
import com.cakmak.tutorial.service.NewsService;
import com.cakmak.tutorial.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class NoticeController {

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	NoticeService noticeService;

	@GetMapping("/notices")
	public ResponseEntity<List<NoticeResponse>> getAllNews(@RequestParam(required = false) String title) {
		try {

			ServiceResult<List<NoticeResponse>> serviceResult = noticeService.getAll(title);

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

	@GetMapping("/notices/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<NoticeResponse> getNewsById(@PathVariable("id") long id) {
		try{
			ServiceResult<NoticeResponse> serviceResult = noticeService.getNewsById(id);

			if (serviceResult.isSuccess()) {
				return new ResponseEntity<>(serviceResult.getValue(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/notices/search/{title}")
	public ResponseEntity<List<NoticeResponse>> searchByTitle(@PathVariable("title") String title) {
		try{
			ServiceResult<List<NoticeResponse>> serviceResult = noticeService.searchByTitle(title);

			if (serviceResult.isSuccess()) {
				return new ResponseEntity<>(serviceResult.getValue(), serviceResult.getStatus());
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/notices")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<NoticeResponse> createNotice(@RequestBody @Valid NoticeRequest request) {
		try {
			ServiceResult<NoticeResponse> serviceResult = noticeService.create(request);
			simpMessagingTemplate.convertAndSend("/topic/notice.create",serviceResult.getValue());
			if (serviceResult.isSuccess()) {
				return new ResponseEntity<>(serviceResult.getValue(), serviceResult.getStatus());
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/notices/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<NoticeResponse> updateNotice(@PathVariable("id") long id, @RequestBody NoticeRequest request) {
		try {
			ServiceResult<NoticeResponse> serviceResult = noticeService.update(id,request);

			if (serviceResult.isSuccess()) {
				return new ResponseEntity<>(serviceResult.getValue(), serviceResult.getStatus());
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/notices/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deleteNotice(@PathVariable("id") long id) {
		try {
			ServiceResult<HttpStatus> serviceResult = noticeService.delete(id);

			if (serviceResult.isSuccess()) {
				return new ResponseEntity<>(serviceResult.getValue(), serviceResult.getStatus());
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/notices")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deleteAllNotice() {
		try {
			ServiceResult<HttpStatus> serviceResult = noticeService.deleteAll();

			if (serviceResult.isSuccess()) {
				return new ResponseEntity<>(serviceResult.getValue(), serviceResult.getStatus());
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/notices/published")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<List<NoticeResponse>> findByPublished() {
		try {
			ServiceResult<List<NoticeResponse>> serviceResult = noticeService.findByPublished();

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
