package com.dormManager.backend.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * AuthorizeVO 类表示用户授权的值对象。
 *
 * <p>此类使用了 Lombok 的 {@code @Data} 注解，自动生成 getter、setter 和 toString 等方法。
 * 它包括用户名、角色、令牌以及令牌过期时间等属性，用于在用户授权时传递相关信息。</p>
 *
 * <p>例如：</p>
 * <pre>
 *     // 创建一个 AuthorizeVO 对象
 *     AuthorizeVO authorizeVO = new AuthorizeVO();
 *     authorizeVO.setUsername("john_doe");
 *     authorizeVO.setRole("ROLE_USER");
 *     authorizeVO.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
 *     authorizeVO.setExpireTime(new Date(System.currentTimeMillis() + 3600000)); // 1 hour later
 * </pre>
 *
 * @see lombok.Data
 */
@Data
public class AuthorizeVO {
    private String username;
    private String role;
    private String token;
    private Date expireTime;
}
