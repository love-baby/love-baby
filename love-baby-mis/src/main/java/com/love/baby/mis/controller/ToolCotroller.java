package com.love.baby.mis.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.love.baby.common.bean.UploadFile;
import com.love.baby.common.common.UserSessionCommon;
import com.love.baby.common.exception.SystemException;
import com.love.baby.mis.config.SystemConfig;
import com.love.baby.mis.service.MusicService;
import com.love.baby.mis.service.UploadFileService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

/**
 * @author liangbc
 * @date 2018/6/14
 */
@RestController
@RequestMapping("/tool")
public class ToolCotroller {

    private static Logger logger = LoggerFactory.getLogger(ToolCotroller.class);

    @Resource
    private UploadFileService uploadFileService;

    @Resource
    private UserSessionCommon userSessionCommon;

    @Resource
    private MusicService musicService;

//    @PostMapping(value = "/analyze/music")
//    public void music(@RequestParam("file") MultipartFile file, @RequestHeader(value = "token") String token) throws InvalidDataException, IOException, UnsupportedTagException {
//        String userId = userSessionCommon.assertSessionAndGetUid(token);
//        UploadFile uploadFile = fileUpload(file, token);
//        Mp3File mp3file = new Mp3File(uploadFile.getPath());
//
//        Music music = Music.builder().
//                id(UUID.randomUUID().toString().replaceAll("-", "")).
//                createTime(new Date())
//                .path(uploadFile.getPath())
//                .build();
//        if (mp3file.hasId3v1Tag()) {
//            ID3v1 id3v1 = mp3file.getId3v1Tag();
//            music.setName(id3v1.getTitle());
//            music.setAlbumId(id3v1.getAlbum());
//            music.setAuthorId(id3v1.getArtist());
//        }
//        if (mp3file.hasId3v2Tag()) {
//            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
//            music.setName(id3v2Tag.getTitle());
//            music.setAlbumId(id3v2Tag.getAlbum());
//            music.setAuthorId(id3v2Tag.getArtist());
//            byte[] albumImageData = id3v2Tag.getAlbumImage();
//            if (albumImageData != null) {
//                String coverPath = new File(uploadFile.getPath()).getPath() + ".png";
//                RandomAccessFile randomAccessFile = new RandomAccessFile(coverPath, "rw");
//                randomAccessFile.write(albumImageData);
//                randomAccessFile.close();
//                music.setCoverPath(coverPath);
//            }
//        }
//        musicService.save(music);
//    }

    /**
     * 上传文件
     *
     * @param file
     * @throws IOException
     */
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public UploadFile fileUpload(@RequestParam("file") MultipartFile file) throws SystemException, IOException {
//        String userId = userSessionCommon.assertSessionAndGetUid(token);
        //文件名称
        String path = SystemConfig.SystemPath + File.separator + "upload" + File.separator + LocalDate.now();
        File filePath = new File(path);
        String type = file.getContentType();
        String originFileName = file.getOriginalFilename();
        path = path + File.separator + System.currentTimeMillis() + originFileName;
        if (!filePath.exists() && !filePath.isDirectory()) {
            if (!filePath.mkdirs()) {
                logger.error("创建目标文件所在目录失败！");
                throw new SystemException(500, "创建目标文件所在目录失败");
            }
        }
        byte[] bytes = file.getBytes();
        BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path));
        buffStream.write(bytes);
        buffStream.close();
        UploadFile uploadFile = UploadFile.builder()
                .id(UUID.randomUUID().toString().replaceAll("-", ""))
                .createTime(new Date())
                .name(originFileName)
                .path(path)
                .fileType(type)
                .build();
        uploadFileService.save(uploadFile);
        return uploadFile;
    }


    LinkedList<FileMeta> files = new LinkedList<>();
    FileMeta fileMeta = null;

    /***************************************************
     * URL: /rest/controller/upload
     * upload(): receives files
     * @param request : MultipartHttpServletRequest auto passed
     * @param response : HttpServletResponse auto passed
     * @return LinkedList<FileMeta> as json format
     ****************************************************/
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public LinkedList<FileMeta> upload(MultipartHttpServletRequest request, HttpServletResponse response) {

        //1. build an iterator
        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = null;

        //2. get each file
        while (itr.hasNext()) {

            //2.1 get next MultipartFile
            mpf = request.getFile(itr.next());
            System.out.println(mpf.getOriginalFilename() + " uploaded! " + files.size());

            //2.2 if files > 10 remove the first from the list
            if (files.size() >= 10) {
                files.pop();
            }
            //2.3 create new fileMeta
            fileMeta = new FileMeta();
            fileMeta.setFileName(mpf.getOriginalFilename());
            fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
            fileMeta.setFileType(mpf.getContentType());

            try {
                fileMeta.setBytes(mpf.getBytes());

                // copy file to local disk (make sure the path "e.g. D:/temp/files" exists)
                FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream("D:/temp/files/" + mpf.getOriginalFilename()));

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //2.4 add to files
            files.add(fileMeta);
        }
        // result will be like this
        // [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
        return files;
    }

    /***************************************************
     * URL: /rest/controller/get/{value}
     * get(): get file as an attachment
     * @param response : passed by the server
     * @param value : value from the URL
     * @return void
     ****************************************************/
    @RequestMapping(value = "/get/{value}", method = RequestMethod.GET)
    public void get(HttpServletResponse response, @PathVariable String value) {
        FileMeta getFile = files.get(Integer.parseInt(value));
        try {
            response.setContentType(getFile.getFileType());
            response.setHeader("Content-disposition", "attachment; filename=\"" + getFile.getFileName() + "\"");
            FileCopyUtils.copy(getFile.getBytes(), response.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Data
    @NoArgsConstructor(force = true)
    @AllArgsConstructor
    @JsonIgnoreProperties({"bytes"})
    public class FileMeta {
        private String fileName;
        private String fileSize;
        private String fileType;

        private byte[] bytes;

        //setters & getters
    }

}
