package com.tasc.blogging.service;

import com.tasc.blogging.aop.ApplicationException;
import com.tasc.blogging.entity.blog.Thumbnail;
import com.tasc.blogging.model.response.BaseResponse;
import com.tasc.blogging.repository.ThumbnailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ThumbnailService {
    @Autowired
    private ThumbnailRepository thumbnailRepository;

    public BaseResponse<Thumbnail> createThumbnail(String url) throws ApplicationException {
        Thumbnail thumbnail = Thumbnail.builder()
                .url(url)
                .build();
        thumbnailRepository.save(thumbnail);
        return new BaseResponse<>("Thumbnail created successfully", thumbnail);
    }
    public BaseResponse<String> deleteThumbnail(Long id) throws ApplicationException  {
        Optional<Thumbnail> optionalThumbnail = thumbnailRepository.findById(id);

        if (optionalThumbnail.isEmpty())
            throw new ApplicationException("Thumbnail not found");

        Thumbnail thumbnail = optionalThumbnail.get();
        thumbnailRepository.delete(thumbnail);
        return new BaseResponse<>("Thumbnail deleted successfully");
    }
}
