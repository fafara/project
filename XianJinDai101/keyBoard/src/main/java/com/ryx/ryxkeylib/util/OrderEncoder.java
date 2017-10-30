package com.ryx.ryxkeylib.util;

import java.util.Random;

public class OrderEncoder {
	private static  int[] sequence = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
	public static int[] reOrder()
	{
		int[]  Pai=new int[10];
		//初始化 生成牌
        for (int i = 0; i <10; i++)
        {
        	Pai[i] = i;
        }
        
        int k,temp;
		Random rnd = new Random();
		///洗牌
        for (int num = 0; num < 100; num++)
        {
            for (int i = 0; i < 10; i++)
            {
                k = rnd.nextInt() % 10;
               // Log.i("k",Integer.toString(k));
                if(k>=0 && k<=9)
                {	                
	                temp = Pai[k];
	                Pai[k] = Pai[i];
	                Pai[i] = temp;  
                }
                else{
                	i++;
                }
            }
        }	
        return Pai;
	}
	
	/**
     * 对给定数目的自0开始步长为1的数字序列进行乱序
      * @param no 给定数目
      * @return 乱序后的数组
      */
     public static int[] getSequence(boolean... needRandom) {
        if(needRandom.length==0||!needRandom[0]){
        	return sequence;
        }
//         for(int i = 0; i < 26; i++){
//            sequence[i] = i;
//        }
         Random random = new Random();
         for(int i = 0; i < 26; i++){
            int p = random.nextInt(26);
         int tmp = sequence[i];
             sequence[i] = sequence[p];
           sequence[p] = tmp;
        }
        random = null;
        return sequence;
    }
}
