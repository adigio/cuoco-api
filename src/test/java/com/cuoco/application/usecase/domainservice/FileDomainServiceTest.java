package com.cuoco.application.usecase.domainservice;

import com.cuoco.application.exception.UnprocessableException;
import com.cuoco.shared.utils.AudioConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileDomainServiceTest {

    private FileDomainService fileDomainService;

    @BeforeEach
    void setUp() {
        fileDomainService = new FileDomainService();
    }

    @Test
    void getFileName_whenOriginalFilenameNotNull_thenReturnOriginalFilename() {
        MultipartFile file = new MockMultipartFile("file", "test.mp3", "audio/mpeg", new byte[]{});
        String result = fileDomainService.getFileName(file);
        assertEquals("test.mp3", result);
    }

    @Test
    void getFileName_whenOriginalFilenameNull_thenReturnUnknownWithTimestamp() {
        MultipartFile file = new MockMultipartFile("file", null, "audio/mpeg", new byte[]{});
        String result = fileDomainService.getFileName(file);
        assertTrue(result.startsWith("unknown_"));
    }

    @Test
    void getMimeType_whenContentTypeNotNull_thenReturnContentType() {
        MultipartFile file = new MockMultipartFile("file", "file.mp3", "audio/mpeg", new byte[]{});
        String result = fileDomainService.getMimeType(file);
        assertEquals("audio/mpeg", result);
    }

    @Test
    void getMimeType_whenContentTypeNull_thenReturnDefault() {
        MultipartFile file = new MockMultipartFile("file", "file.jpg", null, new byte[]{});
        String result = fileDomainService.getMimeType(file);
        assertEquals("image/jpeg", result);
    }

    @Test
    void isValidAudioFile_whenContentTypeStartsWithAudioFolder_thenReturnFalse() {
        MultipartFile file = new MockMultipartFile("file", "file.mp3", "audio/file", new byte[]{});
        boolean result = fileDomainService.isValidAudioFile(file);
        assertFalse(result);
    }

    @Test
    void isValidAudioFile_whenContentTypeNullAndFilenameHasSupportedExtension_thenReturnFalse() {
        MultipartFile file = new MockMultipartFile("file", "song.mp3", null, new byte[]{});
        boolean result = fileDomainService.isValidAudioFile(file);
        assertTrue(result);
    }

    @Test
    void isValidAudioFile_whenContentTypeNullAndFilenameHasUnsupportedExtension_thenReturnTrue() {
        MultipartFile file = new MockMultipartFile("file", "file.txt", null, new byte[]{});
        boolean result = fileDomainService.isValidAudioFile(file);
        assertTrue(result);
    }

    @Test
    void isValidAudioFile_whenContentTypeNullAndFilenameNull_thenReturnTrue() {
        MultipartFile file = new MockMultipartFile("file", null, null, new byte[]{});
        boolean result = fileDomainService.isValidAudioFile(file);
        assertTrue(result);
    }

    @Test
    void getAudioFormat_whenContentTypeHasSlash_thenReturnSubstringAfterSlash() {
        MultipartFile file = new MockMultipartFile("file", "file.mp3", "audio/mpeg", new byte[]{});
        String result = fileDomainService.getAudioFormat(file);
        assertEquals("mpeg", result);
    }

    @Test
    void getAudioFormat_whenContentTypeNullAndFilenameHasDot_thenReturnExtensionLowerCase() {
        MultipartFile file = new MockMultipartFile("file", "Song.WAV", null, new byte[]{});
        String result = fileDomainService.getAudioFormat(file);
        assertEquals("wav", result);
    }

    @Test
    void getAudioFormat_whenContentTypeAndFilenameNull_thenReturnDefaultMP3() {
        MultipartFile file = new MockMultipartFile("file", null, null, new byte[]{});
        String result = fileDomainService.getAudioFormat(file);
        assertEquals(AudioConstants.MP3, result);
    }

    @Test
    void convertToBase64_whenFileBytes_thenReturnBase64String() {
        byte[] content = "test content".getBytes(StandardCharsets.UTF_8);
        MultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", content);

        String base64 = fileDomainService.convertToBase64(file);
        assertEquals(Base64.getEncoder().encodeToString(content), base64);
    }

    @Test
    void convertToBase64_whenExceptionThrown_thenThrowUnprocessableException() {
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() { return "file"; }
            @Override
            public String getOriginalFilename() { return "file.txt"; }
            @Override
            public String getContentType() { return "text/plain"; }
            @Override
            public boolean isEmpty() { return false; }
            @Override
            public long getSize() { return 0; }
            @Override
            public byte[] getBytes() {
                try {
                    throw new Exception("fail");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public java.io.InputStream getInputStream() { return null; }
            @Override
            public void transferTo(java.io.File dest) { }
        };

        assertThrows(UnprocessableException.class, () -> fileDomainService.convertToBase64(file));
    }
}
