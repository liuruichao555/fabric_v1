package com.liuruichao.service;

import com.liuruichao.security.CryptoPrimitive;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * ChaincodeService
 *
 * @author liuruichao
 * Created on 2017/3/23 15:13
 */
@Slf4j
public class ChaincodeService {
    private CryptoPrimitive cryptoPrimitive;

    private static final String chainID = "lrc1";

    private final String curveName = "P-256";

    @Before
    public void before() {
        cryptoPrimitive = new CryptoPrimitive();
    }

    @Test
    public void testInstall() {

    }
}