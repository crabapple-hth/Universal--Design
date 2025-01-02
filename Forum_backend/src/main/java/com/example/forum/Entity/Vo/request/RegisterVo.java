package com.example.forum.Entity.Vo.request;


import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterVo {
    @Email
    String email;
    @Length(min=3,max=20)
    String username;
    @Length(min=6,max=20)
    String password;
    @Length(min=6,max=6)
    String code;
}
