package redis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * 
 * @author ahui
 * 这个文件暂时不要管
 */
public class RedisWriteIn {
	 public static void main(String args[]) throws IOException {

	        File file=new File("D:\\test.json");
	        Long filelength = file.length();
	        byte[] filecontent = new byte[filelength.intValue()];
	        try {
	            FileInputStream in = new FileInputStream(file);
	            in.read(filecontent);
	            in.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        String content= new String(filecontent,"UTF-8");
	        content = content.replaceAll("\\s*", "");
	        //System.out.println(content);
	        JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(content);
				JSONArray test = jsonObject.getJSONArray("test");
				for (int i = 0; i < test.length(); i++) {
					System.out.println(test.getJSONObject(i).getString("name"));
			        System.out.println(test.getJSONObject(i).getInt("age"));
			        System.out.println(test.getJSONObject(i).getString("sex"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		 
		 	/*Jedis jedis = new Jedis("123.206.82.204");
	        System.out.println("连接成功");
	        //查看服务是否运行
	        System.out.println("服务正在运行: "+jedis.ping());*/
	    }
}
