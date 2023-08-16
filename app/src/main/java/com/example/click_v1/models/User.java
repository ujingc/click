package com.example.click_v1.models;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    public String name, image, email, token, id, selfIntroduction, location, country, gender;
    public List<String> hobbies;
}
