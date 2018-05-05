package validate;

import model.User;
import model.Watch;
import util.AppUtil;
import constant.Msg;

public class WatchValidate {

	//参数个数正确，类型正确，偏移量不能超过总条数
	public static String get(String id, String offset, String length, String type){
		if(id==null || offset==null || length==null || type==null){
			return(Msg.ERROR_PARA_NUM);}
		if(!(AppUtil.isInteger(id) && AppUtil.isInteger(offset) && AppUtil.isInteger(length))){
			return(Msg.ERROR_PARA_TYPE);}
		if(!Watch.dao.checkOffsetOut(Integer.parseInt(offset),type,Integer.parseInt(id))){
			return(Msg.ERROR_OFFSET_OUT);}
		return null;
	}
	
	//参数个数及类型正确，两个ID不能相同，userID必须存在users表里，不能重复添加
	public static String post(String id, String target_id){
		if(id==null || target_id==null){
			return(Msg.ERROR_PARA_NUM);}
		if(!(AppUtil.isInteger(id) && AppUtil.isInteger(target_id))){
			return(Msg.ERROR_PARA_TYPE);}
		if(id.equals(target_id)){
			return(Msg.ERROR_ID_SAME);}
		if(Watch.dao.checkExisted(Integer.parseInt(id), Integer.parseInt(target_id))){
			return(Msg.ERROR_INSERT_EXISTED);}
		if(!User.dao.checkUserIDExisted(Integer.parseInt(target_id)) || !User.dao.checkUserIDExisted(Integer.parseInt(id))){
			return(Msg.ERROR_USER_ID_UNEXISTED);}
		return null;
	}
	
	//参数个数及类型正确，两个ID不能相同，不能删除不存在的数据
	public static String delete(String id, String target_id){
		if(id==null || target_id==null){
			return(Msg.ERROR_PARA_NUM);}
		if(!(AppUtil.isInteger(id) && AppUtil.isInteger(target_id))){
			return(Msg.ERROR_PARA_TYPE);}
		if(id.equals(target_id)){
			return(Msg.ERROR_ID_SAME);}
		if(!Watch.dao.checkExisted(Integer.parseInt(id), Integer.parseInt(target_id))){
			return(Msg.ERROR_DELETE_UNEXISTED);}
		if(!User.dao.checkUserIDExisted(Integer.parseInt(target_id)) || !User.dao.checkUserIDExisted(Integer.parseInt(id))){
			return(Msg.ERROR_USER_ID_UNEXISTED);}
		
		return null;
	}
	
	public static String getNum(String id){
		//参数不能为空，必须是数字，必须在users表中
		if(id==null){return(Msg.ERROR_PARA_NUM);}
		if(!AppUtil.isInteger(id)){return(Msg.ERROR_PARA_TYPE);}
		if(!User.dao.checkUserIDExisted(Integer.parseInt(id))){return(Msg.ERROR_USER_ID_UNEXISTED);}
		
		return null;
	}
}
