package com.learnbind.ai.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 表册列表
 * 作用:用户浏览表册
 *
 * MeterBookList---use--->MeterBook
 * 
 * added by jch 2019/06/11
 */
public class MeterBookList {
    private List<MeterBookBean> meterBooks;  //表册列表
    
    public MeterBookList() {
    	meterBooks=new ArrayList<>();
    }

    public List<MeterBookBean> getMeterBooks() {
        return meterBooks;
    }

    public void setMeterBooks(List<MeterBookBean> meterBooks) {
        this.meterBooks = meterBooks;
    }

    //测试表册列表
    private static void testMeterBookList(){
        String testData="{       " +
                "    \"meterBooks\": [" +
                "        {" +
                "            \"meterBookId\": \"1\"," +
                "            \"name\":\"长久花园-10-1\"," +
                "            \"equipType\": \"JC\"" +
                "        }," +
                "        {" +
                "            \"meterBookId\": \"2\"," +
                "            \"name\":\"长久花园-10-2\"," +
                "            \"equipType\": \"JC\"" +
                "        }," +
                "        {" +
                "            \"meterBookId\": \"3\"," +
                "            \"name\":\"长久花园-10-3\"," +
                "            \"equipType\": \"JC\"" +
                "        }" +
                "    ]" +
                "}";
        MeterBookList meterBookList= FastJsonUtils.getObject(testData, MeterBookList.class);
        for(MeterBookBean meterBook:meterBookList.getMeterBooks()){
            System.out.println("表册名称:  "+meterBook.getName());
            System.out.println("表册ID:   "+meterBook.getMeterBookId());
            System.out.println("表册-设备类型:   "+meterBook.getEquipType());
            System.out.println("---------------------------------------");

        }

    }

    public static void main(String[] args){
        testMeterBookList();
    }


}
