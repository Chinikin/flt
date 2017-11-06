/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2016/8/9 9:26:28                             */
/*==============================================================*/


drop table if exists app_release_info;

drop table if exists article;

drop table if exists article_category;

drop table if exists article_collection;

drop table if exists article_comment;

drop table if exists article_detail;

drop table if exists article_share;

drop table if exists article_type;

drop table if exists famous_doctors;

drop table if exists member;

drop table if exists member_advisory;

drop table if exists member_advisory_comment;

drop table if exists member_advisory_detail;

drop table if exists member_advisory_imgs;

drop table if exists member_advisory_praise_num;

drop table if exists member_auth_code;

drop table if exists member_black_list;

drop table if exists member_concern;

drop table if exists member_mood_index;

drop table if exists member_mood_record;

drop table if exists member_private_letter;

drop table if exists member_signin;

drop table if exists member_sleep_quality;

drop table if exists member_step_counter;

drop table if exists member_tag;

drop table if exists member_tag_map;

drop table if exists member_update;

drop table if exists member_update_comment;

drop table if exists member_update_detail;

drop table if exists member_update_embrace;

drop table if exists member_update_imgs;

drop table if exists message_concorn;

drop table if exists personality_characters_similarity;

drop table if exists personality_test_member_mapping;

drop table if exists personality_test_result_desc;

drop table if exists personality_test_share_group;

drop table if exists personality_test_statistics;

drop table if exists picture_description;

drop table if exists push_dev_type;

drop table if exists system_message;

drop table if exists system_userInfo;

drop table if exists testing;

drop table if exists testing_carousel_pictures;

drop table if exists testing_comment;

drop table if exists testing_questions;

drop table if exists testing_questions_options;

drop table if exists testing_result;

drop table if exists testing_result_for_jump;

drop table if exists testing_result_for_member;

drop table if exists testing_score_amount;

drop table if exists testing_section;

drop table if exists testing_share;

drop table if exists testing_type;

drop table if exists unread_comment;

drop table if exists wechar_user_info;

/*==============================================================*/
/* Table: app_release_info                                      */
/*==============================================================*/
create table app_release_info
(
   release_id           bigint not null auto_increment,
   version_name         varchar(30),
   version_num          varchar(30),
   svn_num              int,
   path                 varchar(100),
   is_delete            char(1) default '0',
   note                 text,
   os_type              int default 0,
   primary key (release_id)
);

/*==============================================================*/
/* Table: article                                               */
/*==============================================================*/
create table article
(
   article_id           bigint(20) not null auto_increment comment '主键',
   category_id          bigint(20) comment '科普类别id',
   type_id              bigint(20) comment '文章类型',
   title                varchar(256) comment '文章标题',
   digest               varchar(256) comment '文章摘要',
   hits                 bigint(20) comment '点击数量',
   collection_num       bigint(20) comment '收藏数量',
   author               varchar(20) comment '作者',
   source               varchar(256) comment '来源',
   thumbnail            varchar(64) comment '缩略图',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (article_id)
);

alter table article comment '文章';

/*==============================================================*/
/* Table: article_category                                      */
/*==============================================================*/
create table article_category
(
   category_id          bigint(20) not null auto_increment comment '主键',
   parent_category_id   bigint(20),
   type_id              bigint(20) comment '文章类型',
   category_name        varchar(64) comment '类别名称',
   thumbnail            varchar(64) comment '缩略图',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (category_id)
);

alter table article_category comment '文章类别';

/*==============================================================*/
/* Table: article_collection                                    */
/*==============================================================*/
create table article_collection
(
   col_id               bigint(20) not null auto_increment comment '科普文章收藏id',
   mid                  bigint(20) comment '会员id，关联会员信息表',
   article_id           bigint(20) comment '文章id，关联文章表',
   collection_time      datetime comment '收藏时间',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (col_id)
);

alter table article_collection comment '文章收藏';

/*==============================================================*/
/* Table: article_comment                                       */
/*==============================================================*/
create table article_comment
(
   comment_id           bigint(20) not null auto_increment comment '主键',
   article_id           bigint(20) comment '科普文章id',
   mid                  bigint(20) comment '会员id',
   comment_content      varchar(400) comment '评论内容',
   parent_comment_id    bigint(20) comment '父级评论，指向它的父结点，就是另一条评论的id',
   comment_time         datetime comment '评论时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (comment_id)
);

alter table article_comment comment '对文章的评论，支持盖楼模式，对评论进行评论';

/*==============================================================*/
/* Table: article_detail                                        */
/*==============================================================*/
create table article_detail
(
   detail_id            bigint(20) not null auto_increment comment '主键',
   article_id           bigint(20) comment '科普文章id',
   content              text comment '文章详情',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (detail_id)
);

