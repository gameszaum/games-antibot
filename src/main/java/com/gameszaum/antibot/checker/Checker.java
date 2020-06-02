package com.gameszaum.antibot.checker;

import com.gameszaum.antibot.exception.InvalidCheckException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Checker {

    private List<Verify> verifies;

    public Checker() {
        verifies = new ArrayList<>();
    }

    public boolean check(String ip) throws InvalidCheckException {
        for (Verify verify : verifies) {
            boolean check = verify.verify(ip);

            if (!verify.getResult()) {
                continue;
            }
            return check;
        }
        return false;
    }

    public enum CheckAPI {

        PROXY_CHECK("https://proxycheck.io/v2/<ip>?vpn=1&asn=1");

        @Getter
        private String url;

        CheckAPI(String url) {
            this.url = url;
        }
    }

}
