package com.metacoding.bankv1.account;

import com.metacoding.bankv1.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
        return "redirect:/";
    }


}
