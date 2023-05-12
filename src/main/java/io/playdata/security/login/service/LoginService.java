package io.playdata.security.login.service;

import io.playdata.security.login.model.AccountDTO;
import io.playdata.security.login.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // join, register -> 유사 의미
    // username 중복시
    public AccountDTO register(AccountDTO accountDTO) throws Exception {
        if (accountRepository.findByUsername(accountDTO.getUsername()) != null) {
            throw new Exception("중복 유저 이름");
            // 예외처리 : try-catch문으로 묶어서 처리
            // " : 상위 메소드, 클래스에게 처리를 위임
        }
        // 로그인을 할 때는 bcrypt가 적용이 되었는데
        // 가입을 할 때는 bcrypt가 적용이 안됨
        String newPassword = passwordEncoder.encode(accountDTO.getPassword());
        accountDTO.setPassword(newPassword);

        // Controller로부터 accountDTO를 전달받아서 repository를 사용해서 DB 등록
        return accountRepository.save(accountDTO);
    }
}
