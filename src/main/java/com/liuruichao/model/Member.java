package com.liuruichao.model;

import lombok.Data;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Member
 *
 * @author liuruichao
 * Created on 2017/3/23 16:50
 */
@Data
public class Member {
    private PrivateKey privateKey;

    private PublicKey publicKey;

    private String cert;

    private String mspID;
}
