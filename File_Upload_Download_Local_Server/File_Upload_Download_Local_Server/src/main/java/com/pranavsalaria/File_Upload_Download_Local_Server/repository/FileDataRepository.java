package com.pranavsalaria.File_Upload_Download_Local_Server.repository;

import com.pranavsalaria.File_Upload_Download_Local_Server.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Optional;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, Integer> {
    Optional<FileData> findByName(String originalFilename);
    @Transactional
    void deleteByName(String fileName);
}
