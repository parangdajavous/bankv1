package com.metacoding.bankv1.user;

import lombok.Data;

public class UserRequest {

    @Data  //getter,setter,toString
    public static class JoinDTO {
        private String username;
        private String password;
        private String fullname;

    }

    @Data //getter,setter,toString
    public static class LoginDTO {
        private String username;
        private String password;

    }
}
