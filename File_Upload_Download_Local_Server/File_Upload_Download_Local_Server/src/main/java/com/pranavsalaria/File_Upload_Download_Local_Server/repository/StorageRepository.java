package com.pranavsalaria.File_Upload_Download_Local_Server.repository;

import com.pranavsalaria.File_Upload_Download_Local_Server.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<ImageData, Integer> {
    Optional<ImageData> findByName(String name);
}
