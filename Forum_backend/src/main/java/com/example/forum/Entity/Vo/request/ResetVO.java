package com.example.forum.Entity.Vo.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class ResetVO {

    @Email
    String email;

    String password;
    String repeatPassword;

    @Length(min=6,max=6)
    String code;
}
