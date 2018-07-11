package com.ums.wifiprobe.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author chenjl
 * @author zhangsp
 */
public class ByteUtils {
    private ByteUtils() {
    }

    /** 灏嗘鏁存暟杞崲鎴愪袱涓瓧鑺?*/
    public static final byte[] toTwoByteArray(int i) {
        byte[] array = new byte[2];
        array[0] = (byte) (i >> 8);
        array[1] = (byte) i;
        return array;
    }

    /** 灏嗘鏁存暟杞崲鎴愬洓涓瓧鑺?*/
    public static final byte[] toFourByteArray(int i) {
        byte[] array = new byte[4];
        array[0] = (byte) (i >> 24 & 0x7F); // 鏈?珮浣嶆槸绗﹀彿浣?
        array[1] = (byte) (i >> 16);
        array[2] = (byte) (i >> 8);
        array[3] = (byte) i;
        return array;
    }

    /** 灏嗗瓧鑺傛暟缁勮浆鎹㈡垚涓?釜姝ｆ暣鏁?*/
    public static final int toInt(byte[] array) {
        return toInt(array, 0, array.length);
    }
    public static final int byteToInt(byte b) {  
	    //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值  
	    return b & 0xFF;  
	}
    /** 灏嗘寚瀹氱殑瀛楄妭杞崲鎴愪竴涓鏁存暟 */
    public static final int toInt(byte[] array, int offset, int length) {
        int value = 0;
        for (int i = 0; i < length; i++) {
            value |= getUnsignedInt(array[offset + i]) << (8 * (length - 1 - i));
        }
        return value;
    }

    /** 灏嗕袱涓瓧鑺傝浆鎹㈡垚涓?釜姝ｆ暣鏁?*/
    public static final int toInt(byte high, byte low) {
        return (getUnsignedInt(high) << 8) | getUnsignedInt(low);
    }

    /** TwoByteArray涓瓨鍏ユ椂鏄綋浣滄棤绗﹀彿瀛樺叆锛屽彇鍑烘椂榛樿鏄綋鎴愭湁绗﹀彿鍙栵紝浣跨敤璇ユ柟娉曞綋浣滄棤绗﹀彿鍙?*/
    public static final int getUnsignedInt(byte b) {
        return b & 0xFF; // &xFF娓呯┖绗﹀彿浣嶏紝閬垮厤b琚綋鎴愯礋鏁?
    }

    /** 璁＄畻鏁版嵁鐨凩RC妫?獙鍊?*/
    public static final byte calculateLrc(byte[] data) {
        return calculateLrc(data, 0, data.length);
    }

    /** 璁＄畻浠巓ffset寮?锛屽ぇ灏忎负length鐨勬暟鎹殑LRC妫?獙鍊?*/
    public static final byte calculateLrc(byte[] data, int offset, int length) {
        byte lrc = 0;
        for (int i = offset, l = offset + length; i < l; i++) {
            lrc = (byte) ((lrc ^ data[i]) & 0xFF);
        }
        return lrc;
    }

    /** 杩斿洖瀛楃涓茬殑ASCII瀛楄妭鏁扮粍 */
    public static final byte[] getBytes(String s) {
        try {
        	if (null == s || s.length() <= 0) {
        		 return new byte[0];
			}
            return s.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            // 姝ｅ父涓嶄細鍑虹幇寮傚父
            e.printStackTrace();
            return new byte[0];
        }
    }

