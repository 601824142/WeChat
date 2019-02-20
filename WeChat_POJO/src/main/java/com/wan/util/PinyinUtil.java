package com.wan.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @Author 万星明
 * @Date 2019/2/20
 */
public class PinyinUtil {


    /**
     * 将中文字符串转为拼音
     * @param string
     * @return
     */
    public  static  String stringToPinyin(String string){
        //对传进来的字符串做判断,如果传入的字符串为空,则返回为空
        if (string == null || string.trim().equals("")){
            return null;
        }

        //设置拼音格式
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        //设置声调
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        //新建字符包装类
        StringBuilder stringBuilder = new StringBuilder();
        //将传进来的字符串转为字符数组
        char[] chars = string.toCharArray();

        //对转换的字符数组依次遍历转为拼音
        for (char aChar : chars) {

            //循环依次将字符转换成拼音
            try {

                String[] strs = PinyinHelper.toHanyuPinyinStringArray(aChar, outputFormat);

                if(strs != null){
                    stringBuilder.append(strs[0]);
                } else {
                      //stringBuilder.append(aChar);
                }

            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }



}
