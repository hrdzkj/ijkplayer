package tv.danmaku.ijk.media.example.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiuYi on 2018/8/30.
 *

 *
 *  请求数据格式 ：{"method":"login","para":{"userName":"tongfu001"}}
 *  返回数据格式：{"code":"ok","eSdkResult":"","svResult":{}}
 */

public class MakeRequestMap {
    public  static Map<String,String> makeRequestMap(String methodName, JsonData para){

        Map<String,String> map = new HashMap<>();

        JsonData requestJson = JsonData.create("{}");
        requestJson.put(ConstHttp.JSON_METHOD_KEY,methodName);
        requestJson.put(ConstHttp.JSON_PARMA_KEY,para);
        map.put(ConstHttp.TRANSFER_KEY,requestJson.toString());
        return map;
    }

}
