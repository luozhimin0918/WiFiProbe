package com.ums.shdep.reward;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.ums.tms.app.interact.AppInteractHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenzhy on 2018/4/13.
 */

public class RewardPay {
    private static final String TAG = "RewardPay";//调用者权限值

    private static final String mAppKey = "ac6d287a30ef498c89ae2bb7fd27889d";//调用者权限值
    private static final String smAppKey="076e2adac935401a912a517fe0cc6a7f";
    private static final String ssmAppKey="f3a737e874bf40bf9a7b116403362537";
    private static String mGoodsName = "客流分析打赏";
    private static String mAmount = "10";//支付总金额，单位：分

    private int retryTimes;


    //--服务费（分润，第三方应用可用）
    public void pay(Context context, String goodName, String amount,OnPayResultListener listener) {
        this.listener = listener;
        mGoodsName = goodName;
        mAmount = amount;
        retryTimes=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String curtime = sdf.format(date);
        JSONObject paramData = new JSONObject();
        try {
            paramData.put("dataType", "203");
            paramData.put("appKey", mAppKey);                        //调用者权限值
            paramData.put("packName", context.getPackageName());    //调用者包名
            paramData.put("goodsName", mGoodsName);                    //商品名称
            paramData.put("amount", mAmount);                        //支付总金额，单位：分
            paramData.put("curtime", curtime);                    //时间戳
        } catch (Exception e) {
            // TODO: handle exception
        }
        pay(context, getQrParamData("213",mAmount), paramData);
    }


    //--支付结果查询（均可用）
    private void queryPayResult(final Context context) {
        JSONObject paramData = new JSONObject();
        try {
            paramData.put("dataType", "200");
            paramData.put("packName", context.getPackageName());
            paramData.put("appKey", mAppKey);
            paramData.put("payDataType", mBillInfo.getPayDataType());
            paramData.put("billNo", mBillInfo.getBillNo());
            paramData.put("billDate", mBillInfo.getBillDate());
//			paramData.put("responseFast", "00");//是否立即返回, 00为是（默认），01为否（极限超时时间，直到账单/悦单系统有结果返回）
            //内购时必须组装以下参数
//			paramData.put("billQueryInfo", installBillQueryInfo(paramData.toString()));

            //以下为可选参数
            paramData.put("showView", "01");//是否显示界面,00为否（默认），01为是
            paramData.put("goodsName", mGoodsName);//商品名称, 当showView为01时必需
            paramData.put("amount", mAmount);//总金额,单位：分, 当showView为01时必需
        } catch (Exception e) {
            // TODO: handle exception
        }
        AppInteractHelper.queryPayResult(context, paramData.toString(), new AppInteractHelper.PayResultListener() {
            @Override
            public void onPayResultReturned(String result) {
                // TODO Auto-generated method stub
                Log.e(TAG, "-onPayResultReturned-result:" + result);
                onPayResult(context, result);
            }
        });
    }

    private void pay(final Context context, final JSONObject qrParamData, final JSONObject creditcardParamData) {

        AppInteractHelper.callPay(context, new AppInteractHelper.PayListener() {
            @Override
            public String getPayParamData(int payType) {
                // TODO Auto-generated method stub
                Log.e(TAG, "-getPayParamData-payType:" + payType);
                if (payType == AppInteractHelper.PayListener.PAY_TYPE_CREDITCARD) {
                    return creditcardParamData.toString();
                } else if (payType == AppInteractHelper.PayListener.PAY_TYPE_QR) {
                    return qrParamData.toString();
                }
                return null;
            }

            @Override
            public String onBillGenerated(int payType, String result) {
                // TODO Auto-generated method stub
                Log.e(TAG, "-onBillGenerated-payType:" + payType + ", result:" + result);
                resolveBillInfo(result);
                return null;
            }

            @Override
            public void onPayReturned(int payType, String result) {
                // TODO Auto-generated method stub
                Log.e(TAG, "-onPayReturned-payType:" + payType + ", result:" + result);
                onPayResult(context, result);
            }
        });
    }

