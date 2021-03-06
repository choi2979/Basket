package com.example.basket.logical;

import android.util.Log;

import com.example.basket.loginFragment.KilFragment;
import com.example.basket.loginFragment.NilFragment;
import com.example.basket.vo.MemberDTO;

import java.util.HashMap;
import java.util.Map;

public class HashUtil {
    public static final String TAG = "HashUtil";
    public static void mapToDTOBinder(Map<String, Object> profileMap, String mem_entrance){
        Log.i(TAG, "HashUtil - mapToVOBinder");

        /****************************************************
         *  NAVER 콜백컬럼들
         *  id : 19044688
         *  email : jhleelego@naver.com
         *  nickname : 초보개발자
         *  age : 20-29
         *  gender : M
         *  name : 이정훈
         *  birthday : 03-07
         *
         *  KAKAO 콜백컬럼들
         *  id : 1392706345
         *  email : jhleelego@naver.com
         *  nickname : 이정훈
         *  age : AGE_20-29
         *  gender : MALE
         *  birthday : 0307
         * 
         *  set되는 MemberDTO변수를
         *  mem_id : String
         *  mem_name : String
         *  mem_age : String
         *  mem_getder : String
         *  mem_birth : String
         ****************************************************/
        if(profileMap!=null){
            MemberDTO memberDTO = MemberDTO.getInstance();
            for(Map.Entry vMap : profileMap.entrySet()) {
                Log.i(TAG, vMap.getValue().toString());
                if (vMap.getKey().equals("email")) {
                    memberDTO.setMem_email(vMap.getValue().toString());
                } else if (vMap.getKey().equals("name")) {
                    memberDTO.setMem_name(vMap.getValue().toString());
                } else if (vMap.getKey().equals("age")) {
                    if(mem_entrance.equals(NilFragment.TAG)){
                        memberDTO.setMem_age((vMap.getValue().toString()).substring(0,1));
                    } else if(mem_entrance.equals(KilFragment.TAG)){
                        memberDTO.setMem_age((vMap.getValue().toString()).substring(4,5));
                    }
                } else if (vMap.getKey().equals("gender")) {
                    if(vMap.getValue().equals("M")||vMap.getValue().equals("MALE")){
                        memberDTO.setMem_gender("남");
                    } else if(vMap.getValue().equals("W")||vMap.getValue().equals("FEMALE")){
                        memberDTO.setMem_gender("여");
                    }
                } else if (vMap.getKey().equals("birthday")) {
                    memberDTO.setMem_birth(vMap.getValue().toString());
                }
            }
            memberDTO.setMem_entrance(mem_entrance);
            Log.i(TAG, "생성된컬럼 시작 ");
            memberDTO.toString();
            Log.i(TAG, "생성된컬럼  끝 ");
        }
    }
}
