package com.vijay.springbootdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vijay.springbootdemo.entity.FileEntity;


@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long>{

	FileEntity findByFileName(String fileName);

}