package com.love.baby.mis.util;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

/**
 * Created by liangbc on 2018/9/29.
 */
public class Mu {
    public static void main(String[] args) {
        try {


            AudioFile audioFile = AudioFileIO.read(new File("F://audios/Hands Free_Keke Palmer.mp3"));
            Tag tag = audioFile.getTag();
            String a = tag.getFirst(FieldKey.ARTIST);
            String b = tag.getFirst(FieldKey.ALBUM);
            String c = tag.getFirst(FieldKey.TITLE);
            String d =  tag.getFirst(FieldKey.COMMENT);
            String e = tag.getFirst(FieldKey.YEAR);
            String f =  tag.getFirst(FieldKey.TRACK);
            String g = tag.getFirst(FieldKey.DISC_NO);
            String h = tag.getFirst(FieldKey.COMPOSER);
            String i = tag.getFirst(FieldKey.ARTIST_SORT);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
