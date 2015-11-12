package com.cjq.aijia.domain;

import com.orm.SugarRecord;

/**
 * Created by CJQ on 2015/11/12.
 */
public class User extends SugarRecord<User>{
    String userName;
    String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User() {
    }
}