alter table article_detail comment '文章详情';

/*==============================================================*/
/* Table: article_share                                         */
/*==============================================================*/
create table article_share
(
   share_id             bigint(20) not null auto_increment comment '科普分享id',
   mid                  bigint(20) comment '会员id，关联会员信息表',
   article_id           bigint(20) comment '文章id，关联文章表',
   plateform_type       tinyint comment '分享平台: 
            1. 朋友圈 
            2. 微信 
            3. qq 
            4. 微博',
   share_time           datetime comment '分享时间',
   is_delete            char(1),
   primary key (share_id)
);

alter table article_share comment '文章分享';

/*==============================================================*/
/* Table: article_type                                          */
/*==============================================================*/
create table article_type
(
   type_id              bigint(20) not null auto_increment comment '主键',
   type_name            varchar(64) comment '类型名称',
   is_delete            char(1) default '0' comment '是否删除  默认0:不删除，1:删除',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   primary key (type_id)
);

alter table article_type comment '文章类型';

/*==============================================================*/
/* Table: famous_doctors                                        */
/*==============================================================*/
create table famous_doctors
(
   doct_id              bigint(20) not null auto_increment comment '主键',
   avatar               varchar(256) comment '头像',
   name                 varchar(64) comment '姓名',
   hospital             varchar(256) comment '医院',
   department           varchar(128) comment '科室',
   position             varchar(256) comment '职务',
   specializes          varchar(2048) comment '专业擅长',
   brief_introduction   varchar(2048) comment '简介和头衔',
   is_delete            char(1) default '0' comment '是否删除',
   primary key (doct_id)
);

alter table famous_doctors comment '名医信息';

/*==============================================================*/
/* Table: feedback                                              */
/*==============================================================*/
create table feedback
(
   fid                  bigint(20) not null auto_increment,
   mid                  bigint(20) comment '会员外键',
   f_content            varchar(1000) comment '反馈内容',
   f_time               datetime comment '反馈时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (fid)
);

alter table feedback comment '意见反馈';

/*==============================================================*/
/* Table: image_info                                            */
/*==============================================================*/
create table image_info
(
   id                   bigint not null auto_increment,
   name                 national varchar(128),
   format               national varchar(16),
   width                int,
   height               int,
   color_model          national varchar(32),
   frame_number         int,
   is_delete            int default 0,
   primary key (id)
);

/*==============================================================*/
/* Table: member                                                */
/*==============================================================*/
create table member
(
   mid                  bigint(20) not null auto_increment comment '主键',
   openid               varchar(64) comment '微信身份标识',
   im_account           varchar(128) comment '容联账号',
   im_psw               varchar(128) comment '容联密码',
   mobile_phone         varchar(20) comment '手机号',
   avatar_thumbnail     varchar(256),
   avatar               varchar(256) comment '头像',
   user_name            varchar(64) comment '用户名',
   user_password        varchar(40) comment '密码',
   email                varchar(32) comment '邮箱',
   nickname             varchar(62) comment '昵称',
   hobby                varchar(400) comment '爱好',
   m_level              bigint(20) comment '等级',
   bonus_point          bigint(20) default 0 comment '积分',
   status               tinyint comment '状态',
   user_type            tinyint default 1 comment '用户类型: 1.普通会员 2. 咨询师',
   is_temp              tinyint default 0 comment '是否临时用户：0会员，1游客',
   reg_time             datetime comment '注册时间',
   modify_time          datetime comment '修改时间',
   title                varchar(32),
   answer_count         int default 0 comment '回应的咨询数',
   appreciated_count    int default 0 comment '被感谢次数',
   profile              text comment '简介',
   candid_photo         national varchar(256) comment '生活照',
   location             varchar(64) comment '位置',
   is_delete            int default 0 comment '是否删除',
   is_enable            int default 0,
   primary key (mid)
);

alter table member comment '会员信息';

/*==============================================================*/
/* Index: IDX_mobile_phone                                      */
/*==============================================================*/
create index IDX_mobile_phone on member
(
   mobile_phone
);

/*==============================================================*/
/* Index: IDX_user_name                                         */
/*==============================================================*/
create index IDX_user_name on member
(
   user_name
);

/*==============================================================*/
/* Table: member_advisory                                       */
/*==============================================================*/
create table member_advisory
(
   advisory_id          bigint(20) not null auto_increment comment '动态id',
   mid                  bigint(20) comment '会员id',
   content              varchar(200) comment '内容',
   write_location       varchar(100) comment '发表位置',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (advisory_id)
);

alter table member_advisory comment '会员咨询';

