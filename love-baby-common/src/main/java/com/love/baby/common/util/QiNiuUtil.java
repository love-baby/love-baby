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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liangbc on 2018/10/11.
 */
public class QiNiuUtil {

    private static Logger logger = LoggerFactory.getLogger(QiNiuUtil.class);

    public class Bucket {
        public static final String MUSIC = "music|music.love-baby.vip";
        public static final String LOVE_BABY_IMG = "love-baby-img|images.love-baby.vip";
        public static final String WEB_CSS = "web-css|css.love-baby.vip";
        public static final String WEB_JS = "web-js|js.love-baby.vip";
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
        String accessKey = "61DvPxqmhMIEIVk4QtHzUQ15i6ZoqycECFm7PZY9";
        String secretKey = "MYFjE43xF15_Rq606H1XuWxDezWchfb0W16xnb5C";
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
}
