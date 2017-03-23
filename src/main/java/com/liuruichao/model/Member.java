package com.liuruichao.model;

import lombok.Data;

import java.security.PrivateKey;

/**
 * Member
 *
 * @author liuruichao
 * Created on 2017/3/23 16:50
 */
@Data
public class Member {
    private PrivateKey privateKey;

    private String cert;
}