/*==============================================================*/
/* Table: member_advisory_comment                               */
/*==============================================================*/
create table member_advisory_comment
(
   comment_id           bigint(20) not null auto_increment comment '主键',
   mid                  bigint(20) comment '会员id',
   advisory_id          bigint(20) comment '咨询id，关联咨询表',
   parent_id            bigint(20) comment '父级回复，指向它的父结点，就是另一条回复的id',
   praise_num           bigint(20) comment '有用数量',
   comment_content      varchar(200) comment '评论内容',
   comment_time         datetime comment '创建时间',
   read_status          tinyint comment '是否已读：0.未读 1.已读',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (comment_id)
);

alter table member_advisory_comment comment '对会员咨询的回复支持盖楼模式，对回复进行回复';

/*==============================================================*/
/* Table: member_advisory_detail                                */
/*==============================================================*/
create table member_advisory_detail
(
   advisory_detail_id   bigint(20) not null auto_increment comment '主键',
   advisory_id          bigint(20) comment '会员咨询外键',
   detail               text comment '内容',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (advisory_detail_id)
);

alter table member_advisory_detail comment '会员咨询详情';

/*==============================================================*/
/* Table: member_advisory_imgs                                  */
/*==============================================================*/
create table member_advisory_imgs
(
   advisory_img_id      bigint(20) not null auto_increment comment '会员动态中的图片id',
   advisory_id          bigint(20) comment '会员咨询id',
   img_path             varchar(256) comment '图片路径',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   img_preview_path     varchar(256),
   primary key (advisory_img_id)
);

alter table member_advisory_imgs comment '会员咨询中的图片列表';

/*==============================================================*/
/* Table: member_advisory_praise_num                            */
/*==============================================================*/
create table member_advisory_praise_num
(
   praise_num_id        bigint(20) not null auto_increment comment '有用（点赞）ID',
   mid                  bigint(20) comment '会员ID',
   comment_id           bigint(20) comment '评论ID',
   is_delete            char(1) default '0' comment '是否删除',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   primary key (praise_num_id)
);

alter table member_advisory_praise_num comment '会员咨询有用（点赞）记录';

/*==============================================================*/
/* Table: member_auth_code                                      */
/*==============================================================*/
create table member_auth_code
(
   id                   bigint(20) not null auto_increment,
   mobile_phone         varchar(11),
   auth_code            varchar(10),
   create_time          datetime,
   primary key (id)
);

alter table member_auth_code comment '会员注册验证码';

/*==============================================================*/
/* Table: member_black_list                                     */
/*==============================================================*/
create table member_black_list
(
   member_black_id      bigint(20) not null auto_increment comment '主键',
   mid                  bigint(20) comment '会员id',
   black_mid            bigint(20) comment '黑名单会员id',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (member_black_id)
);

alter table member_black_list comment '黑名单';

/*==============================================================*/
/* Table: member_concern                                        */
/*==============================================================*/
create table member_concern
(
   member_concern_id    bigint(20) not null auto_increment comment '主键',
   concern_from         bigint(20) comment '关注人id',
   concern_to           bigint(20) comment '被关注人id',
   concern_time         datetime,
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (member_concern_id)
);

alter table member_concern comment '会员关注';

/*==============================================================*/
/* Table: member_mood_index                                     */
/*==============================================================*/
create table member_mood_index
(
   mood_index_id        bigint(20) not null auto_increment,
   mid                  bigint,
   mood_index           int,
   is_delete            tinyint,
   record_date          datetime,
   primary key (mood_index_id)
);

/*==============================================================*/
/* Table: member_mood_record                                    */
/*==============================================================*/
create table member_mood_record
(
   mood_record_id       bigint(20) not null auto_increment,
   mid                  bigint(20),
   mood_record          varchar(400),
   is_delete            tinyint,
   record_date          datetime,
   primary key (mood_record_id)
);

alter table member_mood_record comment '情绪记录';

/*==============================================================*/
/* Table: member_private_letter                                 */
/*==============================================================*/
create table member_private_letter
(
   member_private_letter_id bigint(20) not null auto_increment comment '主键',
   sender_id            bigint(20) comment '发起方',
   receiver_id          bigint(20) comment '接收方',
   type                 tinyint comment '类型',
   content              varchar(400) comment '内容',
   send_time            datetime comment '发送日期',
   receive_time         datetime comment '接收日期',
   read_status          tinyint,
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (member_private_letter_id)
);

alter table member_private_letter comment '私信';

/*==============================================================*/
/* Table: member_signin                                         */
/*==============================================================*/
create table member_signin
(
   signin_id            bigint(20) not null auto_increment,
   mid                  bigint(20) not null comment '主键',
   create_time          datetime,
   is_delete            char(1),
   primary key (signin_id)
);

