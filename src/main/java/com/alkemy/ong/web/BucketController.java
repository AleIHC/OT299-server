package com.alkemy.ong.web;

import com.alkemy.ong.domain.cloud.AmazonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage")
public class BucketController {

    private AmazonService amazonService;

    @Autowired
    BucketController(AmazonService amazonService) {
        this.amazonService = amazonService;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonService.uploadFile(file);
    }

}