package com.wanwan.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtils {

    // token过期时间 24h
    public static final long EXPIRE = 1000 * 60 * 60 * 24;

    // 秘钥
    public static final String APP_SECRET = "saseessrtkookppijhfewewsadhuutresxvhjkk";

    // 生成token字符串的方法
    public static String createToken(Long userId,String userType) {
        return Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("alg", "HS256")

                .setSubject("police-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))

                .claim("userId", userId)
                .claim("userType", userType)

                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
    }

    /**
     * 判断token是否存在与有效
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("Authorization");
            if (jwtToken == null || StringUtils.isEmpty(jwtToken)) {
                return false;
            }
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 根据token字符串获取userId
     */
    public static Long getUserIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwtToken)) {
            throw new RuntimeException("未认证");
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return claims.get("userId", Long.class);
    }
}
