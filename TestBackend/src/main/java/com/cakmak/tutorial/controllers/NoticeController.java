package com.cakmak.tutorial.controllers;

import com.cakmak.tutorial.models.NoticeEntity;
import com.cakmak.tutorial.repository.NoticeRepository;
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
public class NoticeController {

	@Autowired
	NoticeRepository noticeRepository;

	@GetMapping("/notices")
	public ResponseEntity<List<NoticeEntity>> getAllNotice(@RequestParam(required = false) String title) {
		try {
			List<NoticeEntity> noticeList = new ArrayList<>();

			if (title == null)
				noticeRepository.findAll().forEach(noticeList::add);
			else
				noticeRepository.findByTitleContaining(title).forEach(noticeList::add);

			if (noticeList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(noticeList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/notices/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<NoticeEntity> getNoticeById(@PathVariable("id") long id) {
		Optional<NoticeEntity> noticeData = noticeRepository.findById(id);

		if (noticeData.isPresent()) {
			return new ResponseEntity<>(noticeData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/notices")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<NoticeEntity> createNotice(@RequestBody NoticeEntity notice) {
		try {
			 notice = noticeRepository
					.save(new NoticeEntity(notice.getTitle(), notice.getDescription(), false));
			return new ResponseEntity<>(notice, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/notices/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<NoticeEntity> updateNotice(@PathVariable("id") long id, @RequestBody NoticeEntity newNoticeData) {
		Optional<NoticeEntity> oldNoticeData = noticeRepository.findById(id);

		if (oldNoticeData.isPresent()) {
			NoticeEntity notice = oldNoticeData.get();
			notice.setTitle(newNoticeData.getTitle());
			notice.setDescription(newNoticeData.getDescription());
			notice.setPublished(newNoticeData.isPublished());
			return new ResponseEntity<>(noticeRepository.save(notice), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/notices/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deleteNotice(@PathVariable("id") long id) {
		try {
			noticeRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/notices")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deleteAllNotice() {
		try {
			noticeRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/notices/published")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<NoticeEntity>> findByPublished() {
		try {
			List<NoticeEntity> noticeList = noticeRepository.findByPublished(true);

			if (noticeList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(noticeList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
