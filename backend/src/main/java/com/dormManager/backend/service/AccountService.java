package com.dormManager.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dormManager.backend.entity.dto.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {

    Account findAccountByNameOrEmail(String text);
}
