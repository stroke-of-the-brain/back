package com.naebom.stroke.naebom.service;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class SpeechToTextService {

    private static final Logger logger = LoggerFactory.getLogger(SpeechToTextService.class);

    public String transcribeSpeech(String base64Audio) {
        File tempAudioFile = null;
        try {
            // Base64 디코딩하여 임시 파일로 저장
            tempAudioFile = File.createTempFile("temp_audio", ".wav");
            try (FileOutputStream fos = new FileOutputStream(tempAudioFile)) {
                byte[] audioBytes = Base64.getDecoder().decode(base64Audio);
                fos.write(audioBytes);
            }

            // Google Cloud Speech-to-Text API 사용
            return recognizeSpeech(tempAudioFile);

        } catch (Exception e) {
            logger.error("STT 변환 실패: {}", e.getMessage());
            return "";
        } finally {
            if (tempAudioFile != null) {
                try {
                    Files.deleteIfExists(tempAudioFile.toPath());
                } catch (Exception ignored) {}
            }
        }
    }

    private String recognizeSpeech(File audioFile) throws Exception {
        try (SpeechClient speechClient = SpeechClient.create()) {
            byte[] audioBytes = Files.readAllBytes(audioFile.toPath());
            ByteString audioData = ByteString.copyFrom(audioBytes);

            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioData).build();
            RecognizeResponse response = speechClient.recognize(config, audio);

            StringBuilder transcript = new StringBuilder();
            for (SpeechRecognitionResult result : response.getResults()) {
                transcript.append(result.getAlternatives(0).getTranscript()).append(" ");
            }

            return transcript.toString().trim();
        }
    }
}