    /** 鍏堣緭鍑哄瓧鑺傛暟缁勭殑ASCII瀛楃锛屽啀杈撳嚭鏁村瀷鍊?*/
    public static final String toString(byte[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(array.length * 6);
        for (int i = 0; i < array.length; i++) {
            sb.append((char) array[i]);
        }
        sb.append('[');
        sb.append(toHexString(array[0]));
        for (int i = 1; i < array.length; i++) {
            sb.append(", ");
            sb.append(toHexString(array[i]));
        }
        sb.append(']');
        return sb.toString();
    }

    public static final String toString(List<Byte> list) {
        if (list == null) {
            return "null";
        }
        if (list.size() == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(list.size() * 6);
        sb.append('[');
        Iterator<Byte> it = list.iterator();
        sb.append(toHexString(it.next()));
        while (it.hasNext()) {
            sb.append(", ");
            sb.append(toHexString(it.next()));
        }
        sb.append(']');
        return sb.toString();
    }

    /** 鍗佸叚杩涘埗杈撳嚭byte锛屾瘡涓猙yte涓や釜瀛楃锛屼笉瓒虫椂鍓嶈ˉ0 */
    public static final String toHexString(byte b) {
        String s = Integer.toHexString(b & 0xFF).toUpperCase();
        if (s.length() == 1) {
            s = "0" + s; // 琛ラ綈鎴愪袱涓瓧绗?
        }
        return s;
    }

    public static void copyArrayToList(byte[] src, int srcPos, List<Byte> dst, int dstPos,
            int length) {
        if (src == null) {
            throw new IllegalArgumentException("source array is null");
        }
        if (dst == null) {
            throw new IllegalArgumentException("destination list is null");
        }
        for (int i = 0; i < length; i++) {
            dst.add(dstPos + i, src[srcPos + i]);
        }
    }

    /** 灏嗗崄鍏繘鍒跺瓧绗︿覆杞崲鎴愬瓧鑺傛暟缁?*/
    public static byte[] toByteArray(String hex) {
        if (hex == null)
            return null;
        if (hex.length() % 2 != 0) {
            return null;
        }
        int length = hex.length() / 2;
        byte[] data = new byte[length];
        for (int i = 0, index = 0; i < length; i++) {
            index = i * 2;
            String oneByte = hex.substring(index, index + 2);
            data[i] = (byte) (Integer.valueOf(oneByte, 16) & 0xFF);
        }
        return data;
    }

    /** 灏嗗瓧鑺傛暟缁勮浆鎹㈡垚鍗佸叚杩涘埗瀛楃涓?*/
    public static String toHexString(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        return toHexString(data, 0, data.length);
    }

    /** 灏嗗瓧鑺傛暟缁勪腑鐨勬寚瀹氬瓧鑺傝浆鎹㈡垚鍗佸叚杩涘埗瀛楃涓?*/
    public static String toHexString(byte[] data, int offset, int length) {
        if (data == null || data.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(toHexString(data[offset + i]));
        }
        return sb.toString();
    }

    /** 灏唅nt鍨嬭浆鍖栦负鍏綅BCD鐮?**/
    public static byte[] toBCDAmountBytes(int data) {
        byte[] bcd = {
                0, 0, 0, 0, 0, 0
        };
        byte[] bcdDou = {
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        if (data <= 0) {
            return bcd;
        }

        int i = bcdDou.length - 1;

        while (data != 0) {
            bcdDou[i] = (byte) (data % 10);
            data /= 10;
            i--;
        }

        for (i = bcd.length - 1; i >= 0; i--) {
            bcd[i] = (byte) (((bcdDou[i * 2 + 1] & 0x0f)) | ((bcdDou[i * 2] << 4) & 0xf0));
        }

        return bcd;
    }

    /** 灏哠tring鐨勬暟瀛楀瓧绗︿覆杞寲涓築CD鐮?**/
    public static byte[] toBCDDataBytes(String data) {
        if (data == null || data.isEmpty())
            return null;
        if (data.length() % 2 != 0) {
            return null;
        }

        int bcdLength = data.length() / 2;
        byte[] bcd = new byte[bcdLength];
        for (int i = 0, index = 0; i < bcdLength; i++) {
            index = i * 2;
            String oneByte = data.substring(index, index + 2);
            if (oneByte.charAt(0) < '0' || oneByte.charAt(0) > '9' || oneByte.charAt(1) < '0'
                    || oneByte.charAt(1) > '9') {
                return null;
            }
            
            bcd[i] = (byte) (((oneByte.charAt(0) - '0') << 4) | (oneByte.charAt(1) - '0'));
        }
        return bcd;
    }

    /** 灏咮CD鐮佽浆涓篠tring鐨勬暟瀛楀瓧绗︿覆 **/
    public static String toBCDString(byte[] data) {
        int length = data.length;
        StringBuilder bcd = new StringBuilder();

        for (int i = 0; i < length; i++) {
            byte dataIndex = data[i];
//            if ((dataIndex >> 4) >= 10 || (dataIndex & 0x0f) >= 10) {
//                return null;
//            }
            if (((dataIndex & 0xf0) >> 4) >= 10) {
                return null;
            }
            
            if ((dataIndex & 0x0f) >= 10) {
                if (i == length - 1) {
                    bcd.append((dataIndex & 0xf0) >> 4);
                    break;
                } else {
                    return null;
                }
            }

            bcd.append((dataIndex & 0xf0) >> 4).append(dataIndex & 0x0f);
        }

        return bcd.toString();
    }

    /**
     * 鍗佸叚杩涘埗瀛楃杞负byte list
     * 
     * @param dataStr
     * @return
     */
    public static List<Byte> hexString2ByteList(String dataStr) {
        byte[] dataArray = hexString2ByteArray(dataStr);
        List<Byte> result = new ArrayList<Byte>();
        for (int i = 0; i < dataArray.length; i++) {
            result.add(dataArray[i]);
        }
        return result;
    }

    /**
     * 鍗佸叚杩涘埗 杞崲 byte[]
     * 
     * @param hexStr
     * @return
     */
    public static byte[] hexString2ByteArray(String hexStr) {
        if (hexStr == null)
            return null;
        if (hexStr.length() % 2 != 0) {
            return null;
        }
        byte[] data = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            char hc = hexStr.charAt(2 * i);
            char lc = hexStr.charAt(2 * i + 1);
            byte hb = hexChar2Byte(hc);
            byte lb = hexChar2Byte(lc);
            if (hb < 0 || lb < 0) {
                return null;
            }
            int n = hb << 4;
            data[i] = (byte) (n + lb);
        }
        return data;
    }

    public static byte hexChar2Byte(char c) {
        if (c >= '0' && c <= '9')
            return (byte) (c - '0');
        if (c >= 'a' && c <= 'f')
            return (byte) (c - 'a' + 10);
        if (c >= 'A' && c <= 'F')
            return (byte) (c - 'A' + 10);
        return -1;
    }

    /**
     * byte[] 杞?16杩涘埗瀛楃涓?
     * 
     * @param arr
     * @return
     */
    // public static String byteArray2HexString(byte[] arr) {
    // StringBuilder sbd = new StringBuilder(100000);
    // for (byte b : arr) {
    // String tmp = Integer.toHexString(0xFF & b);
    // if (tmp.length() < 2)
    // tmp = "0" + tmp;
    // sbd.append(tmp);
    // }
    // return sbd.toString();
    // }

    public static String byteArray2HexString(byte[] arr) {
    	if (null == arr || arr.length == 0) {
    		Log.d("", "byteArray2HexString arr is null");
			return "";
		}
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(String.format("%02x", arr[i]));
        }
        return sb.toString();
    }

    public static String byteArray2HexString(List<Byte> arrList) {
        byte[] arr = new byte[arrList.size()];

        for (int i = 0; i < arrList.size(); ++i) {
            arr[i] = arrList.get(i);
        }

        return byteArray2HexString(arr);
    }

    public static String byteArray2HexStringWithSpace(byte[] arr) {
        StringBuilder sbd = new StringBuilder();
        for (byte b : arr) {
            String tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() < 2)
                tmp = "0" + tmp;
            sbd.append(tmp);
            sbd.append(" ");
        }
        return sbd.toString();
    }

