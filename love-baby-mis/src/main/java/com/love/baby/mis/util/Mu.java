package com.love.baby.mis.util;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;

/**
 * Created by liangbc on 2018/9/29.
 */
public class Mu {
    public static void main(String[] args) {
        try {
            MP3File mp3File = new MP3File("F://audios/爱如潮水_张信哲.mp3");


            if (mp3File.hasID3v2Tag()) {
                AbstractID3v2Tag id3v2Tag = mp3File.getID3v2Tag();
                System.out.println(id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_TITLE));
                System.out.println(id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM));
                System.out.println(id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static String toGB2312(String s) {
        try {
            return new String(s.getBytes("ISO-8859-1"), "gb2312");
        } catch (Exception e) {
            return s;
        }
    }
}