/*==============================================================*/
/* Table: member_sleep_quality                                  */
/*==============================================================*/
create table member_sleep_quality
(
   sleep_quality_id     bigint(20) not null auto_increment,
   mid                  bigint,
   sleep_quality        int,
   is_delete            tinyint,
   record_date          datetime,
   primary key (sleep_quality_id)
);

alter table member_sleep_quality comment '睡眠质量';

/*==============================================================*/
/* Table: member_step_counter                                   */
/*==============================================================*/
create table member_step_counter
(
   step_counter_id      bigint(20) not null auto_increment comment '主键',
   mid                  bigint(20) comment '会员外键',
   all_step_counter     bigint(20) comment '总计步数',
   is_delete            char(1) comment '默认0:不删除  1：删除',
   start_time           datetime,
   end_time             datetime,
   primary key (step_counter_id)
);

alter table member_step_counter comment '会员计步';

/*==============================================================*/
/* Table: member_tag                                            */
/*==============================================================*/
create table member_tag
(
   tag_id               bigint not null auto_increment comment '主键',
   type                 tinyint comment '类型',
   phrase               national varchar(64) comment '短语',
   ref_count            int default 0,
   hit_count            int default 0,
   is_delete            int default 0 comment '是否删除',
   is_enable            int default 0,
   primary key (tag_id)
);

alter table member_tag comment '会员标签';

/*==============================================================*/
/* Table: member_tag_map                                        */
/*==============================================================*/
create table member_tag_map
(
   map_id               bigint not null auto_increment comment '主键',
   mid                  bigint(20) comment '会员id',
   tag_id               bigint comment '标签',
   is_delete            int default 0 comment '是否删除',
   is_enable            int default 0,
   primary key (map_id)
);

alter table member_tag_map comment '会员标签映射';

/*==============================================================*/
/* Table: member_update                                         */
/*==============================================================*/
create table member_update
(
   update_id            bigint(20) not null auto_increment comment '动态id',
   mid                  bigint(20) comment '会员id',
   content              varchar(200) comment '内容',
   embrace_num          bigint(20) comment '拥抱（点赞）数量',
   write_location       varchar(100) comment '发表位置',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   update_type          int comment '动态类型  0不匿名（病友圈） 1匿名发表（心事瓶）',
   primary key (update_id)
);

alter table member_update comment '会员动态';

/*==============================================================*/
/* Table: member_update_comment                                 */
/*==============================================================*/
create table member_update_comment
(
   comment_id           bigint(20) not null auto_increment comment '主键',
   mid                  bigint(20) comment '会员id',
   update_id            bigint(20) comment '试卷id，关联试卷表',
   comment_content      varchar(200) comment '评论内容',
   parent_id            bigint(20) comment '父级评论，指向它的父结点，就是另一条评论的id',
   comment_time         datetime comment '创建时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (comment_id)
);

alter table member_update_comment comment '对试卷的评论，支持盖楼模式，对评论进行评论';

/*==============================================================*/
/* Table: member_update_detail                                  */
/*==============================================================*/
create table member_update_detail
(
   update_detail_id     bigint(20) not null auto_increment comment '主键',
   update_id            bigint(20) comment '会员动态外键',
   detail               text comment '内容',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (update_detail_id)
);

alter table member_update_detail comment '会员动态详情';

/*==============================================================*/
/* Table: member_update_embrace                                 */
/*==============================================================*/
create table member_update_embrace
(
   emberace_id          bigint(20) not null auto_increment,
   update_id            bigint(20) comment '动态ID',
   mid                  bigint(20),
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_delete            char(1) default '0' comment '是否删除',
   primary key (emberace_id)
);

alter table member_update_embrace comment '会员动态点赞';

/*==============================================================*/
/* Table: member_update_imgs                                    */
/*==============================================================*/
create table member_update_imgs
(
   upd_img_id           bigint(20) not null auto_increment comment '会员动态中的图片id',
   update_id            bigint(20) comment '会员动态id',
   img_path             varchar(256) comment '图片路径',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   img_preview_path     varchar(256),
   primary key (upd_img_id)
);

alter table member_update_imgs comment '会员动态中的图片列表';

/*==============================================================*/
/* Table: message_concorn                                       */
/*==============================================================*/
create table message_concorn
(
   message_id           bigint not null auto_increment,
   mid                  bigint(20) comment '主键',
   is_delete            char(1) default '0',
   primary key (message_id)
);

/*==============================================================*/
/* Table: personality_characters_similarity                     */
/*==============================================================*/
create table personality_characters_similarity
(
   pcs_id               bigint(20) not null auto_increment comment '主键',
   g_id                 bigint(20),
   mid                  bigint(20),
   similarity           decimal(5,4) comment '相似度',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_enable            tinyint default 0 comment '0启用，1禁用',
   is_delete            tinyint default 0 comment '0正常，1删除',
   primary key (pcs_id)
);

