package com.gameszaum.antibot.checker;

import com.gameszaum.antibot.exception.InvalidCheckException;

public interface Verify {

    boolean verify(String ip) throws InvalidCheckException;

    boolean getResult();

}
