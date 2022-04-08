package com.example.fileuploadhome.repository;

import com.example.fileuploadhome.entity.Attachment;
import com.example.fileuploadhome.entity.AttachmentContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent,Integer> {

    Optional<AttachmentContent> findByAttachmentId(Integer attachment_id);
}
