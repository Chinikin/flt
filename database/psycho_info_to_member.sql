delimiter $$    
drop procedure if exists psycho_info_to_member; 
create procedure psycho_info_to_member()     
BEGIN    
    declare fid long default 0;
    declare nid long default 0;  
    declare done int default -1;  
      
    /* 声明游标 */  
    declare myCursor cursor for select piid from psycho_info;  
      
    /* 当游标到达尾部时，mysql自动设置done=1 */     
    declare continue handler for not found set done=1;  
      
    /* 打开游标 */  
    open myCursor;  
      
    /* 循环开始 */  
    myLoop: LOOP  
      
        /* 移动游标并赋值 */  
        fetch myCursor into fid;  
          
        if done = 1 then   
        leave myLoop;  
        end if;  
          
        /* do something */
        set @mid = (select mid from psycho_info where piid=fid);
        if @mid is not null then
          set @photo_certification_dealt_rel = (select photo_certification_dealt_rel from psycho_info where piid=fid);
          set @photo_certification_dealt_preview_rel = (select photo_certification_dealt_preview_rel from psycho_info where piid=fid);
          set @photo_identity_card_dealt_rel = (select photo_identity_card_dealt_rel from psycho_info where piid=fid);
          set @photo_identity_card_dealt_preview_rel = (select photo_identity_card_dealt_preview_rel from psycho_info where piid=fid);
          /*填充mid pid*/
          update member set photo_certification_dealt_rel=@photo_certification_dealt_rel, photo_certification_dealt_preview_rel=@photo_certification_dealt_preview_rel, photo_identity_card_dealt_rel=@photo_identity_card_dealt_rel, photo_identity_card_dealt_preview_rel=@photo_identity_card_dealt_preview_rel where mid=@mid;
        end if;
    /* 循环结束 */  
    end loop myLoop;  
      
    /* 关闭游标 */  
    close myCursor;  
END $$    
delimiter ;   

call psycho_info_to_member();
