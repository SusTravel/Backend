package org.sustrav.demo.utils;

import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
 * Created by CUSTDEV3 on 17/03/2018.
 */
public class Joke {
    public static void main(String[] args) throws Exception {
        Random random = new Random(new Date().getTime());
        while (true) {
            int i = random.nextInt(256);
            int j = random.nextInt(256);
            int k = random.nextInt(256);
            String url = "http://192.168.1.56/?rgb=" + i + "," + j + "," + k;
            System.out.println(url);
            new URL(url).openConnection().getContent();
            Thread.sleep(300);
        }
    }
}
