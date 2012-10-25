package edu.gatech.islab.chat.main;

import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

public class MainHelper {

    private Random random;

    public MainHelper() {
        random = new Random();
    }

    public synchronized String getNewSessionId() {
        String randTime = (random.nextLong() + System.currentTimeMillis()) + "";
        return DigestUtils.sha256Hex(randTime);
    }

}
