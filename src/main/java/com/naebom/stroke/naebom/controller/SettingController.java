package com.naebom.stroke.naebom.controller;

import com.naebom.stroke.naebom.entity.Setting;
import com.naebom.stroke.naebom.service.SettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/setting")
public class SettingController {

    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    // 설정 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<Setting> getSetting(@PathVariable Long memberId) {
        Optional<Setting> setting = settingService.getSettingByMemberId(memberId);
        return setting.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 설정 업데이트
    @PutMapping
    public ResponseEntity<Setting> saveOrUpdateSetting(@RequestBody Setting setting) {
        Setting updatedSetting = settingService.saveOrUpdateSetting(setting);
        return ResponseEntity.ok(updatedSetting);
    }
}
