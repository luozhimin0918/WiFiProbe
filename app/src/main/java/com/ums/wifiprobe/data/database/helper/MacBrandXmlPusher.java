package com.ums.wifiprobe.data.database.helper;

import android.util.Log;

import com.ums.wifiprobe.R;
import com.ums.wifiprobe.service.greendao.MacBrandInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhy on 2017/10/26.
 */

public class MacBrandXmlPusher {


    public static List<MacBrandInfo> getParams(InputStream input) throws Throwable {
        List<MacBrandInfo> macBrandInfoList = null;
        String macBrandName = "";
        //xml工厂

        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = parserFactory.newPullParser();
        //解析文件输入流
        parser.setInput(input, "utf-8");
        //产生第一个事件
        int evenType = parser.getEventType();
        //只要不是文档结束事件，就一直循环
        while (evenType != XmlPullParser.END_DOCUMENT) {
            switch (evenType) {
                //开始触发文档事件
                case XmlPullParser.START_DOCUMENT:
                    macBrandInfoList = new ArrayList<MacBrandInfo>();
                    break;
                //开始触发元素事件
                case XmlPullParser.START_TAG:
                    //获取解析器当前指向的元素名称
                    String data = parser.getName();
                    if ("COMPANYGROUP".equals(data)) {
                        //通过解析器获取元素值，并设置和testParser相对的值
                    }
                    if (macBrandInfoList != null) {
                        if ("COMPANY".equals(data)) {
                            macBrandName = parser.getAttributeValue(0);
                        } else if ("ITEM".equals(data)) {
                            MacBrandInfo macBrandInfo = new MacBrandInfo();
                            macBrandInfo.setBrandName(macBrandName);
                            macBrandInfo.setBrandMac(parser.getAttributeValue(0));
                            macBrandInfoList.add(macBrandInfo);
                        }
                    }
                    break;
                //触发结束事件
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("COMPANYGROUP")) {

                    }
                    break;
                default:
                    break;
            }
            evenType = parser.next();
        }
        return macBrandInfoList;
    }
}
