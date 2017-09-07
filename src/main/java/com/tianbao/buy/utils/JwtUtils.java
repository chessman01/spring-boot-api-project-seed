package com.tianbao.buy.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

/**
 * 会话使用 WebToken 的方式跟踪，非使用传统的 session ，此服务提供 WebToken 相关的功能.
 */
public class JwtUtils {
    public static String COOKIE_TOKEN_KEY = "jwtToken";

    public static String OPEN_ID = "openId";

    private static final Log logger = LogFactory.getLog(JwtUtils.class);

    private static Key key = MacProvider.generateKey();

    /**
     * 使用指定的主题生成 token
     *
     * @param openId 指定的信息
     * @return 生成的 token
     */
    public static String generate(String openId, int seconds) {
        DateTime expirationTime = new DateTime().plusSeconds(seconds);

        return Jwts.builder()
                .setIssuer("api.tianbao.com")
                .setSubject(openId)
                .setIssuedAt(new Date())
                .setExpiration(expirationTime.toDate())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    /**
     * 校验指定的主题的 token 是否福匹配
     *
     * @param openId openId
     * @param token 被校验的 token
     * @return 当校验成功时返回 <code>true</code>
     */
    public static boolean verify(String openId, String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject().equals(openId);
        } catch (Exception e) {
            logger.error("Verify fail:", e);
            return false;
        }
    }
}
