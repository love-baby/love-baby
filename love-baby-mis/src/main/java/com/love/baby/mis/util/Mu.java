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


            AudioFile audioFile = AudioFileIO.read(new File("D:\\IdeaProjects\\jaudiotagger\\testdata\\那个人_周延英 (英子-effie).wav"));
            Tag tag = audioFile.getTag();

            String a = tag.getFirst(FieldKey.ARTIST);
            String b = tag.getFirst(FieldKey.ALBUM);
            String c = tag.getFirst(FieldKey.TITLE);
            String d =  tag.getFirst(FieldKey.COMMENT);
            String e = tag.getFirst(FieldKey.YEAR);
            String f =  tag.getFirst(FieldKey.TRACK);


            tag.setField(FieldKey.TITLE, "标题");
            tag.setField(FieldKey.ALBUM, "专辑");
            tag.setField(FieldKey.ARTIST, "作者");
            audioFile.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
