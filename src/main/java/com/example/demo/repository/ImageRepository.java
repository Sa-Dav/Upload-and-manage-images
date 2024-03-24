package com.example.demo.repository;

import com.example.demo.domain.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageData, Long> {

    Optional<ImageData> findByName(String fileName);

    @Query("SELECT i.name FROM  ImageData i")
    List<String> findAllName();
}
