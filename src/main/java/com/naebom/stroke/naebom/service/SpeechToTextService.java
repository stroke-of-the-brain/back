package com.naebom.stroke.naebom.service;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class SpeechToTextService {

    private static final Logger logger = LoggerFactory.getLogger(SpeechToTextService.class);

    public String transcribeSpeech(String base64Audio) {
        File tempAudioFile = null;
        File convertedAudioFile = null;
        try {
            // Base64 디코딩하여 M4A 파일로 저장
            tempAudioFile = File.createTempFile("temp_audio", ".m4a");
            try (FileOutputStream fos = new FileOutputStream(tempAudioFile)) {
                byte[] audioBytes = Base64.getDecoder().decode(base64Audio);
                fos.write(audioBytes);
            }

            System.out.println("생성된 M4A 오디오 파일 경로: " + tempAudioFile.getAbsolutePath());
            System.out.println("생성된 M4A 오디오 파일 크기: " + tempAudioFile.length() + " bytes");

            // 파일이 0바이트인지 확인
            if (tempAudioFile.length() == 0) {
                throw new RuntimeException("오류: Base64 디코딩 후 M4A 파일 크기가 0바이트입니다. 인코딩 문제 가능.");
            }

            // M4A → WAV 변환
            convertedAudioFile = convertToWav(tempAudioFile);

            // Google Cloud Speech-to-Text API 호출
            return recognizeSpeech(convertedAudioFile);

        } catch (Exception e) {
            logger.error("STT 변환 실패: {}", e.getMessage());
            return "";
        } finally {
            // 생성된 파일 정리
            deleteFile(tempAudioFile);
            deleteFile(convertedAudioFile);
        }
    }

    /**
     * M4A → WAV 변환 (FFmpeg 사용)
     */
    private File convertToWav(File audioFile) throws Exception {
        File convertedFile = File.createTempFile("converted_audio", ".wav");

        // FFmpeg 실행 명령어
        ProcessBuilder builder = new ProcessBuilder(
                "/usr/homebrew/bin/ffmpeg", // FFmpeg 실행 경로 (Mac에서는 /opt/homebrew/bin/ffmpeg일 수도 있음)
                "-y",                     // 기존 파일 덮어쓰기
                "-i", audioFile.getAbsolutePath(), // 입력 파일
                "-ac", "1",               // 모노 채널
                "-ar", "16000",           // 16kHz 샘플링 레이트
                "-acodec", "pcm_s16le",   // LINEAR16 (PCM 16-bit)
                convertedFile.getAbsolutePath()
        );
        builder.redirectErrorStream(true);

        Process process = builder.start();

        // FFmpeg 실행 로그 출력
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            System.out.println("FFmpeg 변환 로그:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        process.waitFor();

        // 변환된 파일 검증
        if (convertedFile.length() == 0) {
            throw new RuntimeException("FFmpeg 변환 실패: 올바른 WAV 파일이 생성되지 않음.");
        }

        System.out.println("변환된 WAV 오디오 파일 경로: " + convertedFile.getAbsolutePath());
        System.out.println("변환된 WAV 오디오 파일 크기: " + convertedFile.length() + " bytes");

        return convertedFile;
    }

    /**
     * Google Cloud Speech API로 STT 변환 요청
     */
    private String recognizeSpeech(File audioFile) throws Exception {
        try (SpeechClient speechClient = SpeechClient.create()) {
            byte[] audioBytes = Files.readAllBytes(audioFile.toPath());
            ByteString audioData = ByteString.copyFrom(audioBytes);

            System.out.println("STT 요청: 오디오 파일 크기 " + audioFile.length() + " bytes");

            // STT 설정
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioData).build();

            System.out.println("Google Cloud Speech API 요청 전송 중...");

            // 비동기 요청 (longRunningRecognize)
            OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
                    speechClient.longRunningRecognizeAsync(config, audio);

            System.out.println("비동기 응답 대기 중...");
            LongRunningRecognizeResponse longResponse = response.get();

            System.out.println("Google Cloud Speech API 응답 수신 완료!");

            if (longResponse.getResultsList().isEmpty()) {
                System.out.println("STT 변환 결과 없음: API에서 응답을 받지 못함.");
                return "";
            }

            StringBuilder transcript = new StringBuilder();
            for (SpeechRecognitionResult result : longResponse.getResultsList()) {
                if (result.getAlternativesCount() > 0) {
                    transcript.append(result.getAlternatives(0).getTranscript()).append(" ");
                }
            }

            String resultText = transcript.toString().trim();
            System.out.println("변환된 텍스트(STT 결과): " + resultText);

            return resultText;
        }
    }

    /**
     * 파일 삭제 메서드 (안전한 파일 정리)
     */
    private void deleteFile(File file) {
        if (file != null && file.exists()) {
            try {
                Files.deleteIfExists(file.toPath());
                System.out.println("삭제된 파일: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("파일 삭제 실패: " + file.getAbsolutePath());
            }
        }
    }
}
