package com.bdqn.biz;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SentSSM {

    public boolean setPhone(String phone,String code)
    {
        HashMap<String, Object> result = null;

        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();

        restAPI.init("app.cloopen.com", "8883");

        restAPI.setAccount("8aaf070869dc0b88016a0fc9c78716f4", "9ae2f6c8120b4fa5b4d7f23c110f744a");

        restAPI.setAppId("8aaf070869dc0b88016a0fc9c7df16fa");

        result = restAPI.sendTemplateSMS(phone,"1" ,new String[]{code,"1"});

        System.out.println("SDKTestGetSubAccounts result=" + result);
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
         /*   HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }*/
            return  true;
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
        return  false;

    }

}
