package com.cuoco.application.usecase.domainservice;

import com.cuoco.application.exception.UnprocessableException;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.utils.AudioConstants;
import com.cuoco.shared.utils.Constants;
import com.cuoco.shared.utils.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Component
public class FileDomainService {

    private final static String AUDIO_FOLDER = "audio/";

    public String getFileName(MultipartFile file) {
        return file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank() ? file.getOriginalFilename() : "unknown_" + System.currentTimeMillis();
    }

    public String getMimeType(MultipartFile file) {
        return file.getContentType() != null ? file.getContentType() : "image/jpeg";
    }

    public boolean isValidAudioFile(MultipartFile file) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        if (contentType != null) {
            return !contentType.startsWith(AUDIO_FOLDER);
        }

        if (filename != null && filename.contains(Constants.SLASH.getValue())) {
            String extension = filename.substring(filename.lastIndexOf(Constants.DOT.getValue()) + 1).toLowerCase();
            return !FileUtils.SUPPORTED_AUDIO_EXTENSIONS.contains(extension);
        }

        return true;
    }

    public String getAudioFormat(MultipartFile file) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        if (contentType != null && contentType.contains(Constants.SLASH.getValue())) {
            return contentType.substring(contentType.indexOf(Constants.SLASH.getValue()) + 1);
        }

        if (filename != null && filename.contains(Constants.DOT.getValue())) {
            return filename.substring(filename.lastIndexOf(Constants.DOT.getValue()) + 1).toLowerCase();
        }

        return AudioConstants.MP3;
    }

    public String convertToBase64(MultipartFile file) {
        try {
            return Base64.getEncoder().encodeToString(file.getBytes());
        } catch (Exception e) {
            throw new UnprocessableException(ErrorDescription.UNHANDLED.getValue());
        }
    }
}
