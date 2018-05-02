package controller;

import model.User;
import model.Watch;
import util.AppUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.GET;

import constant.Msg;

public class WatchController extends Controller{

	public void index(){
		
		String id = getPara("id");
		String offset = null;
		String length = null;
		String type = null;
		String target_id = null;
		
		switch(getRequest().getMethod()){
		case "GET" :
			offset = getPara("offset");
			length = getPara("length");
			type = getPara("type");
			//参数个数正确，类型正确，偏移量不能超过总条数
			if(id==null || offset==null || length==null || type==null){
				renderText(Msg.ERROR_PARA_NUM);return;}
			if(!(AppUtil.isInteger(id) && AppUtil.isInteger(offset) && AppUtil.isInteger(length))){
				renderText(Msg.ERROR_PARA_TYPE);return;}
			if(!Watch.dao.checkOffsetOut(Integer.parseInt(offset),type,Integer.parseInt(id))){
				renderText(Msg.ERROR_OFFSET_OUT);return;}
			
			String result = Watch.dao.getWatch(Integer.parseInt(id),Integer.parseInt(offset),Integer.parseInt(length),type);
			if(result==null) renderText(Msg.ERROR_UNKNOWN); else renderText(result); break;
			
		case "POST" :
			//参数个数及类型正确，两个ID不能相同，userID必须存在users表里，不能重复添加
			target_id = getPara("target_id");
			if(id==null || target_id==null){
				renderText(Msg.ERROR_PARA_NUM);return;}
			if(!(AppUtil.isInteger(id) && AppUtil.isInteger(target_id))){
				renderText(Msg.ERROR_PARA_TYPE);return;}
			if(id.equals(target_id)){
				renderText(Msg.ERROR_ID_SAME);return;}
			if(Watch.dao.checkExisted(Integer.parseInt(id), Integer.parseInt(target_id))){
				renderText(Msg.ERROR_INSERT_EXISTED);return;}
			if(!User.dao.checkUserIDExisted(Integer.parseInt(target_id)) || !User.dao.checkUserIDExisted(Integer.parseInt(id))){
				renderText(Msg.ERROR_USER_ID_UNEXISTED);return;}
			
			if(Watch.dao.addWatch(Integer.parseInt(id), Integer.parseInt(target_id))){
				renderText(Msg.SUCCESS_EXECUTE);
			}else{renderText(Msg.ERROR_UNKNOWN);}
			
			break;
			
		case "DELETE" :
			//参数个数及类型正确，两个ID不能相同，不能删除不存在的数据
			target_id = getPara("target_id");
			if(id==null || target_id==null){
				renderText(Msg.ERROR_PARA_NUM);return;}
			if(!(AppUtil.isInteger(id) && AppUtil.isInteger(target_id))){
				renderText(Msg.ERROR_PARA_TYPE);return;}
			if(id.equals(target_id)){
				renderText(Msg.ERROR_ID_SAME);return;}
			if(!Watch.dao.checkExisted(Integer.parseInt(id), Integer.parseInt(target_id))){
				renderText(Msg.ERROR_DELETE_UNEXISTED);return;}
			if(!User.dao.checkUserIDExisted(Integer.parseInt(target_id)) || !User.dao.checkUserIDExisted(Integer.parseInt(id))){
				renderText(Msg.ERROR_USER_ID_UNEXISTED);return;}
			
			if(Watch.dao.deleteWatch(Integer.parseInt(id), Integer.parseInt(target_id))){
				renderText(Msg.SUCCESS_EXECUTE);
			}else{renderText(Msg.ERROR_UNKNOWN);}
			break;
		case "PUT" ://暂不处理
			break;
		}
	}
	
	@Before(GET.class)
	public void getNum(){
		String id = getPara("id");
		
		//参数不能为空，必须是数字，必须在users表中
		if(id==null){renderText(Msg.ERROR_PARA_NUM);return;}
		if(!AppUtil.isInteger(id)){renderText(Msg.ERROR_PARA_TYPE);return;}
		if(!User.dao.checkUserIDExisted(Integer.parseInt(id))){renderText(Msg.ERROR_USER_ID_UNEXISTED);return;}
		
		String result = Watch.dao.numtoJson(Integer.parseInt(id));
		if(result==null) renderText(Msg.ERROR_UNKNOWN); else renderText(result);
	}
	
}
