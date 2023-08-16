package com.example.click_v1.utilities;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "emails";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE_NAME = "clickAppPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fakeTokeny";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";

    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";

    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";

    public static final String KEY_SENDER_NAME = "senderName";

    public static final String KEY_RECEIVER_NAME = "receiverName";

    public static final String KEY_SENDER_IAMGE = "senderImage";

    public static final String KEY_RECEIVER_IMAGE = "receiverImage";

    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";

    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_DATA = "data";

    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static HashMap<String, String> remoteMsgHeaders = null;

    public static HashMap<String, String> getRemoteMsgHeaders() {
        if (remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAbGAQXBE:APA91bGMVyeicD6WKHyS7quhYJmQdhbkeA5GxFWKUPT8XWwyF6eE1iRCAK6vL-oTtWhlFeXPGKFJLvlZhC7TRBErwcWdt2lWLR_7r1H_hKKQ5qK9AS_mLySGrLzNmu1HQrARyBrEO2Cj"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json");
        }
        return remoteMsgHeaders;
    }

    public static final String KEY_LOCATION = "location";

    public static final String KEY_LOCATION_ENABLE = "locationEnable";

    public static final String KEY_HOBBIES = "hobbies";

    public static final String KEY_SELF_INTRODUCTION = "selfIntroduction";

    public static final String KEY_COUNTRY = "country";

    public static final String KEY_BIRTHDAY = "birthday";

    public static final String KEY_GENDER = "gender";

}
