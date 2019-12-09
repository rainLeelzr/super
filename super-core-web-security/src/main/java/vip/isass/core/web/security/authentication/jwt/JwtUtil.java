package vip.isass.core.web.security.authentication.jwt;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.map.MapUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import vip.isass.core.login.LoginUser;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * @author Rain
 */
public class JwtUtil {

    public static String PREFIX = "Bearer ";

    public static long TOKEN_EFFECTIVE_SECONDS = ChronoUnit.WEEKS.getDuration().getSeconds();

    public static long TOKEN_EFFECTIVE_MILLS = TOKEN_EFFECTIVE_SECONDS * 1000;

    public static String generateToken(LoginUser loginUser, String secret) {
        // 生产 token
        Map<String, Object> map = MapUtil.<String, Object>builder()
            .put(JwtClaim.USER_ID, loginUser.getUserId())
            .put(JwtClaim.NICK_NAME, loginUser.getNickName())
            .put(JwtClaim.FROM, loginUser.getLoginFrom())
            .put(JwtClaim.VERSION, loginUser.getVersion())
            .build();
        return Jwts.builder()
            .setClaims(map)
            .setExpiration(new Date(SystemClock.now() + TOKEN_EFFECTIVE_MILLS))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

}