alter table personality_characters_similarity comment '性格相似度';

/*==============================================================*/
/* Table: personality_test_member_mapping                       */
/*==============================================================*/
create table personality_test_member_mapping
(
   mapping_id           bigint(20) not null auto_increment comment '主键',
   mid                  bigint(20),
   ptrd_id              bigint(20),
   create_time          datetime,
   modify_time          datetime,
   is_enable            tinyint default 0 comment '0启用，1禁用',
   is_delete            tinyint default 0 comment '0正常，1删除',
   primary key (mapping_id)
);

alter table personality_test_member_mapping comment '会员测试结果映射';

/*==============================================================*/
/* Table: personality_test_result_desc                          */
/*==============================================================*/
create table personality_test_result_desc
(
   ptrd_id              bigint(20) not null auto_increment comment '主键',
   type                 national varchar(3) comment 'D，I，S，C四种人格',
   descp                text comment '描述',
   pic                  national varchar(256) comment '图片',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_enable            tinyint default 0 comment '0启用，1禁用',
   is_delete            tinyint default 0 comment '0正常，1删除',
   primary key (ptrd_id)
);

alter table personality_test_result_desc comment '测试结果描述';

/*==============================================================*/
/* Table: personality_test_share_group                          */
/*==============================================================*/
create table personality_test_share_group
(
   g_id                 bigint(20) not null auto_increment comment '主键id',
   mid                  bigint(20) comment '分享会员id',
   link                 national varchar(512) comment '链接',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_enable            tinyint default 0 comment '0启用，1禁用',
   is_delete            tinyint default 0 comment '0正常，1删除',
   primary key (g_id)
);

alter table personality_test_share_group comment '测试分享分组';

/*==============================================================*/
/* Table: personality_test_statistics                           */
/*==============================================================*/
create table personality_test_statistics
(
   pts_id               bigint(20) not null auto_increment comment '主键',
   mid                  bigint(20) comment '会员id',
   d_val                int comment 'M中D选项总数-L中D选项总数',
   i_val                int comment 'M中I选项总数-L中I选项总数',
   s_val                int comment 'M中S选项总数-L中S选项总数',
   c_val                int comment 'M中C选项总数-L中C选项总数',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_enable            tinyint default 0 comment '0启用，1禁用',
   is_delete            tinyint default 0 comment '0正常，1删除',
   primary key (pts_id)
);

alter table personality_test_statistics comment '测试结果统计';

/*==============================================================*/
/* Table: picture_description                                   */
/*==============================================================*/
create table picture_description
(
   id                   bigint not null auto_increment,
   name                 national varchar(128),
   is_delete            int default 0,
   original             bigint,
   fixed_300            bigint,
   primary key (id)
);

/*==============================================================*/
/* Table: push_dev_type                                         */
/*==============================================================*/
create table push_dev_type
(
   push_id              bigint(20) not null auto_increment comment '推送ID',
   channel_id           varchar(64) comment '唯一对应一台设备',
   user_id              varchar(64) comment '用户标识ID',
   mid                  bigint(20),
   primary key (push_id)
);

alter table push_dev_type comment '推送设备标识类型';

/*==============================================================*/
/* Table: system_message                                        */
/*==============================================================*/
create table system_message
(
   message_id           bigint(20) not null auto_increment comment '主键',
   mid                  bigint(20) comment '主键',
   send_time            datetime comment '发送时间',
   read_status          tinyint comment '是否已读：0.未读 1.已读',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   type                 tinyint,
   ref_id               bigint,
   primary key (message_id)
);

alter table system_message comment '系统消息';

/*==============================================================*/
/* Table: system_userInfo                                       */
/*==============================================================*/
create table system_userInfo
(
   user_id              bigint(20) not null auto_increment comment '主键',
   show_name            varchar(64) comment '显示名',
   user_name            varchar(64) comment '用户名',
   user_psw             varchar(64) comment '密码',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (user_id)
);

alter table system_userInfo comment '系统用户表';

/*==============================================================*/
/* Table: testing                                               */
/*==============================================================*/
create table testing
(
   testing_id           bigint(20) not null auto_increment comment '主键',
   type_id              bigint(20) comment '主键',
   calc_method          int default 0 comment '计算方式：0计分，1跳转',
   title                varchar(64) comment '标题',
   subtitle             varchar(1000) comment '副标题',
   content_explain      text comment '正文说明（图文）',
   thumbnail            varchar(64) comment '缩略图（PC）',
   thumbnail_mobile     varchar(64) comment '移动端配图',
   thumbnail_slide      varchar(64) comment '滑动配图',
   questions_num        int comment '试题数量',
   testing_people_num   bigint(20) default 0 comment '测试人数',
   testing_comment_people_num bigint(20) default 0 comment '评论人数',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (testing_id)
);

