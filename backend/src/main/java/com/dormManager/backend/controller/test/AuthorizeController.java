package com.dormManager.backend.controller.test;

import com.dormManager.backend.entity.RestBean;
import com.dormManager.backend.entity.vo.request.EmailRegisterVO;
import com.dormManager.backend.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    @Resource
    AccountService service;

    /**
     * 添加验证的validate
     *
     * @param email
     * @param type
     * @param request
     * @return
     */
    @GetMapping("/ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email,
                                        @RequestParam @Pattern(regexp = "(register|reset)") String type,
                                        HttpServletRequest request) {
        String message = service.registerEmailVerifyCode(type, email, request.getRemoteAddr());
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }

    /**
     * 处理通过邮箱注册账户的请求。
     *
     * @param vo 包含注册信息的EmailRegisterVO对象
     * @return 包含操作结果的RestBean对象，成功时code为200，失败时code为400，包含错误消息
     */
    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody @Valid EmailRegisterVO vo) {
        return this.messageHandle(() -> service.registerEmailAccount(vo));
    }

    /**
     * 处理包含操作的Supplier，并封装结果为RestBean对象。
     *
     * @param action 包含操作的Supplier
     * @return 包含操作结果的RestBean对象，成功时code为200，失败时code为400，包含错误消息
     */
    private RestBean<Void> messageHandle(Supplier<String> action) {
        String message = action.get();
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }
}
