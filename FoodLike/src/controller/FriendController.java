package controller;

import util.AppUtil;
import model.Friend;
import model.User;

import com.jfinal.core.Controller;

import constant.Msg;

public class FriendController extends Controller {
	
	public void index(){
		
		String id = getPara("id");
		String offset = null;
		String length = null;
		String friend_id = null;
		switch(getRequest().getMethod()){
		case "GET":
			offset = getPara("offset");
			length = getPara("length");
			//参数个数正确，类型正确，偏移量不能超过总条数
			if(id==null || offset==null || length==null){
				renderText(Msg.ERROR_PARA_NUM);return;}
			if(!(AppUtil.isInteger(id) && AppUtil.isInteger(offset) && AppUtil.isInteger(length))){
				renderText(Msg.ERROR_PARA_TYPE);return;}
			if(!Friend.dao.checkOffsetOut(Integer.parseInt(offset), Integer.parseInt(id))){
				renderText(Msg.ERROR_OFFSET_OUT);return;}
			
			String result = Friend.dao.getFriends(Integer.parseInt(id),Integer.parseInt(offset),Integer.parseInt(length));
			if(result==null) renderText(Msg.ERROR_UNKNOWN); else renderText(result); break;
		
		case "POST":
			//参数个数及类型正确，两个ID不能相同，userID必须存在users表里，不能重复添加
			friend_id = getPara("friend_id");
			if(id==null || friend_id==null){
				renderText(Msg.ERROR_PARA_NUM);return;}
			if(!(AppUtil.isInteger(id) && AppUtil.isInteger(friend_id))){
				renderText(Msg.ERROR_PARA_TYPE);return;}
			if(id.equals(friend_id)){
				renderText(Msg.ERROR_ID_SAME);return;}
			if(Friend.dao.checkExisted(Integer.parseInt(id), Integer.parseInt(friend_id))){
				renderText(Msg.ERROR_INSERT_EXISTED);return;}
			if(!User.dao.checkUserIDExisted(Integer.parseInt(friend_id)) || !User.dao.checkUserIDExisted(Integer.parseInt(id))){
				renderText(Msg.ERROR_USER_ID_UNEXISTED);return;}
			
			if(Friend.dao.addFriend(Integer.parseInt(id), Integer.parseInt(friend_id))){
				renderText(Msg.SUCCESS_EXECUTE);
			}else{renderText(Msg.ERROR_UNKNOWN);}
			break;
			
		case "DELETE":
			//参数个数及类型正确，两个ID不能相同，不能删除不存在的数据
			friend_id = getPara("friend_id");
			if(id==null || friend_id==null){
				renderText(Msg.ERROR_PARA_NUM);return;}
			if(!(AppUtil.isInteger(id) && AppUtil.isInteger(friend_id))){
				renderText(Msg.ERROR_PARA_TYPE);return;}
			if(id.equals(friend_id)){
				renderText(Msg.ERROR_ID_SAME);return;}
			if(!Friend.dao.checkExisted(Integer.parseInt(id), Integer.parseInt(friend_id))){
				renderText(Msg.ERROR_DELETE_UNEXISTED);return;}
			if(!User.dao.checkUserIDExisted(Integer.parseInt(friend_id)) || !User.dao.checkUserIDExisted(Integer.parseInt(id))){
				renderText(Msg.ERROR_USER_ID_UNEXISTED);return;}
			
			if(Friend.dao.deleteFriend(Integer.parseInt(id), Integer.parseInt(friend_id))){
				renderText(Msg.SUCCESS_EXECUTE);
			}else{renderText(Msg.ERROR_UNKNOWN);}
			break;
			
		case "PUT"://暂无处理
			break;
		}
	}
	
	
}