alter table testing comment '试卷';

/*==============================================================*/
/* Table: testing_carousel_pictures                             */
/*==============================================================*/
create table testing_carousel_pictures
(
   cpid                 bigint(20) not null auto_increment,
   type                 int default 0 comment '问卷类型: 0. 专业测试，1. 趣味测试',
   descp                varchar(1000),
   img_path             varchar(256),
   testing_id           bigint(20),
   is_delete            char(1) default '0',
   primary key (cpid)
);

alter table testing_carousel_pictures comment '问卷轮播图片';

/*==============================================================*/
/* Table: testing_comment                                       */
/*==============================================================*/
create table testing_comment
(
   comment_id           bigint(20) not null auto_increment comment '主键',
   testing_id           bigint(20) comment '试卷id，关联试卷表',
   mid                  bigint(20) comment '会员id',
   test_comment_id      bigint(20) comment '主键',
   comment_content      varchar(2048) comment '评论内容',
   comment_time         datetime comment '评论时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (comment_id)
);

alter table testing_comment comment '对试卷的评论，支持盖楼模式，对评论进行评论';

/*==============================================================*/
/* Table: testing_questions                                     */
/*==============================================================*/
create table testing_questions
(
   questions_id         bigint(20) not null auto_increment comment '题目id',
   testing_id           bigint(20) comment '问卷id, 关联测试问卷表',
   subject_seq_num      int comment '题目序号',
   questions_title      varchar(64) comment '试题标题',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (questions_id)
);

alter table testing_questions comment '试题';

/*==============================================================*/
/* Table: testing_questions_options                             */
/*==============================================================*/
create table testing_questions_options
(
   options_id           bigint(20) not null auto_increment comment '主键',
   questions_id         bigint(20) comment '问卷试题id',
   sequence             int comment '选项序号',
   title                varchar(64) comment '选项标题',
   score                int comment '得分',
   opt_type             int default 0 comment '选项类型：0跳转，1最终结果',
   jump_to_question_no  int comment '跳转至题号(跳转)',
   jump_result_tag      varchar(10) comment '结果标签(跳转) : A, B, C,...',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (options_id)
);

alter table testing_questions_options comment '试题选项';

/*==============================================================*/
/* Table: testing_result                                        */
/*==============================================================*/
create table testing_result
(
   result_id            bigint(20) not null auto_increment comment '主键',
   mid                  bigint(20) comment '会员id，关联会员信息表',
   testing_id           bigint(20),
   questions_id         bigint(20),
   options_id           bigint(20) comment '试题选择id，关联试题选项表',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (result_id)
);

alter table testing_result comment '会员测试结果';

/*==============================================================*/
/* Table: testing_result_for_jump                               */
/*==============================================================*/
create table testing_result_for_jump
(
   res_id               bigint(20) not null auto_increment,
   testing_id           bigint(20) comment '问卷id，关联测试问卷表',
   result_tag           varchar(10),
   title                varchar(256),
   thumbnail            varchar(64) comment '图片描述',
   detail               text comment '正文描述',
   is_delete            char(1) default '0' comment '是否删除',
   primary key (res_id)
);

alter table testing_result_for_jump comment '问卷结论（跳转）';

/*==============================================================*/
/* Table: testing_result_for_member                             */
/*==============================================================*/
create table testing_result_for_member
(
   tid                  bigint(20) not null auto_increment,
   mid                  bigint(20) comment '会员外键',
   res_id               bigint(20) comment '问卷结论（跳转）外键',
   test_time            datetime comment '测试时间',
   is_delete            char(1) default '0' comment '是否删除',
   primary key (tid)
);

alter table testing_result_for_member comment '会员测试结论（跳转问卷）';

/*==============================================================*/
/* Table: testing_score_amount                                  */
/*==============================================================*/
create table testing_score_amount
(
   score_id             bigint(20) not null auto_increment comment '主键',
   mid                  bigint(20) comment '会员id，关联会员信息表',
   testing_id           bigint(20) comment '试卷id',
   score                int comment '总分，各选项分数总和',
   test_time            datetime comment '测试时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (score_id)
);

alter table testing_score_amount comment '会员测试总分（计分问卷）';

/*==============================================================*/
/* Table: testing_section                                       */
/*==============================================================*/
create table testing_section
(
   section_id           bigint(20) not null auto_increment comment '主键',
   testing_id           bigint(20) comment '问卷id，关联测试问卷表',
   greater_than         int comment '得分高于',
   less_than            int comment '得分低于',
   level                varchar(30) comment '结果等级: A,B,C...等等',
   detail               text comment '结果详细描述',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (section_id)
);

alter table testing_section comment '问卷测试得分区间';

