package com.example.fileuploadhome.repository;

import com.example.fileuploadhome.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment,Integer> {


}
