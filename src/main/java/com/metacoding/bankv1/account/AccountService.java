package com.metacoding.bankv1.account;

import com.metacoding.bankv1.account.history.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final HistoryRepository historyRepository;

    @Transactional
    public void 계좌생성(AccountRequest.SaveDTO saveDTO, int userId) {
        accountRepository.save(saveDTO.getNumber(), saveDTO.getPassword(), saveDTO.getBalance(), userId);
    }

    public List<Account> 나의계좌목록(Integer userId) {
        return accountRepository.findAllByUserId(userId);
    }

    @Transactional
    public void 계좌이체(AccountRequest.TransferDTO transferDTO, int userId) {
        // 1. 출금 계좌 조회
        Account withdrawAccount = accountRepository.findByNumber(transferDTO.getWithdrawNumber());

        // 2. 출금 계좌 없으면 RunTimeException
        if (withdrawAccount == null) throw new RuntimeException("출금 계좌가 존재하지 않습니다");

        // 3. 입금계좌 조회
        Account depositAccount = accountRepository.findByNumber(transferDTO.getDepositNumber());

        // 4. 입금 계좌 없으면 RunTimeException
        if (depositAccount == null) throw new RuntimeException("입금 계좌가 존재하지 않습니다");

        // 5. 출금 계좌의 잔액 검사
        if (withdrawAccount.getBalance() < transferDTO.getAmount()) {
            throw new RuntimeException("출금 계좌의 잔액: " + withdrawAccount.getBalance() + ", 이체하려는 금액: " + transferDTO.getAmount());
        }

        // 6. 출금 계좌의 비밀번호 획인해서 동일인인지 체크
        if (!(withdrawAccount.getPassword().equals(transferDTO.getWithdrawPassword()))) {
            throw new RuntimeException("출금계좌 비밀번호가 틀렸습니다");
        }

        // 7. 출금 계좌의 주인과 로그인 유저가 동일 인물인지 권한 확인
        if (!(withdrawAccount.getUserId().equals(userId))) {  // Integer는 125이하까지만 값을 비교해주기 때문에 equals로 비교하는게 좋다
            throw new RuntimeException("출금계좌의 권한이 없습니다");
        }

        // 8. Account Update -> 출금계좌
        int withdrawBalance = withdrawAccount.getBalance();
        withdrawBalance = withdrawBalance - transferDTO.getAmount();
        accountRepository.updateByNumber(withdrawBalance, withdrawAccount.getPassword(), withdrawAccount.getNumber());

        // 9. Account Update -> 입금계좌
        int depositBalance = depositAccount.getBalance();
        depositBalance = depositBalance + transferDTO.getAmount();
        accountRepository.updateByNumber(depositBalance, depositAccount.getPassword(), depositAccount.getNumber());

        // 10. History save
        historyRepository.save(transferDTO.getWithdrawNumber(), transferDTO.getDepositNumber(), transferDTO.getAmount(), withdrawAccount.getBalance(), depositAccount.getBalance());  // 검증이 끝났기 때문에 transferDTO에서 가져온다
    }

    public void 계좌상세보기(int number, String type, Integer sessionUserId) {
        // 1. 계좌 존재 확인
        Account account = accountRepository.findByNumber(number);
        if (account == null) throw new RuntimeException("출금 계좌가 존재하지 않습니다");

        // 2. 계좌 주인 확인
        if (!(account.getUserId().equals(sessionUserId))) {  // Integer는 125이하까지만 값을 비교해주기 때문에 equals로 비교하는게 좋다
            throw new RuntimeException("해당 계좌의 권한이 없습니다");
        }

        // 3. 조회

    }
}
