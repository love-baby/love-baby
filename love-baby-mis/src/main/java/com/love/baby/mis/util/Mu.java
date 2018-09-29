package com.love.baby.mis.util;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;

import java.io.File;
import java.util.Iterator;

/**
 * Created by liangbc on 2018/9/29.
 */
public class Mu {
    public static void main(String[] args) {
        try {


            AudioFile audioFile = AudioFileIO.read(new File("F://audios/差一点_庄心妍.flac"));
            Tag tag = audioFile.getTag();
            Iterator<TagField> it = tag.getFields();
            while (it.hasNext()){
                TagField f = it.next();
                System.out.println(f.getId()+":"+f.toString());
//                System.out.println(it.next().toString());
            }


//            if (mp3File.hasID3v2Tag()) {
//                AbstractID3v2Tag id3v2Tag = mp3File.getID3v2Tag();
//                System.out.println(id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_TITLE));
//                System.out.println(id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM));
//                System.out.println(id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST));
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
