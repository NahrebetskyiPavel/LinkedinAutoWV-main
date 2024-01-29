package com.automation.linkedin;

import org.testng.annotations.Test;

public class Debug {
    @Test
    public void test(){
        System.getenv().forEach((k, v) -> System.out.println(k + ":" + v));
    }
}
