package com.gameszaum.antibot.checker.verifies;

import com.gameszaum.antibot.checker.Checker;
import com.gameszaum.antibot.checker.Verify;
import com.gameszaum.antibot.exception.InvalidCheckException;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Setter;

public class ProxyCheck implements Verify {

    @Setter
    private boolean result;

    @Override
    public boolean verify(String ip) throws InvalidCheckException {
        try {
            HttpRequest request = HttpRequest.get(Checker.CheckAPI.PROXY_CHECK.getUrl().replace("<ip>", ip)).connectTimeout(5000).readTimeout(5000);
            JsonObject json = (JsonObject) new JsonParser().parse(request.body());

            setResult(true);
            return json.getAsJsonObject(ip).get("proxy").getAsString().equals("no");
        } catch (Exception e) {
            setResult(false);

            throw new InvalidCheckException(ip);
        }
    }

    @Override
    public boolean getResult() {
        return result;
    }

}
