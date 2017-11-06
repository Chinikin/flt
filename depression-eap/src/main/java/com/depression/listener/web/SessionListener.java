package com.depression.listener.web;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.depression.entity.Constant;

import java.util.*;

    /**
     * 这个监听器只为防止用户非正常退出
     * 根据session注销用户信息
     * @author admin
     *
     */
    public class SessionListener implements HttpSessionListener,HttpSessionAttributeListener{


    	 //保存当前登录的所有用户
        public static Map<HttpSession,Long> loginUser= new HashMap<HttpSession, Long>();

		@Override
		public void attributeAdded(HttpSessionBindingEvent arg0) {
			// 如果添加的属性是用户ID, 则加入map中
			if (arg0.getName().equals(Constant.ADMIN_LOGINED)) { 
			loginUser.put(arg0.getSession(), Long.valueOf(arg0.getValue().toString()));
			    }
			
		}

		@Override
		public void attributeRemoved(HttpSessionBindingEvent arg0) {
			// 如果移除的属性是用户ID, 则从map中移除
			 if (arg0.getName().equals(Constant.ADMIN_LOGINED)) { 
			        try {
			         loginUser.remove(arg0.getSession());
			        } catch (Exception e) {
			        }
			    }
			
		}

		@Override
		public void attributeReplaced(HttpSessionBindingEvent arg0) {
			// 如果改变的属性是用户ID, 则跟着改变map
			if (arg0.getName().equals(Constant.ADMIN_LOGINED)) { 
			loginUser.put(arg0.getSession(), Long.valueOf(arg0.getValue().toString()));
			}
			
		}

		@Override
		public void sessionCreated(HttpSessionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sessionDestroyed(HttpSessionEvent arg0) {
			// 从map中移除用户ID
			        try {
			         System.out.println("========================session注销========================");
			         loginUser.remove(arg0.getSession());
			        } catch (Exception e) {
			        }
			    }
		
		
}

