package com.example.microservices2.repository;

import com.example.microservices2.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findByUserId(Integer id);

    void deleteAllByUserId(Integer userId);

    List<Contact> findAllByUserId(Integer userId);
}
