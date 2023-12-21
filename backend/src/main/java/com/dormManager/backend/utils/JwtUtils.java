package com.dormManager.backend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * JwtUtils 类提供了生成 JWT 令牌的工具方法。
 *
 * <p>此组件使用了 Spring 的 {@code @Value} 注解来注入密钥和过期时间，通过调用 {@code createJwt} 方法生成 JWT 令牌。
 * 这个令牌包括用户的身份信息、权限列表以及过期时间等信息。</p>
 *
 * <p>例如：</p>
 * <pre>
 *     &#64;Autowired
 *     private JwtUtils jwtUtils;
 *
 *     // 使用 JwtUtils 创建 JWT 令牌
 *     String token = jwtUtils.createJwt(userDetails, userId, username);
 * </pre>
 *
 * @see org.springframework.stereotype.Component
 * @see org.springframework.beans.factory.annotation.Value
 * @see com.auth0.jwt.algorithms.Algorithm
 * @see com.auth0.jwt.JWT
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see org.springframework.security.core.GrantedAuthority
 */
@Component
public class JwtUtils {

    /**
     * JWT 密钥，从配置文件中注入。
     */
    @Value("${spring.security.jwt.key}")
    private String key;

    /**
     * JWT 令牌过期时间（小时），从配置文件中注入。
     */
    @Value("${spring.security.jwt.expire}")
    private int expireTime;

    @Resource
    StringRedisTemplate template;

    /**
     * 创建 JWT 令牌。
     *
     * @param details  UserDetails 对象，包含用户身份信息和权限列表
     * @param id       用户ID
     * @param username 用户名
     * @return 生成的 JWT 令牌
     */
    public String createJwt(UserDetails details, int id, String username) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expireTime = this.expireTime();
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("id", id)
                .withClaim("name", username)
                .withClaim("authorities", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(expireTime)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    /**
     * 解析 JWT 令牌。
     *
     * <p>该方法接收一个包含 JWT 令牌的 Authorization 头部信息，通过提取令牌、验证签名和检查过期时间等步骤，
     * 返回解码后的 JWT 对象（DecodedJWT）。如果解析失败或过期，则返回 null。</p>
     *
     * @param headerToken 包含 JWT 令牌的 Authorization 头部信息
     * @return 解析后的 DecodedJWT 对象，如果解析失败或过期则返回 null
     */
    public DecodedJWT resolveJwt(String headerToken) {
        // 提取 Bearer 令牌
        String token = this.extractBearerToken(headerToken);
        // 检查提取的令牌是否有效
        if (token == null) return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT verify = jwtVerifier.verify(token);
            if (this.isInvalidToken(verify.getId()))
                return null;
            Date expireTime = verify.getExpiresAt();
            return new Date().after(expireTime) ? null : verify;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    /**
     * 使 JWT 令牌失效的方法。
     *
     * <p>此方法首先从 Authorization 头部提取 Bearer 令牌，然后使用密钥和 HMAC256 算法创建 JWTVerifier 对象。
     * 接着，它尝试使用 JWTVerifier 验证令牌的签名和内容。如果验证成功，从令牌中获取 ID 和过期时间，并调用
     * deleteToken 方法使令牌失效。</p>
     *
     * @param headerToken 包含 JWT 令牌的 Authorization 头部信息
     * @return 如果令牌成功失效，返回 true；否则返回 false
     */
    public boolean invalidateJwt(String headerToken) {
        String token = this.extractBearerToken(headerToken);
        if (token == null) return false;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = jwtVerifier.verify(token);
            String id = jwt.getId();
            return deleteToken(id, jwt.getExpiresAt());
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 将 DecodedJWT 转换为 Spring Security 的 UserDetails 对象。
     *
     * <p>此方法接收一个 DecodedJWT 对象，从中提取用户的声明信息，并创建一个对应的 UserDetails 对象。
     * 用户的用户名、密码和权限信息都从 JWT 的声明中获取。</p>
     *
     * @param jwt 包含用户声明信息的 DecodedJWT 对象
     * @return 转换后的 UserDetails 对象
     */
    public UserDetails toUser(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return User
                .withUsername(claims.get("name").asString())
                .password("123456")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }

    public Integer toId(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }

    /**
     * 计算令牌的过期时间。
     *
     * @return 过期时间的 Date 对象
     */
    public Date expireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expireTime * 24);
        return calendar.getTime();
    }

    /**
     * 将包含在 Authorization 头部中的 Bearer 令牌信息提取出来。
     *
     * @param headerToken 包含 JWT 令牌的 Authorization 头部信息
     * @return 提取出的 JWT 令牌字符串，如果未找到或格式不正确则返回 null
     */
    private String extractBearerToken(String headerToken) {
        if (headerToken == null || !headerToken.startsWith("Bearer ")) {
            return null;
        }
        return headerToken.substring(7);
    }

    /**
     * 使令牌失效并加入黑名单的方法。
     *
     * <p>此方法首先调用 invalidateJwt 方法使令牌失效，然后计算令牌的剩余有效期，并将令牌加入黑名单。
     * 如果令牌失效成功，则在 Redis 中设置相应的键值对，并返回 true；否则返回 false。</p>
     *
     * @param uuid 令牌的唯一标识
     * @param time 令牌的过期时间
     * @return 如果令牌成功失效并加入黑名单，返回 true；否则返回 false
     */
    private boolean deleteToken(String uuid, Date time) {
        if (this.invalidateJwt(uuid)) return false;
        Date now = new Date();
        long expire = Math.max(time.getTime() - now.getTime(), 0);
        template.opsForValue().set(Const.JWT_BLACK_LIST + uuid, "", expire, TimeUnit.MILLISECONDS);
        return true;
    }

    /**
     * 检查令牌是否在黑名单中。
     *
     * <p>此方法检查 Redis 中是否存在以令牌唯一标识为键的键值对，如果存在则表示令牌已经加入黑名单。</p>
     *
     * @param uuid 令牌的唯一标识
     * @return 如果令牌在黑名单中，返回 true；否则返回 false
     */
    public boolean isInvalidToken(String uuid) {
        return Boolean.TRUE.equals(template.hasKey(Const.JWT_BLACK_LIST + uuid));
    }
}
