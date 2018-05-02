package app;

import interceptor.DefaultInterceptor;
import model.Friend;
import model.User;
import model.Watch;

import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.template.Engine;

import controller.FriendController;
import controller.WatchController;

public class Config extends JFinalConfig {

	public void configConstant(Constants me){
		me.setDevMode(true);
	}
	
	public void configRoute(Routes me) {
		me.add("/friends",FriendController.class);
		me.add("/watch",WatchController.class);
	}
	
	public void configPlugin(Plugins me) {
		loadPropertyFile("properties/jdbc.properties");
		System.out.println(getProperty("url")+ "  " + getProperty("username") + "  " + getProperty("password"));
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("url"),
				getProperty("username"), getProperty("password"));
		me.add(c3p0Plugin);
		
		ActiveRecordPlugin activeRecord=new ActiveRecordPlugin(c3p0Plugin);
        activeRecord.addMapping("users", "id", User.class);
        activeRecord.addMapping("friends", "one_user_id,another_user_id", Friend.class);
        activeRecord.addMapping("watch", "watcher_id,target_id", Watch.class);
        me.add(activeRecord);
	}

	public void configInterceptor(Interceptors me) {
		me.add(new DefaultInterceptor());
	}
	
	public void configHandler(Handlers me) {}
	
	@Override
	public void configEngine(Engine me) {
		
	}
}