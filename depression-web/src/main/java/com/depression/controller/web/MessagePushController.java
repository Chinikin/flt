package com.depression.controller.web;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.depression.entity.ErrorCode;
import com.depression.entity.ResultEntity;
import com.depression.model.Member;
import com.depression.model.MessagePush;
import com.depression.model.web.dto.MessagePushDTO;
import com.depression.model.web.vo.MessagePushVO;
import com.depression.service.MemberService;
import com.depression.service.MessagePushService;
import com.depression.utils.Configuration;
import com.depression.utils.PropertyUtils;

@Controller
@RequestMapping("/messagePush")
public class MessagePushController{
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private MessagePushService messagePushService;
	@Autowired
	private MemberService memberService;

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception{
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}
	
	/**
	 * 获取推送列表
	 * @param 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainMsgPushList.json")
	@ResponseBody
	public Object obtainMsgPushList(HttpSession session, HttpServletRequest request, String title, Byte pushStatus,
			Integer pageIndex,Integer pageSize){
		ResultEntity result = new ResultEntity();
		if(PropertyUtils.examineOneNull(pageIndex, pageSize)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		List<MessagePushDTO> messagePushDTOs = new ArrayList<MessagePushDTO>();
		Integer count;
		try{
			
			List<MessagePush> mps = messagePushService.getMessagePushWithPageDesc(title,pushStatus,pageIndex,pageSize);
			count=messagePushService.countMessagePush(title,pushStatus);
			for(MessagePush m:mps){
				MessagePushDTO messagePushDTO = new MessagePushDTO();
				BeanUtils.copyProperties(m, messagePushDTO);
				// 转换实际文件路径
				messagePushDTO.setViewImg(m.getImg());
				messagePushDTOs.add(messagePushDTO);
			}
		}catch(Exception e){
			log.error(ErrorCode.ERROR_DATABASE_QUERY.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_QUERY.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_QUERY.getMessage());
			return result;
		}
		
		result.put("count", count);
		result.put("messagePushDTOs", messagePushDTOs);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;
	}
	
	
	
	/**
	 * 
	 * @param request
	 * @param messagePushVO 创建推送消息（包括IM消息发送） 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveMsgPush.json")
	@ResponseBody
	public Object saveMsgPush(HttpServletRequest request, MessagePushVO messagePushVO){
		ResultEntity result = new ResultEntity();
    	//检验参数
    	if(PropertyUtils.examineOneNull(
    			messagePushVO.getContent(),
    			//messagePushVO.getImg(),
    			messagePushVO.getIsAll(),
    			//messagePushVO.getPushLink(),
    			//messagePushVO.getLinkText(),
    			messagePushVO.getPushTime(),
    			//messagePushVO.getPushType(),
    			messagePushVO.getTitle()
    			//messagePushVO.getPreImg()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
    	
    	//List<Long> mids=new ArrayList<Long>();
    	HashSet<Long> members=new HashSet<Long>(); 
    	
    	//判断是否是所有用户
    	//不是所有用户从excel中读取用户mid
    	if(messagePushVO.getIsAll()==MessagePushService.IS_NOT_ALL){
    	MultipartHttpServletRequest req =(MultipartHttpServletRequest) request;
    	//通过excel获取推送用户信息 添加传递用户资料
    	// 创建迭代器
		Iterator<String> itr =req.getFileNames();
		MultipartFile mpf = null;
		
	
		// 循环文件
		while (itr.hasNext()){
			// 遍历文件
			mpf = req.getFile(itr.next());
			if (mpf != null){
				try{
					
				members=importPushMember(mpf);	
				} catch (Exception e) {
					log.error(ErrorCode.ERROR_UPLOAD_FILE_INCORRECT.getMessage(), e);
					result.setCode(ErrorCode.ERROR_UPLOAD_FILE_INCORRECT.getCode());
					result.setMsg(ErrorCode.ERROR_UPLOAD_FILE_INCORRECT.getMessage());
					return result;
					}
				}
			}
			
    	}
    	MessagePush messagePush=new MessagePush();
    	BeanUtils.copyProperties(messagePushVO, messagePush);
    	//返还数据dto
    	try{
    		 messagePushService.insertMsgPush(messagePush, members);
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_INSERT.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_INSERT.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_INSERT.getMessage());
			return result;
		}
    	result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	
	/**
	 * 推送点击+1
	 * @param mpId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/clickMsgPush.json")
	@ResponseBody
	public Object clickMsgPush(Long mpId){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(mpId)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		try{
			//增加点击数时  
			messagePushService.transcountClickMsgPush(mpId);
			} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	
	/**
	 * 启动消息推送(包括IM消息发送)
	 * @param mpId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/startMsgPush.json")
	@ResponseBody
	public Object startMsgPush(Long mpId){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(mpId)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		try{
			messagePushService.transStartMessagePush(mpId);
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	
	/**
	 * 根据主键获取推送信息
	 * @param mpId 推送信息主键
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/obtainMsgPush.json")
	@ResponseBody
	public Object obtainMsgPush(Long mpId){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(mpId)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		MessagePush messagePush=null;
		try{
			messagePush=messagePushService.obtainMessagePush(mpId);
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		MessagePushDTO messagePushDTO=new MessagePushDTO();
		
		BeanUtils.copyProperties(messagePush, messagePushDTO);
		// 转换实际文件路径
		if(messagePush != null){
			if (messagePush.getImg()!= null && !messagePush.getImg().equals("")){
				messagePushDTO.setViewImg(messagePush.getImg());
			}
		}
		result.put("messagePushDTO", messagePushDTO);
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	/**
	 * 编辑推送信息
	 * @param messagePushVO 更新参数
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/updateMsgPush.json")
	@ResponseBody
	public Object updateMsgPush(HttpServletRequest request,MessagePushVO messagePushVO){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(
				messagePushVO.getMpId(),
    			messagePushVO.getContent(),
    			messagePushVO.getImg(),
    			messagePushVO.getIsAll(),
    			messagePushVO.getPushLink(),
    			messagePushVO.getLinkText(),
    			messagePushVO.getPushTime(),
    			messagePushVO.getPushType(),
    			messagePushVO.getTitle(),
    			messagePushVO.getPreImg()
				)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		HashSet<Long> members=new HashSet<Long>();
		if(messagePushVO.getIsAll()==MessagePushService.IS_NOT_ALL){
	    	MultipartHttpServletRequest req =(MultipartHttpServletRequest) request;
	    	//通过excel获取推送用户信息 添加传递用户资料
	    	// 创建迭代器
			Iterator<String> itr =req.getFileNames();
			MultipartFile mpf = null;
		
			// 循环文件
			while (itr.hasNext())
			{
				// 遍历文件
				mpf = req.getFile(itr.next());
				if (mpf != null)
				{
					try
					{
						members=importPushMember(mpf);	
					} catch (Exception e)
					{
						log.error(ErrorCode.ERROR_UPLOAD_FILE_INCORRECT.getMessage(), e);
						result.setCode(ErrorCode.ERROR_UPLOAD_FILE_INCORRECT.getCode());
						result.setMsg(ErrorCode.ERROR_UPLOAD_FILE_INCORRECT.getMessage());
						return result;
						}
					}
				}
				
	    	}
		
		
		try{
			MessagePush messagePush=new MessagePush();
			BeanUtils.copyProperties(messagePushVO, messagePush);
			messagePushService.updateMessagePush(messagePush,members);
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	
	
	
	/**
	 * 取消推送
	 * @param mpId 推送信息主键
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/cancelMsgPush.json")
	@ResponseBody
	public Object cancelMsgPush(Long mpId){
		ResultEntity result = new ResultEntity();
		
		if(PropertyUtils.examineOneNull(mpId)){
			result.setCode(ErrorCode.ERROR_PARAM_INCOMPLETE.getCode());
			result.setMsg(ErrorCode.ERROR_PARAM_INCOMPLETE.getMessage());
			return result;
		}
		
		try{
			messagePushService.removeMessagePush(mpId);
		} catch (Exception e){
			log.error(ErrorCode.ERROR_DATABASE_UPDATE.getMessage(), e);
			result.setCode(ErrorCode.ERROR_DATABASE_UPDATE.getCode());
			result.setMsg(ErrorCode.ERROR_DATABASE_UPDATE.getMessage());
			return result;
		}
		
		result.setCode(ErrorCode.SUCCESS.getCode());
		result.setMsg(ErrorCode.SUCCESS.getMessage());
		return result;	
	}
	
	
	
	
	public HashSet<Long> importPushMember(MultipartFile mpf) throws Exception{
		HashSet<Long> members=new HashSet<Long>();
			
		 Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(mpf.getBytes()));
		 Sheet sheet = wb.getSheetAt(0);
		 //验证Excel格式
		 String[] row0Str = {"用户id","手机号"};
		 Row row0 = sheet.getRow(0);					 
		 DataFormatter formatter = new DataFormatter();
		 for(int i=0; i<2; i++)
		 {
			 Cell cell = row0.getCell(i);
			 String colName = formatter.formatCellValue(cell);	
	         if(!colName.contains(row0Str[i]))
	         {
	        	 throw new Exception("incorect format");
	         }
		 }
		 
		 
		 //读取Excel中的数据
		 
		 for(int i=1; i<=sheet.getLastRowNum(); i++){
			 Long mid=0l;
			 //System.out.println(sheet.getLastRowNum());
			 Row row = sheet.getRow(i);
			 //用户手机号
			 Cell cellMP = row.getCell(1);
			 //用户id
			 Cell cellId = row.getCell(0);
			 
			 String midStr=formatter.formatCellValue(cellId);
			 String mobile=formatter.formatCellValue(cellMP);
			 
			 if("" == midStr && ""== mobile){
				 break;
			 }
			 
			 if(midStr != ""){
				 mid=Long.parseLong(formatter.formatCellValue(cellId));
			 }
			 if(mobile != ""){
				 Member member=new Member();
				 member.setMobilePhone(mobile);
				 List<Member> ms=memberService.selectByMember(member);
				 mid=ms.get(0).getMid();
			 }
			 
				 members.add(mid);
		 }
		return members;
	}
	
	
}
