package com.gerardo.churchmanager.backend.news.repository;

import com.gerardo.churchmanager.backend.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
