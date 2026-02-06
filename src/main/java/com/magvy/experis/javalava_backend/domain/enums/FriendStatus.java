package com.magvy.experis.javalava_backend.domain.enums;

public enum FriendStatus {
    NOT_FRIENDS,
    FRIENDS,
    PENDING,    // request sent by current user
    REQUESTED   // request received by current user
}