package io.playdata.security.login.repository;

import io.playdata.security.login.model.AccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // spring에 등록
public interface AccountRepository extends JpaRepository<AccountDTO, Long> {
    AccountDTO findByUsername(String username); // Username으로 계정을 찾는 메소드
}
