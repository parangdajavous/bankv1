package com.metacoding.bankv1.account;

import com.metacoding.bankv1.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AccountController {

    private final AccountService accountService;
    private final HttpSession session;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/account/save-form")
    public String saveForm() {
        // 인증체크 (반복되는 부가로직)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");

        return "account/save-form";
    }

    @PostMapping("/account/save")
    public String save(AccountRequest.SaveDTO saveDTO) {
        // 인증체크 (반복되는 부가로직)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");

        accountService.계좌생성(saveDTO, sessionUser.getId());
        return "redirect:/account";
    }

    @GetMapping("/account")
    public String list(HttpServletRequest request) {
        // 인증체크 (반복되는 부가로직)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");

        List<Account> accountList = accountService.나의계좌목록(sessionUser.getId());
        request.setAttribute("models", accountList);

        return "account/list";
    }

    @GetMapping("/account/transfer-form")
    public String transferForm() {
        // 인증체크 (반복되는 공통부가로직)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");

        return "account/transfer-form";
    }

    @PostMapping("/account/transfer")
    public String transfer(AccountRequest.TransferDTO transferDTO) {
        // 인증체크 (반복되는 공통부가로직)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");

        accountService.계좌이체(transferDTO, sessionUser.getId());
        return "redirect:/";  // TODO
    }

    // /account/1111?type=전체
    @GetMapping("/account/{number}")
    public String detail(@PathVariable("number") int number, @RequestParam(value = "type", required = false, defaultValue = "전체") String type, HttpServletRequest request) {
//        System.out.println("number = " + number);
//        System.out.println("type = " + type);

        // 인증체크 (반복되는 공통부가로직)
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");

        List<AccountResponse.DetailDTO> datailList = accountService.계좌상세보기(number, type, sessionUser.getId());  // sessionUser.getId()로 권한체크 필요
        request.setAttribute("models", datailList);
        return "account/detail";
    }
}
