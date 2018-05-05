package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class User extends Model<User>{
	public static final User dao = new User();
	
	//userID存在检测
	public boolean checkUserIDExisted(int userID){
		if(User.dao.findById(userID)!=null){return true;}else{return false;}
	}
	
	//将列表转成JSON数据
	public String toJson(List<User> list, int pageCount){
		return toJson(list, pageCount, null);
	}

	public String toJson(List<User> list, int pageCount, String type){
		StringBuffer jsonText = new StringBuffer();
		jsonText.append("{\"error\": 0, \"page_count\": ").append(pageCount).append(", \"data\": [");
		for (User user : list) {
			jsonText.append("{\"id\": ").append(user.getInt("id")).append(",");
			jsonText.append(" \"nickname\": \"").append(user.getStr("nickname")).append("\",");
			jsonText.append(" \"avatar\": \"").append(user.getStr("avatar")).append("\"");
			if(type!=null && "watched".equals(type)){
				jsonText.append(", \"watched\":").append(user.getBoolean("watched"));
			}
			jsonText.append("}");
			if(user!= list.get(list.size()-1)){
				jsonText.append(",");
			}
		}
		jsonText.append("]}");
		return jsonText.toString();
	}
}