/*==============================================================*/
/* Table: testing_share                                         */
/*==============================================================*/
create table testing_share
(
   share_id             bigint(20) not null auto_increment comment '科普分享id',
   mid                  bigint(20) comment '会员id，关联会员信息表',
   testing_id           bigint(20) comment '试卷id，关联试卷表',
   plateform_type       tinyint comment '分享平台: 
            1. 朋友圈 
            2. 微信 
            3. qq 
            4. 微博',
   share_time           datetime comment '分享时间',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (share_id)
);

alter table testing_share comment '试卷分享';

/*==============================================================*/
/* Table: testing_type                                          */
/*==============================================================*/
create table testing_type
(
   type_id              bigint(20) not null auto_increment comment '主键',
   parent_id            bigint(20) comment '父级类别id',
   testing_name         varchar(64) comment '试卷类别名称',
   ts_type              int(4) comment '问卷类型：0专业测试，1趣味测试',
   thumbnail            varchar(64) comment '缩略图',
   create_time          datetime comment '创建时间',
   modify_time          datetime comment '修改时间',
   is_delete            char(1) default '0' comment '默认0:不删除  1：删除',
   primary key (type_id)
);

alter table testing_type comment '试卷类别';

/*==============================================================*/
/* Table: unread_comment                                        */
/*==============================================================*/
create table unread_comment
(
   unread_id            bigint not null auto_increment,
   mid                  bigint(20) comment '主键',
   comment_id           bigint,
   type                 tinyint,
   primary key (unread_id)
);

/*==============================================================*/
/* Table: wechar_user_info                                      */
/*==============================================================*/
create table wechar_user_info
(
   openid               varchar(64) not null comment '身份标识',
   nickname             varchar(256) comment '昵称',
   sex                  char(1) comment '性别',
   city                 varchar(64) comment '市',
   country              varchar(64) comment '国家',
   province             varchar(64) comment '省',
   headimgurl           varchar(1024) comment '头像地址',
   create_time          datetime,
   modify_time          datetime,
   primary key (openid)
);

alter table wechar_user_info comment '微信用户信息';

alter table article add constraint FK_article_Reference_article_type foreign key (type_id)
      references article_type (type_id) on delete restrict on update restrict;

alter table article add constraint FK_science_education_doc_Reference_science_education_type foreign key (category_id)
      references article_category (category_id) on delete restrict on update restrict;

alter table article_category add constraint FK_article_category_Reference_article_type foreign key (type_id)
      references article_type (type_id) on delete restrict on update restrict;

alter table article_collection add constraint FK_article_collection_Reference_article foreign key (article_id)
      references article (article_id) on delete restrict on update restrict;

alter table article_collection add constraint FK_science_education_doc_collection_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table article_comment add constraint FK_science_education_doc_comment_Reference_doc foreign key (article_id)
      references article (article_id) on delete restrict on update restrict;

alter table article_comment add constraint FK_science_education_doc_comment_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table article_detail add constraint FK_article_detail_Reference_article foreign key (article_id)
      references article (article_id) on delete restrict on update restrict;

alter table article_share add constraint FK_science_education_doc_share_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table article_share add constraint FK_science_education_doc_share_Reference_science_education_doc foreign key (article_id)
      references article (article_id) on delete restrict on update restrict;

alter table feedback add constraint FK_feedback_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table member add constraint FK_Reference_46 foreign key (openid)
      references wechar_user_info (openid) on delete restrict on update restrict;

alter table member_advisory add constraint FK_member_advisory_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table member_advisory_comment add constraint FK_member_advisory_comment_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table member_advisory_comment add constraint FK_member_advisory_comment_Reference_member_advisory foreign key (advisory_id)
      references member_advisory (advisory_id) on delete restrict on update restrict;

alter table member_advisory_detail add constraint FK_member_advisory_detail_Reference_member_advisory foreign key (advisory_id)
      references member_advisory (advisory_id) on delete restrict on update restrict;

alter table member_advisory_imgs add constraint FK_member_advisory_imgs_Reference_member_advisory foreign key (advisory_id)
      references member_advisory (advisory_id) on delete restrict on update restrict;

alter table member_black_list add constraint FK_member_black_list_Reference_member_1 foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table member_black_list add constraint FK_member_black_list_Reference_member_2 foreign key (black_mid)
      references member (mid) on delete restrict on update restrict;

alter table member_concern add constraint FK_member_concern_Reference_member_1 foreign key (concern_from)
      references member (mid) on delete restrict on update restrict;

alter table member_concern add constraint FK_member_concern_Reference_member_2 foreign key (concern_to)
      references member (mid) on delete restrict on update restrict;

alter table member_private_letter add constraint FK_member_private_letter_Reference_member_1 foreign key (sender_id)
      references member (mid) on delete restrict on update restrict;

