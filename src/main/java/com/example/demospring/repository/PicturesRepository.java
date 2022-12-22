package com.example.demospring.repository;
 
import com.example.demospring.model.PictureModel;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface PicturesRepository extends JpaRepository<PictureModel, Long> {
}