package com.naebom.stroke.naebom.entity;

import jakarta.persistence.*;

@Entity
public class Setting {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private String screenMode;

    @Column(nullable = false)
    private boolean notificationsEnabled; // 알림

    @Column(nullable = false)
    private String fontSize; // small, medium, large 이 3개로 나눔

    public Setting() {
    }

    public Setting(Long memberId, String language, String screenMode, boolean notificationsEnabled, String fontSize) {
        this.memberId = memberId;
        this.language = language;
        this.screenMode = screenMode;
        this.notificationsEnabled = notificationsEnabled;
        this.fontSize = fontSize;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }
}
