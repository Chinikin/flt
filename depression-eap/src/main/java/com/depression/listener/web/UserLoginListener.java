package com.depression.listener.web;

import java.util.Set;

import javax.servlet.http.HttpSession;

import com.depression.entity.Constant;

public class UserLoginListener {
	public static boolean isLogonUser(Long userId) {
        Set<HttpSession> keys = SessionListener.loginUser.keySet();
        for (HttpSession key : keys) {
            if (SessionListener.loginUser.get(key).equals(userId)) {
            	key.setAttribute(Constant.ADMIN_LEVEL,"000000");
                return true;
            }
        }
        return false;
}
}
