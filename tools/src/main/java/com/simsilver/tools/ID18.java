package com.simsilver.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class ID18 {
    static int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};    //十七位数字本体码权重
    static char[] validate = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};    //mod11,对应校验码字符值

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
        if(id18 == null || id18.length() != 18) {
            return null;
        }
        String sub17Id = id18.substring(0, 17);
        char checkSum = ID18.getValidateCode(sub17Id);
        if(checkSum == id18.charAt(17)) {
            return id18;
        } else {
            return sub17Id + checkSum;
        }
    }

    public static String generateValidID() {
        String province = "410";
        String city = "184";
        Calendar cal = Calendar.getInstance();
        Random rand = new Random(cal.getTimeInMillis());
        cal.add(Calendar.YEAR, -38 + rand.nextInt(20));
        cal.roll(Calendar.MONTH, rand.nextInt(12));
        cal.roll(Calendar.DATE, rand.nextInt(30));
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String birthday = format.format(cal.getTime());
        String order = String.format("%03d",rand.nextInt(999));
        String id17 = province + city + birthday + order;
        char checkSum = getValidateCode(id17);
        return id17 + checkSum;
    }

    public static void main(String[] args) {
        switch (args.length) {
            case 1:
                String idNumber = args[0];
                char checkSum;
                switch (idNumber.length()){
                    case 17:
                        checkSum = ID18.getValidateCode(idNumber);
                        Text.show("校验码： " + checkSum);
                        break;
                    case 18:
                        String validID = ID18.checkValid(idNumber);
                        if(validID.equals(idNumber)) {
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
