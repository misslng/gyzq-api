package org.example;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.sm4.SM4Util;

import java.util.Formatter;

public class Main {
    public static void main(String[] args) {
        try {
            SM4Util sm4Util = new SM4Util();
            String loginString = sm4Util.encryptDataCBC(Core.login("account", "password", "phone"));
            String loginUrl = "https://djms.gyzq.com.cn:51900/api/trade/ptjy/ptyw/login";
            HttpPost httpPost = new HttpUtil().Default(loginUrl).setPostText(loginString).getHttpPost();

            HttpResponse response = HttpClients.createDefault().execute(httpPost);
            String retText = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(sm4Util.decryptDataCBC(retText));
        } catch (Exception ignored) {

        }
    }
}

class HttpUtil {
    HttpPost httpPost;

    public HttpUtil Default(String uri) {
        this.httpPost = new HttpPost(uri);
        this.httpPost.setHeader("kds_is_encrypt", "3");
        this.httpPost.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 7.1.2; V1938CT Build/N2G47O; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/86.0.4240.198 Mobile Safari/537.36 GYDJ-7.0.6");

        return this;
    }

    public HttpUtil setPostText(String text) {
        this.httpPost.setEntity(new StringEntity(text, ContentType.TEXT_PLAIN));
        return this;
    }

    public HttpPost getHttpPost() {
        return httpPost;
    }
}

class Core {
    public static String login(String acc, String pwd, String phone) {
        Formatter formatter = new Formatter();
        return formatter.format("{\"hbdm\":\"\",\"khbzlx\":\"Z\",\"khbz\":\"%s\",\"jymm\":\"%s\",\"sessionid\":\"0\",\"yybdm\":\"129\",\"token\":\"0123456789\",\"lhxx\":\"%s,352746023717988,7.0.6.20211117.66099,460009771036561,02:00:00:00:00:00,406a21f051213e76,172.16.1.15\",\"lhxxn\":\"MA;IIP=120.41.156.103;IPORT=12002;LIP=172.16.1.15;MAC=020000000000;IMEI=352746023717988;RMPN=%s;UMPN=ERROR-系统API限制;ICCID=89860017106171693034;OSV=ANDROID 7.1.2;IMSI=460009771036561;@JHYT;V7.0.6\",\"lhxxnVer\":\"1\",\"rzfs\":\"1\",\"rzxx\":\"\",\"rznr\":\"\"}", acc, pwd, phone, phone).toString();
    }
}