package model;

import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import constant.Msg;

public class Friend extends Model<Friend>{
	private static final long serialVersionUID = 1L;
	
	public static final Friend dao = new Friend();
	//SQL语句，这样写直接存在class文件里，提高效率
	public static final String checkExistedSql = "select count(*) as num from friends where one_user_id = ? and another_user_id = ? or another_user_id = ? and one_user_id = ?";
	public static final String getFriendsSql = "select id,nickname,avatar from users where id in (select id from (select one_user_id as id from friends where another_user_id = ? union select another_user_id as id from friends where one_user_id = ? limit ?,?)as news)";
	public static final String getPageCountSql = "select count(*) as num from friends where one_user_id =? or another_user_id=?";
	public static final String AddFriendSql = "insert into friends values (?,?)";
	public static final String DeleteFriendSql = "delete from friends where one_user_id=? and another_user_id=?";
	public static final String checkOffsetOutSql = getPageCountSql;
	
	/**
	 * 数据处理
	 */
	
	//获取好友信息
	public String getFriends(int userID,int offset, int length){
		try{
			//获取总页数
			if(length==0) return Msg.ERROR_PER_PAGE_NUM;
			int pageCount = getPageCount(userID, length);

			//获取friendList，这条SQL语句写的有点长，因为limit和in不能连用，我多加了一个中间表
			List<User> friendList = User.dao.find(getFriendsSql, userID, userID, offset, length);
			return User.dao.toJson(friendList, pageCount);
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
	}
	
	//新增好友关系
	public boolean addFriend(int userID, int friendID){
		try{
			int firstID = userID<friendID?userID:friendID;
			int lastID  = userID>friendID?userID:friendID;
			
			Record record = new Record().set("one_user_id", firstID).set("another_user_id", lastID).set("create_time", new Date());
			Db.save("friends", record);
			return true;
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
	
	//删除好友关系
	public boolean deleteFriend(int userID, int friendID){
		try{
			int firstID = userID<friendID?userID:friendID;
			int lastID  = userID>friendID?userID:friendID;
			
			Db.delete(DeleteFriendSql, firstID, lastID);
			return true;
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
	
	
	
	/**
	 * 工具部分
	 */
	
	//获取总页数
	public int getPageCount(int userID,int length){
		//当查询条数为0时返回0
		int num = dao.find(getPageCountSql, userID, userID).get(0).getInt("num");
		if(num==0) return 0;
		return num%length==0?num/length:num/length+1;
	}
		
	
	
	/**
	 * 数据检测
	 */
	
	//偏移量超出检测
	public boolean checkOffsetOut(int offset, int userID){
		if(offset > Db.find(checkOffsetOutSql, userID, userID).get(0).getInt("num"))	return false;
		else return true;
	}
	
	//数据存在检测
	public boolean checkExisted(int userID, int friendID){
		if(Db.find(checkExistedSql, userID, friendID, userID, friendID).get(0).getInt("num")==0)
			return false;
		else return true;
	}
}
