package com.vijay.springbootdemo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vijay.springbootdemo.dto.FileUploadResponse;
import com.vijay.springbootdemo.entity.FileEntity;
import com.vijay.springbootdemo.repository.FileRepository;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class FileService{
	
	static final Logger log = LoggerFactory.getLogger(FileService.class);
	
	@Autowired
	private FileRepository fileRepository;
	
	@Value("${file.storage.location:}")
	private String fileStorageLocation;
	
	@PostConstruct
	public void init() {
		try {
			Files.createDirectories(Paths.get(fileStorageLocation));
		} catch (IOException e) {
			throw new RuntimeException("Issue in creating file directory");
		}
	}

	public ResponseEntity<FileUploadResponse> singleFileUpload(MultipartFile file) {
		Path path = Paths.get(fileStorageLocation);
		String filename = file.getOriginalFilename();
		try {
			Files.copy(file.getInputStream(), path.resolve(filename));
		} catch (IOException e) {
			throw new RuntimeException("Issue in storing the file", e);
		}
		String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
			.path("/download/")
			.path(filename)
			.toUriString();
		String contentType = file.getContentType();
		FileUploadResponse fileUploadResponse = new FileUploadResponse(filename,contentType, uri);
		return new ResponseEntity<>(fileUploadResponse, HttpStatus.CREATED);
	}

	public ResponseEntity<List<FileUploadResponse>> multipleFileUpload(MultipartFile[] files) {
		List<FileUploadResponse> fileUploadResponseList = new ArrayList<>();
		Arrays.asList(files).stream().forEach(file-> {
			FileUploadResponse fileUploadResponse = singleFileUpload(file).getBody();
			fileUploadResponseList.add(fileUploadResponse);
		});
		
		return new ResponseEntity<>(fileUploadResponseList, HttpStatus.OK);
	}

	public ResponseEntity<Resource> downloadSingleFile(String filename, HttpServletRequest request) {
		Path path = Paths.get(fileStorageLocation).resolve(filename);
		Resource resource;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Issue in reading the file", e);
		}
		
		if(resource.exists() && resource.isReadable()) {
			String mimeType;
			try {
				mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			} catch (IOException e) {
				mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
			}
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(mimeType))
//					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+resource.getFilename()) //download
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+resource.getFilename()) //render
					.body(resource);
		}else {
			throw new RuntimeException("the file doesn't exist or not readable");
		}
	}

	public ResponseEntity<FileUploadResponse> singleImageUpload(MultipartFile file) {
		String contentType = file.getContentType();
		if(contentType.startsWith("image/")) {
			return singleFileUpload(file);
		}
		throw new RuntimeException("it's not an image");
	}

	public ResponseEntity<FileUploadResponse> singleFileUploadDb(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		FileEntity fileEntity = new FileEntity();
		fileEntity.setFileName(fileName);
		try {
			fileEntity.setDocFile(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileRepository.save(fileEntity);
		String url = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/downloadFromDb/")
				.path(fileName)
				.toUriString();
		String contentType = file.getContentType();
		FileUploadResponse response = new FileUploadResponse(fileName, contentType, url);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	public ResponseEntity<byte[]> downloadSingleFileDb(String fileName, HttpServletRequest request) {
		FileEntity fileEntity = fileRepository.findByFileName(fileName);
		String mimeType = request.getServletContext().getMimeType(fileEntity.getFileName());
		ResponseEntity<byte[]> body = ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(mimeType))
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+fileEntity.getFileName()) //download
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName="+fileEntity.getFileName()) //render
				.body(fileEntity.getDocFile());
		return body;
	}

	public List<String> getAllFileFromDb() {
		List<FileEntity> files = fileRepository.findAll();
		List<String> allFiles = new ArrayList<>();
		files.stream().forEach(file -> {
			allFiles.add(file.getFileName());
		});
		return allFiles;
	}

}
