package com.project.simplegw.upload.controllers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.project.simplegw.system.helpers.ResponseConverter;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.Constants;
import com.project.simplegw.system.vos.ResponseMsg;
import com.project.simplegw.upload.services.ImageService;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ImageController {
    private final ImageService imageService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }


    @PostMapping("/images")
    public HashMap<String, Object> uploadImages(HttpServletRequest req, @RequestParam("upload") MultipartFile imgFile) {
        return imageService.upload(req, imgFile);
    }


    @GetMapping(Constants.IMAGE_GET_URL + "{pathYear}/{pathMonth}/{pathDay}/{conversionName}")
    public ResponseEntity<byte[]> getImages(@PathVariable String pathYear, @PathVariable String pathMonth, @PathVariable String pathDay, @PathVariable String conversionName) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageService.getImageByteStream(pathYear, pathMonth, pathDay, conversionName));
    }




    @GetMapping("/portrait")
    public ResponseEntity<byte[]> getPortrait(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageService.getPortrait(loginUser));
    }

    @PostMapping("/portrait")
    public ResponseEntity<Object> uploadPortrait(MultipartHttpServletRequest req, MultipartFile imgFile, @AuthenticationPrincipal LoginUser loginUser) {
        return ResponseConverter.message(
            imageService.uploadPortrait(req, loginUser), ResponseMsg.INSERTED
        );
    }
}
