package model;

import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import constant.Msg;

public class Watch extends Model<Watch>{

	private static final long serialVersionUID = 1L;
	public static final Watch dao = new Watch();

	//SQL语句
	public static final String checkExistedSql = "select count(*) as num from watch where watcher_id = ? and target_id = ?";
	public static final String getTargetsSql = "select id,nickname,avatar from users where id in (select target_id from (select target_id from watch where watcher_id = ? limit ?,?)as news)";
	public static final String getWatchersSql = "select id,nickname,avatar from users where id in (select watcher_id from (select watcher_id from watch where target_id = ? limit ?,?) as news)";
	public static final String eachWatchedSql = "select id,nickname,avatar from users where id in (select watcher_id from (select watcher_id from watch where target_id=? and watcher_id in (select target_id from watch where watcher_id=?) limit ?,?) as news)";
	public static final String getTargetPageCountSql = "select count(*) as num from watch where watcher_id=?";
	public static final String getWatcherPageCountSql = "select count(*) as num from watch where target_id=?";
	public static final String eachWatchedPageCountSql = "select count(*) as num from watch where target_id=? and watcher_id in (select target_id from watch where watcher_id=?)";
	public static final String addWatchSql = "insert into watch values (?,?)";
	public static final String deleteWatchSql = "delete from watch where watcher_id=? and target_id=?";
	public static final String checkWatchedSql = "select watcher_id from watch where watcher_id=? and target_id=?";
	
	
	/**
	 * 数据处理
	 */
	
	public String getWatch(int userID,int offset, int length, String type){
		try{
			if(length==0) return Msg.ERROR_PER_PAGE_NUM;
			List<User> userList = null;
			int pageCount = getPage(userID, length,type);
			
			if("watcher".equals(type)){//我关注的人
				userList = User.dao.find(getTargetsSql, userID, offset, length);
				for (User user : userList) {
					if(Watch.dao.find(checkWatchedSql, user.get("id"), userID).isEmpty()) 
						user.set("watched",false);
					else user.set("watched", true);
				}
			}else if("target".equals(type)){//关注我的人
				userList = User.dao.find(getWatchersSql, userID, offset, length);
				for (User user : userList) {
					if(Watch.dao.find(checkWatchedSql, userID, user.get("id")).isEmpty()) 
						user.set("watched",false);
					else user.set("watched", true);
				}
			}else if("each".equals(type)){//互相关注的人
				userList = User.dao.find(eachWatchedSql, userID, userID, offset, length);
				for (User user : userList) {user.set("watched", true);}
			}else{ return Msg.ERROR_WATCH_TYPE;}
			
			return User.dao.toJson(userList, pageCount, "watched");
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
	}
	
	//新增关注关系
	public boolean addWatch(int watcherID, int targetID){
		try{
			Record record = new Record().set("watcher_id", watcherID).set("target_id", targetID).set("create_time", new Date());
			Db.save("watch", record);
			return true;
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
	
	//删除关注关系
	public boolean deleteWatch(int watcherID, int targetID){
		try{
			Db.delete(deleteWatchSql, watcherID, targetID);
			return true;
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
	
	public String numtoJson(int userID){
		return "{\"error\" : 0, \"target_num\": " 
				+ dao.find(getTargetPageCountSql,userID).get(0).getInt("num")	//我关注的人
				+ ", \"watcher_num\": " 
				+ dao.find(getWatcherPageCountSql,userID).get(0).getInt("num")	//关注我的人
				+ " }";
	}
	
	
	/**
	 * 工具函数
	 */
	
	//获取总条数
	public int getCount(int userID, String type){
		int num = 0;
		if("each".equals(type)){
			num = Db.find(eachWatchedPageCountSql,userID,userID).get(0).getInt("num");
		}else if ("watcher".equals(type)){
			num = Db.find(getTargetPageCountSql,userID).get(0).getInt("num");
		}else if("target".equals(type)){
			num = Db.find(getWatcherPageCountSql,userID).get(0).getInt("num");
		}
		return num;
	}
	
	
	//获取总页数
	public int getPage(int userID, int length, String type){
		int num = getCount(userID, type);
		if(num==0) return 0;
		return num%length==0?num/length:(num/length)+1;
	}
		
	/**
	 * 数据检测
	 */
	
	//偏移量超出检测
	public boolean checkOffsetOut(int offset, String type, int userID){
		if(offset >= getCount(userID, type)) return false; else return true;
	}
	
	//数据存在检测
	public boolean checkExisted(int watcherID, int targetID){
		if(Db.find(checkExistedSql, watcherID, targetID).get(0).getInt("num")==0)
			return false;
		else return true;
	}
}
