package com.love.baby.common.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.love.baby.common.exception.SystemException;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liangbc on 2018/10/11.
 */
public class QiNiuUtil {

    private static Logger logger = LoggerFactory.getLogger(QiNiuUtil.class);

    private static String accessKey = "61DvPxqmhMIEIVk4QtHzUQ15i6ZoqycECFm7PZY9";
    private static String secretKey = "MYFjE43xF15_Rq606H1XuWxDezWchfb0W16xnb5C";
    /**
     * 防盗链key 备用 05b8cdb97fbabe88e88982a94698d78813aa589b
     */
    private static String encryptKey = "809907b8b1b86b8a4998529a80abb48b1c9a3388";


    public class Bucket {
        public static final String MUSIC = "music|http://music.love-baby.vip";
        public static final String LOVE_BABY_IMG = "love-baby-img|https://images.love-baby.vip";
        public static final String WEB_CSS = "web-css|https://css.love-baby.vip";
        public static final String WEB_JS = "web-js|https://js.love-baby.vip";
    }


    public static String fileUpload(byte[] uploadBytes, String bucket, String key) {
        if (StringUtils.isBlank(bucket)) {
            throw new SystemException(500, "bucket参数非法！");
        }
        if (StringUtils.isBlank(key)) {
            throw new SystemException(500, "key参数非法！");
        }
        Configuration cfg = new Configuration(Zone.zone1());
        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(accessKey, secretKey);
        String[] arr = StringUtils.split(bucket, "|");
        String upToken = auth.uploadToken(arr[0]);
        try {
            Response response = uploadManager.put(uploadBytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            logger.info("上传结果 putRet = {}", JSON.toJSON(putRet));
            return arr[1] + "/" + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            logger.error(r.toString());
            try {
                logger.error(r.bodyString());
            } catch (QiniuException ex2) {
                logger.error("上传文件出错", ex2);
            }
        }
        return null;
    }


    /**
     * 生成资源基于CDN时间戳防盗链的访问外链
     *
     * @param
     * @param
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static String getAntiLeechAccessUrlBasedOnTimestamp(String url, int durationInSeconds) {
        try {
            URL urlObj = new URL(url);
            String path = urlObj.getPath();
            long timestampNow = System.currentTimeMillis() / 1000 + durationInSeconds;
            String expireHex = Long.toHexString(timestampNow);
            String toSignStr = String.format("%s%s%s", encryptKey, path, expireHex);
            String signedStr = md5ToLower(toSignStr);
            if (urlObj.getQuery() != null) {
                return String.format("%s&sign=%s&t=%s", url, signedStr, expireHex);
            } else {
                return String.format("%s?sign=%s&t=%s", url, signedStr, expireHex);
            }

        } catch (Exception e) {
            logger.error("防盗链生成失败", e);
            return null;
        }
    }

    private static String md5ToLower(String src) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(src.getBytes("utf-8"));
        byte[] md5Bytes = digest.digest();
        return Hex.encodeHexString(md5Bytes);
    }

}
