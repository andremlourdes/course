package com.ead.course.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {
}
