# ANJANA SAMAJ - FIREBASE INTEGRATION GUIDE

This document provides step-by-step instructions to connect **Anjana Samaj** to your live Firebase backend.

---

## 1. FIREBASE PROJECT SETUP

1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Click **Add Project** and name it `Anjana Samaj`.
3. Register an **Android App** with the package name:
   `com.aistudio.anjanasamaj.community`
4. Download `google-services.json` and place it in the `/app` directory of this project.

---

## 2. PHONE AUTHENTICATION CONFIGURATION

1. In the Firebase Console, go to **Authentication** -> **Sign-in method**.
2. Enable **Phone Authentication**.
3. Add test phone numbers (e.g., `+91 98290 12345` with OTP `123456`) under **Phone numbers for testing** for easy local development.

---

## 3. FIRESTORE DATA STRUCTURE & SECURITY RULES

Deploy the following collections:

- `users`: User profiles and settings.
- `posts`: Daily social posts, likes, comment counts.
- `comments`: Comments per post.
- `statuses`: 24-hour status stories.
- `marriage_profiles`: Matrimonial candidate profiles.
- `interests`: Matrimonial interest requests and status (`PENDING`, `ACCEPTED`, `REJECTED`).
- `matches`: Mutual matrimonial matches.
- `chats`: Active 1-on-1 conversations.
- `messages`: Individual chat messages.
- `calls`: Real-time WebRTC video call signaling sessions.
- `notifications`: Push notification history.
- `announcements`: Community announcements.
- `reports`: Content & profile violation reports.
- `verification_requests`: Document verification requests.

### Firestore Security Rules Sample:
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    match /posts/{postId} {
      allow read: if request.auth != null;
      allow create, update, delete: if request.auth != null;
    }
    match /marriage_profiles/{profileId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
    match /verification_requests/{reqId} {
      allow create: if request.auth != null;
      allow read, update: if request.auth.token.admin == true;
    }
  }
}
```

---

## 4. FIREBASE STORAGE SECURITY RULES

Identity documents must be stored in restricted paths:
```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /users/{userId}/verification/{allPaths=*} {
      allow create: if request.auth != null && request.auth.uid == userId;
      allow read, delete: if request.auth.token.admin == true;
    }
    match /posts/{postId}/{allPaths=*} {
      allow read, write: if request.auth != null;
    }
  }
}
```

---

## 5. WEBRTC VIDEO CALL SIGNALING

The application handles 1-on-1 video call signaling through Firestore documents under `calls/{callSessionId}`. Offers, answers, and ICE candidates are exchanged securely between matched users.
