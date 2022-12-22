package com.example.demospring.controller;

import com.example.demospring.model.PictureModel;
import com.example.demospring.repository.PicturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pictures")
public class PictureController {

    @Autowired
    private PicturesRepository questionRepository;

    @GetMapping
    public Page<PictureModel> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

}