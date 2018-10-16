package com.love.baby.mis.util;

import com.love.baby.common.util.QiNiuUtil;

/**
 * Created by liangbc on 2018/9/29.
 */
public class Mu {
    public static void main(String[] args) {
//        try {
//
//
//            AudioFile audioFile = AudioFileIO.read(new File("C:\\Users\\23770\\Downloads\\1538664002512_daf029f987159d0a33788d109274049c.mp3"));
//            Tag tag = audioFile.getTag();
//
//            String a = tag.getFirst(FieldKey.ARTIST);
//            System.out.println(new String(a.getBytes(),"GBK"));
//            String b = tag.getFirst(FieldKey.ALBUM);
//            String c = tag.getFirst(FieldKey.TITLE);
//            String d =  tag.getFirst(FieldKey.COMMENT);
//            String e = tag.getFirst(FieldKey.YEAR);
//            String f =  tag.getFirst(FieldKey.TRACK);
//
//
//            tag.setField(FieldKey.TITLE, "叫姐姐");
//            tag.setField(FieldKey.ALBUM, "112312");
//            tag.setField(FieldKey.ARTIST, "112321321");
//            audioFile.commit();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        System.out.println(QiNiuUtil.getAntiLeechAccessUrlBasedOnTimestamp("http://music.love-baby.vip/a7b85c94309ab6388e4ebe4f9de1972b.mp3",6000));
    }




}
