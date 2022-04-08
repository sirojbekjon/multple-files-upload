package com.example.fileuploadhome.controller;

import com.example.fileuploadhome.entity.Attachment;
import com.example.fileuploadhome.entity.AttachmentContent;
import com.example.fileuploadhome.repository.AttachmentContentRepository;
import com.example.fileuploadhome.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {


    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    @PostMapping("/upload")
    public String uploadfile(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());

        if (file!=null){
            String originalFilename= file.getOriginalFilename();
            long size = file.getSize();
            String contentType = file.getContentType();

            Attachment attachment = new Attachment();
            attachment.setOriginalName(originalFilename);
            attachment.setSize(size);
            attachment.setContentType(contentType);
            Attachment savedAttachment = attachmentRepository.save(attachment);

            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setAsosiyContent(file.getBytes());
            attachmentContent.setAttachment(savedAttachment);

            AttachmentContent content = attachmentContentRepository.save(attachmentContent);

            return "File saved ID si:"+attachment.getId();
                    }
        return "File saqlanmadi";
    }


    public static final String uploadurl = "upload";
    @PostMapping("/uploadsystem")
    public String uploadsystem(MultipartHttpServletRequest request) throws IOException {

        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());
        Attachment attachment = new Attachment();
        if (file != null){
            String originalFileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            long size = file.getSize();
            attachment.setOriginalName(file.getOriginalFilename());
            attachment.setContentType(file.getContentType());
            attachment.setSize(file.getSize());

            String orginalname = file.getOriginalFilename();
            String[] split = orginalname.split("\\.");

            String name = UUID.randomUUID().toString()+"."+split[split.length-1];
            attachment.setName(name);
            attachmentRepository.save(attachment);

            Path path = Paths.get(uploadurl+"/"+name);
            Files.copy(file.getInputStream(),path);
            return "file saved ID si:"+attachment.getId();
       }
        return "file not found for saving";
    }



    @PostMapping("/uploadfiles")
    public String uploadFiles(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        while (fileNames.hasNext()) {
            MultipartFile file = request.getFile(fileNames.next());
            if (file!=null){
                Attachment attachment = new Attachment();
                String orginalname = file.getOriginalFilename();
                long size = file.getSize();
                String contenttype = file.getContentType();
                String name = file.getName();
                attachment.setName(name);
                attachment.setSize(size);
                attachment.setOriginalName(orginalname);
                attachment.setContentType(contenttype);
                Attachment savedAttachment = attachmentRepository.save(attachment);

                AttachmentContent attachmentContent = new AttachmentContent();
                attachmentContent.setAsosiyContent(file.getBytes());
                attachmentContent.setAttachment(savedAttachment);
                attachmentContentRepository.save(attachmentContent);
            }
        }

        return "All files saved successfully";
    }

    @GetMapping("/info")
    public List<Attachment> getAttachment(){
        return attachmentRepository.findAll();
    }
    @GetMapping("/info/{id}")
    public Optional<Attachment> getAttachment(@PathVariable Integer id){
        return attachmentRepository.findById(id);
    }


    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()) {

            Attachment attachment = optionalAttachment.get();
            Optional<AttachmentContent> contentOptional = attachmentContentRepository.findByAttachmentId(id);

            if (contentOptional.isPresent()){
                AttachmentContent attachmentContent = contentOptional.get();

                response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getOriginalName() + "\"");
                response.setContentType(attachment.getContentType());

                FileCopyUtils.copy(attachmentContent.getAsosiyContent(),response.getOutputStream());
                System.out.println("file  downloaded ");
            }
        }

    }

}
