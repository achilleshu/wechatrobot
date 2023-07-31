package com.doodl6.wechatrobot.service;

import com.alibaba.fastjson.JSONObject;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatGlmService {

    private static final String OPENAI_API_KEY = "";

    private static final String OPENAI_BASE_DOMAIN = "http://192.168.111.34:7860/chat";

    private static final String OPENAI_PROXY = "OPENAI_PROXY";

    private static final String MODEL = "chatglm2-6b";

    public ChatGlmService() {


    }


    public static String doPost(String url, JSONObject json){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);

            // 首先Header部分需要设定字符集为：uft-8
            post.addHeader("Content-Type", "application/json;charset=utf-8");
            // 此处也需要设定
            post.setHeader("Accept", "*/*");
            post.setHeader("Accept-Encoding", "gzip, deflate, br");
//            post.setHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
            post.setHeader("Connection", "keep-alive");
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
            post.setEntity(new StringEntity(json.toString(), Charset.forName("UTF-8"))); //设置请求参数
            HttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode){
                //返回String
                String res = EntityUtils.toString(response.getEntity());
                return res;
            }else{
                return "调用POST请求失败";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "调用POST请求失败";
        }
    }
    public BaseMessage getResponse(String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("question", content);
        map.put("history", new ArrayList<>());
        JSONObject json = new JSONObject(map);

        //以post形式请求接口
        String result = doPost("http://192.168.111.34:7860/chat", json);

        JSONObject jsonObject = JSONObject.parseObject(result);
        String response = StringUtils.isNotBlank(jsonObject.getString("response")) ? jsonObject.getString("response") : "";
        return new TextMessage(response);
    }
}
