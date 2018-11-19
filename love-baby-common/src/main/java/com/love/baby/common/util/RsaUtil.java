package com.love.baby.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * Created by liangbc on 2017/11/23.
 */
public class RsaUtil {

    private static Logger logger = LoggerFactory.getLogger(RsaUtil.class);

    public static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 生成密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        //获得对象 KeyPairGenerator 参数 RSA 1024个字节
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        //通过对象 KeyPairGenerator 获取对象KeyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();

        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        logger.info("publicKeyStr = {}", new String(Base64Utils.encode(publicKey.getEncoded())));
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        logger.info("privateKeyStr = {}", new String(Base64Utils.encode(privateKey.getEncoded())));
        //公私钥对象存入map中
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 根据字符串获取公钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes =  Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 根据字符串获取私钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes =  Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 用私钥加密
     *
     * @param content
     * @param privateKey
     * @param charset
     * @return
     */
    public static String rsaSign(String content, String privateKey, String charset) throws Exception {
        PrivateKey priKey = getPrivateKey(privateKey);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(priKey);
        if (StringUtils.isEmpty(charset)) {
            signature.update(content.getBytes());
        } else {
            signature.update(content.getBytes(charset));
        }
        byte[] signed = signature.sign();
        return Base64.encodeBase64URLSafeString(signed);
    }

    /**
     * 校验签名
     *
     * @param content
     * @param sign
     * @param publicKey
     * @param charset
     * @return
     * @throws Exception
     */
    public static boolean rsaCheckContent(String content, String sign, String publicKey, String charset) throws Exception {
        logger.info("content = " + content);
        logger.info("sign = " + sign);
        logger.info("publicKey = " + publicKey);
        logger.info("charset = " + charset);
        PublicKey pubKey = getPublicKey(publicKey);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(pubKey);
        if (StringUtils.isEmpty(charset)) {
            signature.update(content.getBytes());
        } else {
            signature.update(content.getBytes(charset));
        }
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    /**
     * 处理要签名的参数
     *
     * @param map
     * @return
     */
    public static String buildParam(TreeMap<String, String> map) {
        List<Object> keys = new ArrayList<>(map.keySet());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            if (i != 0) {
                sb.append("&");
            }
            String key = String.valueOf(keys.get(i));
            String value = String.valueOf(map.get(key));
            sb.append(key);
            sb.append("=");
            sb.append(Base64.encodeBase64URLSafeString(value.getBytes()));
        }
        return Base64.encodeBase64URLSafeString(sb.toString().getBytes());
    }

    /**
     * 还原签名之前处理的参数
     *
     * @param str
     * @return
     */
    public static TreeMap buildParam(String str) {
        logger.info("解析原始字符串 str = {}", str);
        String[] arr = StringUtils.split(new String(Base64.decodeBase64(str)), "&");
        logger.info("decodeBase64后的字符串 arr = {}", JSON.toJSONString(arr));
        TreeMap treeMap = new TreeMap();
        for (int i = 0; i < arr.length; i++) {
            String[] param = StringUtils.split(arr[i], "=");
            if (param.length == 2) {
                treeMap.put(param[0], new String(Base64.decodeBase64(param[1])));
            }
        }
        logger.info("还原后参数 treeMap = {}", JSON.toJSONString(treeMap));
        return treeMap;
    }


    public static void main(String arr[]) throws Exception {
        TreeMap map = new TreeMap();
        map.put("name", "liangbc");
        map.put("age", 26);
        map.put("sex", "男");
        map.put("address", "北京");
        String str = buildParam(map);
        logger.info("待签名的字符串str = {}", str);
        String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCu46si2YLop8w8zrBz3tGENrh4oWueaBQVRH+Y8MrS1mRIbhzcu6jMIKhgjPEFDt9mRsMZhL+177rJaJukwmC7daARh9koQw3YGPC5q6afE2s8zDugeBJFGLb3uCYpU6RuzemK1JyowKXTHPX5CWQK5HFGn3BovjykUD/AmyBUFQIDAQAB";
        String prv = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK7jqyLZguinzDzOsHPe0YQ2uHiha55oFBVEf5jwytLWZEhuHNy7qMwgqGCM8QUO32ZGwxmEv7Xvuslom6TCYLt1oBGH2ShDDdgY8Lmrpp8TazzMO6B4EkUYtve4JilTpG7N6YrUnKjApdMc9fkJZArkcUafcGi+PKRQP8CbIFQVAgMBAAECgYAA1zFMYpVBfwT5SdkJRYnmQ8kRM11sDn4COCkQ2B9xcfxbaMQUv4YpLQMCK8tAxfiG6lInvj6ZpkRUkX8mBJUob3iCs3Q7Gayi7GlHRUBctfu9Fk9DUpoIUK95k1yz36RbkEC2H1w7dWYNE8xHSdhhibcqga4bmXRH4UnGAYbPYQJBANcbrGy6SXK6mRzble3OZ1EvdTdaU7Chr+9A1tZOaiDcrZ78fYYWpeeVLfAja0NVeQJXAwljDLnLVWB4SjNuBf0CQQDQIrtH39eTljbRqH8lWxfIXqsZELw6jehJU8huNqQZZhBU5j7StHttX+cgV8QfuOzTtu8wxz19ocxxR6E79dX5AkEAg48HS5A7b5G0JzQdZE4CXmLaG7qAyNx6j6dmPbfDrMmK3luqIaIkYZiZee8PoSTbV5UD5G/RzqgPVWr8Zqv5uQJAOQgrVUiK49AXeV+4Z14MqET4kUe6rDjSW7VviHPEh4kmFH163XXDFHdg98XU4fRkQKcx56XQvaomQSwljC6S0QJAGKYThOaDq8antoOaKpIc/NwuqY3i7HqwBQFy94uje00g6ZOQMYEQV0daTfWTZd38unHaOjZ7o4PF8T/xtY43aw==";

        String sign = rsaSign(str, prv, "utf-8");
        logger.info("签名：" + sign);
        if (rsaCheckContent(str, sign, pub, "utf-8")) {
            logger.info("校验签名通过");
            logger.info("解密：" + JSON.toJSONString(buildParam(str)));
        } else {
            logger.info("校验签名通过失败");
        }
    }

}
