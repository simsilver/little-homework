package com.simsilver.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

public class ID18 {
    static int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};    //十七位数字本体码权重
    static char[] validate = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};    //mod11,对应校验码字符值
    static Dictionary mDict = null;
    static Random mRand = null;

    static {
        mRand = new Random(Calendar.getInstance().getTimeInMillis());
        updateList();
    }

    public static char getValidateCode(String id17) {
        int sum = 0;
        int mode = 0;
        for (int i = 0; i < id17.length(); i++) {
            sum = sum + Integer.parseInt(String.valueOf(id17.charAt(i))) * weight[i];
        }
        mode = sum % 11;
        return validate[mode];
    }

    public static String checkValid(String id18) {
        if (id18 == null || id18.length() != 18) {
            return null;
        }
        String add = checkAreaValid(id18.substring(0,6));
        if(add == null){
            return null;
        }
        String sub17Id = id18.substring(0, 17);
        char checkSum = ID18.getValidateCode(sub17Id);
        if (checkSum == id18.charAt(17)) {
            return id18;
        } else {
            return sub17Id + checkSum;
        }
    }

    public static String genAreaCode() {
        int size = mDict.size();
        Map.Entry<String, String> entry = mDict.getEntry(mRand.nextInt(size));
        return entry.getKey();
    }

    public static String checkAreaValid(String area) {
        if (area == null || area.length() < 6) {
            return null;
        }
        String areaPart = area.substring(0, 6);
        String[] areaAddress = mDict.getValue(areaPart);
        if(areaAddress == null) {
            return null;
        }
        return areaAddress[0];
    }

    public static void updateList() {
        InputStream is = Utils.getDataFileStream("AreaCode");
        if (mDict == null) {
            mDict = new Dictionary();
        } else {
            mDict.reset();
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(" ");
                mDict.insert(data[0], data[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateValidID() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -38 + mRand.nextInt(20));
        cal.roll(Calendar.MONTH, mRand.nextInt(12));
        cal.roll(Calendar.DATE, mRand.nextInt(30));
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String birthday = format.format(cal.getTime());
        String order = String.format("%03d", mRand.nextInt(999));
        String id17 = genAreaCode() + birthday + order;
        char checkSum = getValidateCode(id17);
        return id17 + checkSum;
    }

    public static void main(String[] args) {
        switch (args.length) {
            case 1:
                String idNumber = args[0];
                char checkSum;
                switch (idNumber.length()) {
                    case 17:
                        checkSum = ID18.getValidateCode(idNumber);
                        Text.show("校验码： " + checkSum);
                        break;
                    case 18:
                        String validID = ID18.checkValid(idNumber);
                        if (validID.equals(idNumber)) {
                            Text.show("校验正确：" + idNumber);
                        } else {
                            Text.show("校验错误，应该为：" + validID);
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                String generatedID = ID18.generateValidID();
                Text.show("随机生成身份证号： " + generatedID);
                break;
        }
    }
}
