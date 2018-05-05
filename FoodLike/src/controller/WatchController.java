package controller;

import model.Watch;
import validate.WatchValidate;
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
		String valid = null;
		
		switch(getRequest().getMethod()){
		case "GET" :
			offset = getPara("offset");
			length = getPara("length");
			type = getPara("type");
			
			valid = WatchValidate.get(id, offset, length, type);
			if(valid!=null){renderText(valid);return;}
			
			String result = Watch.dao.getWatch(Integer.parseInt(id),Integer.parseInt(offset),Integer.parseInt(length),type);
			if(result==null) renderText(Msg.ERROR_UNKNOWN); else renderText(result); break;
			
		case "POST" :
			target_id = getPara("target_id");
			
			valid = WatchValidate.post(id, target_id);
			if(valid!=null){renderText(valid);return;}
			
			if(Watch.dao.addWatch(Integer.parseInt(id), Integer.parseInt(target_id))){
				renderText(Msg.SUCCESS_EXECUTE);
			}else{renderText(Msg.ERROR_UNKNOWN);}break;
			
		case "DELETE" :
			target_id = getPara("target_id");
			
			valid = WatchValidate.delete(id, target_id);
			if(valid!=null){renderText(valid);return;}
			
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
		
		String valid = WatchValidate.getNum(id);
		if(valid!=null){renderText(valid);return;}
		
		String result = Watch.dao.numtoJson(Integer.parseInt(id));
		if(result==null) renderText(Msg.ERROR_UNKNOWN); else renderText(result);
	}
}
