package validate;

import model.Friend;
import model.User;
import util.AppUtil;
import constant.Msg;

public class FriendValidate {

	//参数个数正确，类型正确，偏移量不能超过总条数
	public static String get(String id, String offset, String length){
		String result = null;
		if(id==null || offset==null || length==null)
			result = Msg.ERROR_PARA_NUM;
		if(!(AppUtil.isInteger(id) && AppUtil.isInteger(offset) && AppUtil.isInteger(length)))
			result = Msg.ERROR_PARA_TYPE;
		if(!Friend.dao.checkOffsetOut(Integer.parseInt(offset), Integer.parseInt(id)))
			result = Msg.ERROR_OFFSET_OUT;
		
		return result;
	}
	
	//参数个数及类型正确，两个ID不能相同，userID必须存在users表里，不能重复添加
	public static String post(String id, String friend_id){
		String result = null;
		if(id==null || friend_id==null)
			result = Msg.ERROR_PARA_NUM;
		if(!(AppUtil.isInteger(id) && AppUtil.isInteger(friend_id)))
			result = Msg.ERROR_PARA_TYPE;
		if(id.equals(friend_id))
			result = Msg.ERROR_ID_SAME;
		if(Friend.dao.checkExisted(Integer.parseInt(id), Integer.parseInt(friend_id)))
			result = Msg.ERROR_INSERT_EXISTED;
		if(!User.dao.checkUserIDExisted(Integer.parseInt(friend_id)) || !User.dao.checkUserIDExisted(Integer.parseInt(id)))
			result = Msg.ERROR_USER_ID_UNEXISTED;
		
		return result;
	}
	
	//参数个数及类型正确，两个ID不能相同，不能删除不存在的数据
	public static String delete(String id, String friend_id){
		String result = null;
		if(id==null || friend_id==null)
			result = Msg.ERROR_PARA_NUM;
		if(!(AppUtil.isInteger(id) && AppUtil.isInteger(friend_id)))
			result = Msg.ERROR_PARA_TYPE;
		if(id.equals(friend_id))
			result = Msg.ERROR_ID_SAME;
		if(!Friend.dao.checkExisted(Integer.parseInt(id), Integer.parseInt(friend_id)))
			result = Msg.ERROR_DELETE_UNEXISTED;
		if(!User.dao.checkUserIDExisted(Integer.parseInt(friend_id)) || !User.dao.checkUserIDExisted(Integer.parseInt(id)))
			result = Msg.ERROR_USER_ID_UNEXISTED;
		
		return result;
	}
}