alter table member_private_letter add constraint FK_member_private_letter_Reference_member_2 foreign key (receiver_id)
      references member (mid) on delete restrict on update restrict;

alter table member_signin add constraint FK_signin_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table member_step_counter add constraint FK_member_step_counter_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table member_tag_map add constraint FK_Reference_56 foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table member_tag_map add constraint FK_Reference_57 foreign key (tag_id)
      references member_tag (tag_id) on delete restrict on update restrict;

alter table member_update add constraint FK_member_update_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table member_update_comment add constraint FK_member_update_comment_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table member_update_comment add constraint FK_member_update_comment_Reference_member_update foreign key (update_id)
      references member_update (update_id) on delete restrict on update restrict;

alter table member_update_detail add constraint FK_member_update_detail_Reference_member_update foreign key (update_id)
      references member_update (update_id) on delete restrict on update restrict;

alter table member_update_imgs add constraint FK_member_update_imgs_Reference_member_update foreign key (update_id)
      references member_update (update_id) on delete restrict on update restrict;

alter table message_concorn add constraint FK_Reference_49 foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table personality_characters_similarity add constraint FK_personality_characters_similarity_Reference_group foreign key (g_id)
      references personality_test_share_group (g_id) on delete restrict on update restrict;

alter table personality_characters_similarity add constraint FK_personality_characters_similarity_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table personality_test_member_mapping add constraint FK_mapping_Reference_personality_test_result_desc foreign key (ptrd_id)
      references personality_test_result_desc (ptrd_id) on delete restrict on update restrict;

alter table personality_test_share_group add constraint FK_personality_test_share_group_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table personality_test_statistics add constraint FK_Reference_58 foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table picture_description add constraint FK_Reference_52 foreign key (original)
      references image_info (id) on delete restrict on update restrict;

alter table picture_description add constraint FK_Reference_53 foreign key (fixed_300)
      references image_info (id) on delete restrict on update restrict;

alter table system_message add constraint FK_system_message_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table testing add constraint FK_testing_Reference_testing_type foreign key (type_id)
      references testing_type (type_id) on delete restrict on update restrict;

alter table testing_comment add constraint FK_testing_comment_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table testing_comment add constraint FK_testing_comment_Reference_self foreign key (test_comment_id)
      references testing_comment (comment_id) on delete restrict on update restrict;

alter table testing_comment add constraint FK_testing_comment_Reference_testing foreign key (testing_id)
      references testing (testing_id) on delete restrict on update restrict;

alter table testing_questions add constraint FK_testing_questions_Reference_testing foreign key (testing_id)
      references testing (testing_id) on delete restrict on update restrict;

alter table testing_questions_options add constraint FK_testing_questions_options_Reference_testing_questions foreign key (questions_id)
      references testing_questions (questions_id) on delete restrict on update restrict;

alter table testing_result add constraint FK_testing_result_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table testing_result add constraint FK_testing_result_Reference_testing foreign key (testing_id)
      references testing (testing_id) on delete restrict on update restrict;

alter table testing_result add constraint FK_testing_result_Reference_testing_questions foreign key (questions_id)
      references testing_questions (questions_id) on delete restrict on update restrict;

alter table testing_result add constraint FK_testing_result_Reference_testing_questions_options foreign key (options_id)
      references testing_questions_options (options_id) on delete restrict on update restrict;

alter table testing_result_for_jump add constraint FK_testing_result_for_jump_Reference_testing foreign key (testing_id)
      references testing (testing_id) on delete restrict on update restrict;

alter table testing_result_for_member add constraint FK_testing_result_for_member_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table testing_result_for_member add constraint FK_testing_result_for_member_Reference_testing_result_for_jump foreign key (res_id)
      references testing_result_for_jump (res_id) on delete restrict on update restrict;

alter table testing_score_amount add constraint FK_testing_score_amount_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table testing_score_amount add constraint FK_testing_score_amount_Reference_testing foreign key (testing_id)
      references testing (testing_id) on delete restrict on update restrict;

alter table testing_section add constraint FK_testing_section_Reference_testing foreign key (testing_id)
      references testing (testing_id) on delete restrict on update restrict;

alter table testing_share add constraint FK_testing_share_Reference_member foreign key (mid)
      references member (mid) on delete restrict on update restrict;

alter table testing_share add constraint FK_testing_share_Reference_testing foreign key (testing_id)
      references testing (testing_id) on delete restrict on update restrict;

alter table testing_type add constraint FK_testing_type_Reference_self foreign key (parent_id)
      references testing_type (type_id) on delete restrict on update restrict;

alter table unread_comment add constraint FK_Reference_48 foreign key (mid)
      references member (mid) on delete restrict on update restrict;

