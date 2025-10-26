package com.luma.miceliomm.customs;

import android.content.Context;

public class TokenManager {
    private final Context context;
    private final String PREFS = "app_prefs";
    private final String TOKEN_KEY = "jwt_token";
    private final String REFRESH_KEY = "refresh_token";

    public TokenManager(Context context) {
        this.context = context;
    }

    public String getToken() {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(TOKEN_KEY, null);
    }

    public String getRefreshToken() {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(REFRESH_KEY, null);
    }

    public void saveTokens(String accessToken, String refreshToken) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                .putString(TOKEN_KEY, accessToken)
                .putString(REFRESH_KEY, refreshToken)
                .apply();
    }
}
