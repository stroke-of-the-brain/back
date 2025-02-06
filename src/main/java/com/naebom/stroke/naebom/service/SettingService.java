package com.naebom.stroke.naebom.service;

import com.naebom.stroke.naebom.entity.Setting;
import com.naebom.stroke.naebom.repository.SettingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingService {

    private final SettingRepository settingRepository;

    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    // 설정 정보 조회
    public Optional<Setting> getSettingByMemberId(Long memberId) {
        return settingRepository.findById(memberId);
    }

    // 설정 저장 또는 업데이트
    public Setting saveOrUpdateSetting(Setting setting) {
        return settingRepository.save(setting);
    }

    // 기본 설정 초기화
    public Setting initializeDefaultSetting(Long memberId) {
        Setting defaultSetting = new Setting(
                memberId,
                "en",
                "light",
                true,
                "medium"
        );
        return settingRepository.save(defaultSetting);
    }
}
