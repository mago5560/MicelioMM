package com.luma.miceliomm.customs;

import android.content.Context;

import com.luma.miceliomm.controller.UsuarioController;
import com.luma.miceliomm.model.LoginModel;
import com.luma.miceliomm.model.UsuarioModel;
import com.luma.miceliomm.service.ApiService;

import java.io.IOException;

import android.util.Base64;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public class AuthInterceptor implements Interceptor {

    private final TokenManager tokenManager;
    private final ApiService apiService; // Servicio retrofit para refrescar token
    private UsuarioController usuarioController;
    public AuthInterceptor(Context context, ApiService apiService) {
        this.tokenManager = new TokenManager(context);
        this.apiService = apiService;
        this.usuarioController = new UsuarioController(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = tokenManager.getToken();

        if (token != null && isTokenExpired(token)) {
            // 🔄 Intentar refrescar token
            String refreshToken = tokenManager.getRefreshToken();
            if (refreshToken != null) {
                try {
                    LoginModel model = new LoginModel();
                    model.Usuario = usuarioController.getUsuario().getUsuario();
                    model.Password = usuarioController.getUsuario().getPassword();

                    retrofit2.Response<UsuarioModel> refreshResponse =
                            apiService.postLogin(model).execute();

                    if (refreshResponse.isSuccessful() && !refreshResponse.body().getToken().isEmpty()) {
                        String newToken = refreshResponse.body().getToken();
                        String newRefresh = "Refresh Now";
                        tokenManager.saveTokens(newToken, newRefresh);
                        token = newToken;
                    }
                    /*
                    * retrofit2.Response<RefreshResponse> refreshResponse =
                            authService.refreshToken(new RefreshRequest(refreshToken)).execute();

                    if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
                        String newToken = refreshResponse.body().getAccessToken();
                        String newRefresh = refreshResponse.body().getRefreshToken();
                        tokenManager.saveTokens(newToken, newRefresh);
                        token = newToken;
                    }
                    * */
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Request.Builder requestBuilder = chain.request().newBuilder();

        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }

        return chain.proceed(requestBuilder.build());
    }

    // 🔍 Decodificar y verificar expiración
    private boolean isTokenExpired(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) return true;

            byte[] decodedBytes = Base64.decode(parts[1], Base64.URL_SAFE | Base64.NO_WRAP);
            String payloadJson = new String(decodedBytes, StandardCharsets.UTF_8);
            JSONObject payload = new JSONObject(payloadJson);

            long exp = payload.optLong("exp", 0);
            long now = System.currentTimeMillis() / 1000L;

            return exp <= now;
        } catch (Exception e) {
            return true; // si no se puede decodificar, asumir expirado
        }
    }
}