    /**
     * @param val
     * @param bitPos The leftmost bit is 8 (the most significant bit)
     * @return
     */
    public static boolean isBitSet(byte val, int bitPos) {
        if (bitPos < 1 || bitPos > 8) {
            throw new IllegalArgumentException(
                    "parameter 'bitPos' must be between 1 and 8. bitPos=" + bitPos);
        }
        if ((val >> (bitPos - 1) & 0x1) == 1) {
            return true;
        }
        return false;
    }

    /*
     * 鍒ゆ柇byte鏁版嵁鏄惁涓築CD鐮?鏄垯杩斿洖true锛屽惁杩斿洖false
     */
    public static boolean isBCD(byte[] data) {
        boolean flag = true;

        if (data == null) {
            return false;
        }

        for (int i = 0; i < data.length; i++) {
            byte dataIndex = data[i];

            byte dataIndexH = (byte) (dataIndex >> 4);
            if (dataIndexH >= 10) {
                flag = false;
                break;
            }

            byte dataIndexL = (byte) (dataIndex & 0x0f);
            if (dataIndexL >= 10) {
                flag = false;
                break;
            }
        }

        return flag;
    }

    /*
     * 鍒ゆ柇String瀛楃涓叉槸鍚︽槸鍗佸叚杩涘埗鏁版嵁 鏄垯杩斿洖true锛屽惁杩斿洖false
     */
    public static boolean isHexString(String data) {
        boolean flag = true;

        Log.d("ByteUtils", "data : " + data);
        if (data == null || data.length() % 2 != 0) {
            return false;
        }

        for (int i = 0; i < data.length(); i++) {
            char dataIndex = data.charAt(i);

            if (!((dataIndex >= '0' && dataIndex <= '9') || (dataIndex >= 'a' && dataIndex <= 'f') || (dataIndex >= 'A' && dataIndex <= 'F'))) {
                flag = false;
                break;
            }
        }

        Log.d("ByteUtils", "flag is " + flag);

        return flag;
    }

