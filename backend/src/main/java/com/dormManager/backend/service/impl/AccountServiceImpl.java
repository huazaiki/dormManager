package com.dormManager.backend.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dormManager.backend.entity.dto.Account;
import com.dormManager.backend.entity.vo.request.EmailRegisterVO;
import com.dormManager.backend.mapper.AccountMapper;
import com.dormManager.backend.service.AccountService;
import com.dormManager.backend.utils.Const;
import com.dormManager.backend.utils.FlowUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    FlowUtils flowUtils;

    @Resource
    PasswordEncoder encoder;

    /**
     * 根据用户名加载用户详细信息，用于Spring Security的身份验证。
     *
     * @param username 要加载的用户的用户名或邮箱地址
     * @return 包含用户详细信息的UserDetails对象
     * @throws UsernameNotFoundException 如果找不到用户，则抛出此异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.findAccountByNameOrEmail(username);
        if (account == null) throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    /**
     * 使用查询构造器根据用户名或邮箱在数据库中查找对应的账户信息。
     *
     * @param text 用户名或邮箱地址
     * @return 包含账户信息的Account对象，如果找不到则返回null
     */
    public Account findAccountByNameOrEmail(String text) {
        return this.query()
                .eq("username", text).or()
                .eq("email", text)
                .one();
    }

    /**
     * 发送注册时的邮箱验证代码，并进行频率限制检查。
     *
     * @param type  验证码类型
     * @param email 目标邮箱地址
     * @param ip    请求的IP地址
     * @return 如果通过频率限制检查，返回null；否则返回提示信息
     */
    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        synchronized (ip.intern()) {
            if (!this.verifyLimit(ip)) {
                return "请求频繁，请稍后再试";
            }
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            Map<String, Object> data = Map.of("type", type, "email", email, "code", code)   ;
            amqpTemplate.convertAndSend("mail", data);
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        }
    }

    /**
     * 对邮箱验证的请求进行频率限制检查。
     *
     * @param ip 请求的IP地址
     * @return 如果通过限制检查，返回true；否则返回false
     */
    public boolean verifyLimit(String ip) {
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }

    /**
     * 处理通过邮箱注册账户的请求，包括验证验证码、检查邮箱和用户名是否已注册，并保存新账户信息。
     *
     * @param vo 包含注册信息的EmailRegisterVO对象
     * @return 如果注册成功，返回null；否则返回相应的错误提示信息
     */
    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String key = Const.VERIFY_EMAIL_DATA + email;
        String username = vo.getUsername();
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + email);
        if (code == null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "验证码有误，请重新输入";
        if (isExistAccountEmail(email)) return "此邮箱已被其他用户注册，请更改邮箱地址";
        if (isExistAccountUsername(username)) return "此用户名已被其他用户注册，请更改用户名";
        String password = encoder.encode(vo.getPassword());
        Account account = new Account(null, username, password, email, "user", new Date());
        if (this.save(account)) {
            stringRedisTemplate.delete(key);
            return null;
        } else {
            return "内部错误，请联系管理员";
        }
    }

    private boolean isExistAccountEmail(String email) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email", email));
    }

    private boolean isExistAccountUsername(String username) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username", username));
    }
}
