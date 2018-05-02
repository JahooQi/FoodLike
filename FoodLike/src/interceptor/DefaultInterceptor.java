package interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class DefaultInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		//解决跨域问题
		inv.getController().getResponse().setHeader("Access-Control-Allow-Origin", "*");
		//设置编码
		inv.getController().getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
		inv.invoke();
	}

}
