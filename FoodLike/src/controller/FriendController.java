package controller;

import validate.FriendValidate;
import model.Friend;
import com.jfinal.core.Controller;
import constant.Msg;

public class FriendController extends Controller {
	
	public void index(){
		
		String id = getPara("id");
		String offset = null;
		String length = null;
		String friend_id = null;
		String valid = null;
		switch(getRequest().getMethod()){
		case "GET":
			offset = getPara("offset");
			length = getPara("length");

			valid = FriendValidate.get(id, offset, length);
			if(valid!=null){renderText(valid);return;}
			
			String result = Friend.dao.getFriends(Integer.parseInt(id),Integer.parseInt(offset),Integer.parseInt(length));
			if(result==null) renderText(Msg.ERROR_UNKNOWN); else renderText(result); break;
		
		case "POST":
			friend_id = getPara("friend_id");
			
			valid = FriendValidate.post(id, friend_id);
			if(valid!=null){renderText(valid);return;}
			
			if(Friend.dao.addFriend(Integer.parseInt(id), Integer.parseInt(friend_id))){
				renderText(Msg.SUCCESS_EXECUTE);
			}else{renderText(Msg.ERROR_UNKNOWN);}
			break;
			
		case "DELETE":
			
			friend_id = getPara("friend_id");
			
			valid = FriendValidate.delete(id, friend_id);
			if(valid!=null){renderText(valid);return;}
			
			if(Friend.dao.deleteFriend(Integer.parseInt(id), Integer.parseInt(friend_id))){
				renderText(Msg.SUCCESS_EXECUTE);
			}else{renderText(Msg.ERROR_UNKNOWN);}
			break;
			
		case "PUT"://暂无处理
			break;
		}
	}
	
	
}