    private void onPayResult(final Context context, String result) {
        try {
            JSONObject resultJson = new JSONObject(result);
            String resultCode = getJSONString(resultJson, "state");
            String resultMsg = getJSONString(resultJson, "msg");
            String payResult = getJSONString(resultJson, "payResult");//支付结果，"00"支付失败，"01"支付成功，"02"支付渠道尚未同步支付结果（或未支付）

            if (!"01".equals(resultCode)) { // 接口调用失败
                if(listener!=null){
                    listener.onPayResult(-1);
                }
                return;
            }
            if ("01".equals(payResult)) {//支付成功
                if(listener!=null){
                    listener.onPayResult(0);
                }
            } else if ("02".equals(payResult)) {//支付渠道尚未同步支付结果（或未支付）
                retryTimes++;
                if(retryTimes>=2){
                    retryTimes=0;
                    return;
                }
                new Handler(context.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        queryPayResult(context);
                    }
                }, 1000);
            } else {// unpaid
                //支付失败
                //重新去支付
                if(listener!=null){
                    listener.onPayResult(2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject getQrParamData(String qrDataType,String amount) {
        try {
            JSONObject qrParamData = new JSONObject();
            qrParamData.put("dataType", qrDataType);
            qrParamData.put("billInfo", installBillInfo(amount));
            return qrParamData;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject installBillInfo(String amount) {
        try {
            JSONObject billInfo = new JSONObject();
            billInfo.put("totalAmount", amount);             //支付总金额，单位：分
            JSONObject good = new JSONObject();
            good.put("quantity", "1");             //商品数量
            good.put("goodsId", "0001");             //商品ID
            good.put("price", amount);                //商品单价，单位：分
            good.put("goodsCategory", "0001");      //商品分类
            good.put("body", "商品说明");            //商品说明
            good.put("goodsName", mGoodsName);       //商品名称
            JSONArray goods = new JSONArray();
            goods.put(good);
            billInfo.put("goods", goods);
            billInfo.put("billDesc", "账单描述:客流分析打赏"); //账单描述
            return billInfo;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }


    private void resolveBillInfo(String result) {
        if (result != null) {
            try {
                JSONObject resultData = new JSONObject(result);
                mBillInfo.setPayDataType(getJSONString(resultData, "payDataType"));
                mBillInfo.setOrderNo(getJSONString(resultData, "orderNo"));
                mBillInfo.setBillNo(getJSONString(resultData, "billNo"));
                mBillInfo.setBillDate(getJSONString(resultData, "billDate"));
                mBillInfo.setRespon(getJSONString(resultData, "respon"));//内购时才有此数据
            } catch (Exception e) {
                Log.e(TAG, "-Exception:" + e.toString());
            }
        }
    }

    private static String getJSONString(JSONObject jObj, String key) throws JSONException {
        if (jObj != null && jObj.has(key)) {
            return jObj.getString(key);
        }
        return null;
    }

    private BillInfo mBillInfo = new BillInfo();

    private class BillInfo {
        private String payDataType = null;    //支付请求类型
        private String orderNo = null;        //订单号
        private String billNo = null;        //账单号
        private String billDate = null;        //账单日期
        private String respon = null;        //内购数据

        public String getPayDataType() {
            return payDataType;
        }

        public void setPayDataType(String payDataType) {
            this.payDataType = payDataType;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getBillNo() {
            return billNo;
        }

        public void setBillNo(String billNo) {
            this.billNo = billNo;
        }

        public String getBillDate() {
            return billDate;
        }

        public void setBillDate(String billDate) {
            this.billDate = billDate;
        }

        public String getRespon() {
            return respon;
        }

        public void setRespon(String respon) {
            this.respon = respon;
        }
    }

    public interface OnPayResultListener{
        void onPayResult(int resultCode);//-1 接口调用失败 0支付成功 1支付失败
    }

    private OnPayResultListener listener;
}
