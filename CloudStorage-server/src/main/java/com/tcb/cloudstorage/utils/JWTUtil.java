package com.tcb.cloudstorage.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/*
 * code for class JWTUtil
 * @param null
 * JWT工具类，对用户名密码加密，实现Token的生成，校验token，获取用户名等操作
 * @version 1.0.0
 * @return
 * @author Zhou Yun
 * @date 2022/10/6 9:54
 **/
public class JWTUtil
{

    //设置过期时间为1天
    private static final long EXPIRE_TIME = 5* 60 * 60 * 1000L;

    private static final String JWT_KEY =  "ZHOU";


    //UUID:通用唯一识别码，由8个十六进制数—4个十六进制数—4个十六进制数—4个十六进制数—12个十六进制数
    public static String getUUID()
    {
        String token = UUID.randomUUID().toString().replaceAll("-","");
        return token;
    }

    /**
     * 生成jwt
     * @param subject token中要存放的数据
     * @return String
     */
    public static String createJwt(String subject)
    {
        JwtBuilder jwtBuilder = getJwtBuilder(subject, null, getUUID());
        return jwtBuilder.compact();
    }

    /**
     * 生成jwt
     * @param subject token中要存放的数据
     * @param ttlMills token过期时间
     * @return String
     */
    public static String createJwt(String subject, Long ttlMills)
    {
        JwtBuilder jwtBuilder = getJwtBuilder(subject, ttlMills, getUUID());
        return jwtBuilder.compact();
    }
    /**
     * 生成jwt
     * @param id
     * @param subject token中要存放的数据
     * @param ttlMills token过期时间
     * @return String
     */
    public static String createJwt(String subject, Long ttlMills,String id)
    {
        JwtBuilder jwtBuilder = getJwtBuilder(subject, ttlMills, id);
        return jwtBuilder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMills, String uuid)
    {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);
        if (ttlMills == null)
        {
            ttlMills = JWTUtil.EXPIRE_TIME;
        }
        long expMills = nowMills + ttlMills;
        Date expDate = new Date(expMills);
        return Jwts.builder()
                .setId(uuid) //唯一的id
                .setSubject(subject)  //主题 json数据
                .setIssuer("zy")     //签发者
                .setIssuedAt(now)     //签发时间
                .signWith(signatureAlgorithm, secretKey)   //加密算法，加密秘钥
                .setExpiration(expDate);
    }

    /**
     * 生成加密后的秘钥
     * @return
     */
    private static SecretKey generalKey()
    {
        byte[] encodeKey = Base64.getDecoder().decode(JWTUtil.JWT_KEY);
        SecretKey  secretKey = new SecretKeySpec(encodeKey,0,encodeKey.length,"AES");
        return secretKey;
    }

    /**
     * 解析token
     *
     * @param jwt 密钥
     * @return
     */
    public static Claims parseJwt(String jwt)
    {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

}