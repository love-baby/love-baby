//package com.love.baby.mis.util;
//
//import org.jaudiotagger.audio.mp3.MP3File;
//import org.jaudiotagger.tag.Tag;
//import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
//import org.jaudiotagger.tag.id3.ID3v24Frames;
//
///**
// * Created by liangbc on 2018/9/29.
// */
//public class Mu {
//    public static void main(String[] args) {
//        try {
//            MP3File f = new MP3File("F://audios/爱如潮水_张信哲.mp3");
//
//            Tag tag = f.getTag();
//            AbstractID3v2Tag v2tag = f.getID3v2Tag();
//            System.out.println(v2tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST));
//            System.out.println(v2tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM));
//            System.out.println(v2tag.getFirst(ID3v24Frames.FRAME_ID_TITLE));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private static String toGB2312(String s) {
//        try {
//            return new String(s.getBytes("ISO-8859-1"), "gb2312");
//        } catch (Exception e) {
//            return s;
//        }
//    }
//}
