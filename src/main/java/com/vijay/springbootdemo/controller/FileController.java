package com.vijay.springbootdemo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vijay.springbootdemo.dto.FileUploadResponse;
import com.vijay.springbootdemo.service.FileService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/file")
public class FileController {
	
	static final Logger log = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	private FileService fileService;
	
	
	//local system
	@PostMapping("/upload")
	public ResponseEntity<FileUploadResponse> singleFileUpload(MultipartFile file){
		return fileService.singleFileUpload(file);
	}
	
	@PostMapping("/upload/multiple")
	public ResponseEntity<List<FileUploadResponse>> multipleFileUpload(MultipartFile[] files){
		return fileService.multipleFileUpload(files);
	}
	
	@GetMapping("/download/{filename}")
	public ResponseEntity<Resource> downloadSingleFile(@PathVariable String filename, HttpServletRequest request){
		return fileService.downloadSingleFile(filename, request);
	}
	
	@PostMapping("/upload/image")
	public ResponseEntity<FileUploadResponse> singleImageUpload(@RequestParam MultipartFile file) {
		return fileService.singleImageUpload(file);
	}
	
	//database 
	@PostMapping("/uploadDb")
	public ResponseEntity<FileUploadResponse> singleFileUploadDb(MultipartFile file){
		return fileService.singleFileUploadDb(file);
	}
	
	@GetMapping("/downloadFromDb/{fileName}")
	public ResponseEntity<byte[]> downloadSingleFileDb(@PathVariable String fileName, HttpServletRequest request){
		return fileService.downloadSingleFileDb(fileName,request);
	}
	
	@GetMapping("/list")
	public List<String> GetAllFileFromDb(){
		return fileService.getAllFileFromDb();
	}

}

