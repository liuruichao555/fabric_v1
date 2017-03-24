package com.liuruichao.service;

import com.liuruichao.model.Member;
import com.liuruichao.sdk.Chain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * OrdererService
 *
 * @author liuruichao
 *         Created on 2017/3/24 14:27
 */
@Slf4j
public class OrdererService {
    private MemberService memberService = new MemberService();

    @Test
    public void testCreateChain() throws Exception {
        String name = "foo";
        String configtxPath = "/Users/liuruichao/javaSRC/oxchains/fabric_v1/src/main/resources/foo.configtx";
        Member member = memberService.enroll("admin", "adminpw");
        Chain chain = new Chain(name, "localhost", 7050, configtxPath, member);
        System.out.println(chain.getGenesisBlock());
    }
}
