package com.puvn.common.validation;

import com.google.gson.Gson;
import com.puvn.models.JsonCaptchaStatus;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ReCaptchaValidator {
    public boolean isValid(String g_recaptcha_response) throws IOException {
        return recaptchaIsValid(g_recaptcha_response);
    }

    private boolean recaptchaIsValid(String g_recaptcha_response) throws IOException {
        Gson gson = new Gson();
        JsonCaptchaStatus cpStatus = gson.fromJson(getRecaptchaStatus(g_recaptcha_response), JsonCaptchaStatus.class);
        return cpStatus.isSuccess();
    }

    private String getRecaptchaStatus(String g_recaptcha_response) throws IOException {
        String url = "https://www.google.com/recaptcha/api/siteverify";

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "secret<>" + g_recaptcha_response;

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return (response.toString());
    }
}
