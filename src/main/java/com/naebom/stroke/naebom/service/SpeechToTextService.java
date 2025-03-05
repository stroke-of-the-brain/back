package com.naebom.stroke.naebom.service;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import com.naebom.stroke.naebom.utils.LevenshteinUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

@RequiredArgsConstructor
@Service
public class SpeechToTextService {

    private static final Logger logger = LoggerFactory.getLogger(SpeechToTextService.class);
    private static final int SAMPLE_RATE = 16000; // 표준 음성 샘플 레이트

    /**Base64 오디오 데이터를 받아 STT 변환 후 유사도 점수 계산*/
    public double evaluateSpeech(String base64Audio, String expectedText) {
        File audioFile = null;
        try {
            // Base64 → FLAC 변환
            audioFile = convertBase64ToFlac(base64Audio);

            if (audioFile.length() == 0) {
                logger.error("변환된 FLAC 파일 크기 - 0바이트");
                return 0;
            }

            //Google Speech API 호출하여 음성 → 텍스트 변환
            String recognizedText = transcribeSpeech(audioFile);

            if (recognizedText.isEmpty()) {
                logger.warn("음성이 감지X");
                return 0;
            }

            //Levenshtein 거리 계산을 통한 유사도 점수 반환
            int distance = LevenshteinUtil.levenshteinDistance(expectedText, recognizedText);
            int maxLength = Math.max(expectedText.length(), recognizedText.length());
            double score = Math.max(0, 100 - ((double) distance / maxLength * 100));

            logger.info("평가 완료: 예상='{}', 인식된='{}', 점수={}", expectedText, recognizedText, score);
            return score;

        } catch (Exception e) {
            logger.error("STT 평가 중 오류 발생: {}", e.getMessage(), e);
            return 0;
        } finally {
            deleteFile(audioFile);
        }
    }

    /**Base64 인코딩된 오디오 데이터를 FLAC 파일로 변환*/
    private File convertBase64ToFlac(String base64Audio) throws Exception {
        byte[] audioBytes = Base64.decodeBase64(base64Audio);
        File flacFile = File.createTempFile("converted_audio", ".flac");

        try (FileOutputStream fos = new FileOutputStream(flacFile)) {
            fos.write(audioBytes);
        }

        logger.info("변환된 FLAC 파일 저장 완료: {}, 크기: {} bytes", flacFile.getAbsolutePath(), flacFile.length());
        return flacFile;
    }

    /** Google Cloud Speech API를 사용하여 STT 변환*/
    private String transcribeSpeech(File audioFile) throws Exception {
        try (SpeechClient speechClient = SpeechClient.create()) {
            byte[] audioBytes = new FileInputStream(audioFile).readAllBytes();
            ByteString audioData = ByteString.copyFrom(audioBytes);

            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.FLAC) // FLAC 포맷 사용
                    .setSampleRateHertz(SAMPLE_RATE) // 16kHz 설정
                    .setLanguageCode("ko-KR") // 한국어 설정
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioData).build();
            RecognizeResponse response = speechClient.recognize(config, audio);

            // 응답 확인 및 상세 로그 추가
            if (response.getResultsList().isEmpty()) {
                logger.warn("변환된 텍스트가 없습니다.");
                return "";
            }

            String transcript = response.getResultsList().get(0).getAlternatives(0).getTranscript();
            logger.info("변환된 텍스트: {}", transcript);
            return transcript;
        }
    }

    /**파일 삭제*/
   private void deleteFile(File file) {
        if (file != null && file.exists() && file.delete()) {
            logger.info("삭제된 파일: {}", file.getAbsolutePath());
        }
    }
}
