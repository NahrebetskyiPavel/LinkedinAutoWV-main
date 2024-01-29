package com.automation.linkedin;

import org.testng.annotations.Test;

import java.util.Map;

public class Debug {
    @Test
    public void test(){
        Map<String, String> env = System.getenv();
        System.out.println(env.get("LOGIN"));
        System.out.println(env.get("PASSWORD"));
    }
}
