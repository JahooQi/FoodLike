package constant;

public class Msg {
	public static final String SUCCESS_EXECUTE = "{\"error\": 0}";
	public static final String ERROR_PARA_NUM = "{\"error\": 1, \"msg\":\"参数个数不对应\"}";
	public static final String ERROR_PARA_TYPE = "{\"error\": 2, \"msg\":\"参数类型不对应\"}";
	public static final String ERROR_OFFSET_OUT = "{\"error\": 3, \"msg\":\"偏移量超过数据总条数\"}";
	public static final String ERROR_ID_SAME = "{\"error\": 4, \"msg\":\"两个USER_ID相同\"}";
	public static final String ERROR_INSERT_EXISTED = "{\"error\": 5, \"msg\":\"要添加的数据已存在，无法重复添加\"}";
	public static final String ERROR_DELETE_UNEXISTED = "{\"error\": 6, \"msg\":\"要删除的数据不存在\"}";
	public static final String ERROR_PER_PAGE_NUM = "{\"error\": 7, \"msg\":\"每页显示数据不能为零\"}";
	public static final String ERROR_USER_ID_UNEXISTED = "{\"error\": 8, \"msg\":\"参数userID未出现在users表中\"}";
	public static final String ERROR_WATCH_TYPE = "{\"error\": 9, \"msg\":\"操作watch表时参数type不是指定字符串\"}";
	public static final String ERROR_UNKNOWN = "{\"error\": 999, \"msg\":\"后台发生未知错误，可能是数据处理或操作数据库出错，异常已抛出，具体情况请查询日志，/tomcat/log/catalina-当前日期.log\"}";
}
