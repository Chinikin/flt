package com.depression.push;

/**
 * 推送消息类型
 *
 * @author hongqian_li
 */
public abstract class CustomMsgType
{  
    /**
     * 系统消息
     */
    public final static int SYSTEM_MSG = 0;

    /**
     * 病友圈评论消息
     */
    public final static int WARDMATE_COMMENTS_MSG = 1;

    /**
     * 病友圈评论被@消息
     */
    public final static int WARDMATE_COMMENTS_TO_AT = 2;

    /**
     * 心事瓶评论消息
     */
    public final static int ANONYMITY_COMMENTS_MSG = 3;

    /**
     * 心事瓶评论被@消息
     */
    public final static int ANONYMITY_COMMENTS_TO_AT = 4;

    /**
     * 文章评论消息被@
     */
    public final static int ARTICLE_COMMENTS_MSG_TO_AT = 5;

    /**
     * 测试评论消息被@
     */
    public final static int TEST_COMMENTS_MSG_TO_AT = 6;

    /**
     * 咨询瓶评论消息
     */
    public final static int ADVISORY_COMMENTS_MSG = 7;
    /**
     * 咨询瓶评论消息被@
     */
    public final static int ADVISORY_COMMENTS_MSG_TO_AT = 8;
    
    /**
     * 回拨电话通知
     */
    public final static int CALL_BACK_MSG = 9;
    
    /**
     * 会员关注
     */
    public final static int MEMBER_CONCERN = 10;
    
    /**
     * 自定义消息推送
     */
    public final static int CUSTOM_PUSH_MSG = 11;
}
