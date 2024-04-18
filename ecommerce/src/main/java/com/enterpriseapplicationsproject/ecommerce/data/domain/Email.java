package com.enterpriseapplicationsproject.ecommerce.data.domain;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Email {

    private String value = null;

    private String regex = "\\b[A-Za-z0-9._%+-]{1,64}@[A-Za-z0-9.-]{1,254}\\.[A-Za-z]{2,}\\b";

    public Email(String email){

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        if(matcher.find())
            value = email;
    }

    public String getValue(){
        return value;
    }
}
