package com.automation.linkedin;

import org.testng.annotations.Test;

public class Debug {
    @Test
    public void test(){
        String strUserName = System.getProperty("LOGIN");

        System.out.println(strUserName);
    }
}