    /*
     * 鍒ゆ柇String瀛楃涓叉槸鍚︽槸鍗佽繘鍒舵暟鎹?鏄垯杩斿洖true锛屽惁杩斿洖false
     */
    public static boolean isNumString(String data) {
        boolean flag = true;

        if (data == null) {
            return false;
        }

        for (int i = 0; i < data.length(); i++) {
            char dataIndex = data.charAt(i);

            if (!(dataIndex >= '0' && dataIndex <= '9')) {
                flag = false;
                break;
            }
        }

        return flag;
    }
 // char转byte
    public static byte[] getBytes (char[] chars) {
       Charset cs = Charset.forName ("UTF-8");
       CharBuffer cb = CharBuffer.allocate (chars.length);
       cb.put (chars);
       cb.flip ();
       ByteBuffer bb = cs.encode (cb);
      
       return bb.array();

     }

    // byte转char
    public static char[] getChars (byte[] bytes) {
          Charset cs = Charset.forName ("UTF-8");
          ByteBuffer bb = ByteBuffer.allocate (bytes.length);
          bb.put (bytes);
          bb.flip ();
          CharBuffer cb = cs.decode (bb);
      
       return cb.array();
    }
    /**
	 * 获得子数组
	 * @param data		数据
	 * @param offset	偏移位置。0~data.length
	 * @param len		长度。为负数表示正常范围的最大长度
	 * @return	子数组
	 */
	public static byte[] subBytes(byte[] data, int offset, int len){
		if(offset < 0 || data.length <= offset) {
			return null;
		}
		
		if(len < 0 || data.length < offset + len) {
			len = data.length - offset;
		}
		
		byte[] ret = new byte[len];
		
		System.arraycopy(data, offset, ret, 0, len);
		return ret;
	}
	   /**
     * 将多个数据并起来
     * @param data  数据数组，可传任意个
     * @return      合并的数据
     */
    public static byte[] merage(byte[]... data) {
        byte[] newData;
        int len = 0;
        for(int i=0; i<data.length; i++){
            if(data[i] == null) {
                throw new IllegalArgumentException("");
            }
            len += data[i].length;
        }
        
        newData = new byte[len];
        len = 0;
        for(byte[] d : data) {
            System.arraycopy(d, 0, newData, len, d.length);
            len += d.length;
        }
        return newData;
    }
    /**
	  * 将ASC码的字符串转成BCD码数组
	  * @param asc  asc数据
	  * @return  BCD码数组
	*/
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0) {
         asc = "0" + asc;
         len = asc.length();
        }

        byte abt[] = new byte[len];
        if (len >= 2) {
         len = len / 2;
        }

        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;

        for (int p = 0; p < asc.length()/2; p++) {
         if ( (abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
          j = abt[2 * p] - '0';
         } else if ( (abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
          j = abt[2 * p] - 'a' + 0x0a;
         } else {
          j = abt[2 * p] - 'A' + 0x0a;
         }

         if ( (abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
          k = abt[2 * p + 1] - '0';
         } else if ( (abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
          k = abt[2 * p + 1] - 'a' + 0x0a;
         }else {
          k = abt[2 * p + 1] - 'A' + 0x0a;
         }
         int a = (j << 4) + k;
         byte b = (byte) a;
         bbt[p] = b;
        }
        return bbt;
    }
    // byteתchar
    public static byte[] isEmpty (byte[] data) {
    	if (null == data) {
    		data = (byte[])null;
		}
       return data;
    }
}
