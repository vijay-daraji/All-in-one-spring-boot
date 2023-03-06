package com.vijay.springbootdemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity(name="file")
public class FileEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	private String fileName;
	@Lob
	private byte[] docFile;
	
	public FileEntity() {
	}
	
	public FileEntity(Long id, String fileName, byte[] docFile) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.docFile = docFile;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getDocFile() {
		return docFile;
	}

	public void setDocFile(byte[] docFile) {
		this.docFile = docFile;
	}
	
	

}