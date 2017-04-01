package com.liuruichao.service;

import com.liuruichao.model.Member;
import com.liuruichao.sdk.Chain;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.protos.Chaincode;
import org.junit.Test;

/**
 * OrdererService
 *
 * @author liuruichao
 *         Created on 2017/3/24 14:27
 */
@Slf4j
public class OrdererService {
    private static final String CHAIN_CODE_NAME = "example_cc.go";
    private static final String CHAIN_CODE_PATH = "github.com/example_cc";
    private static final String CHAIN_CODE_VERSION = "1.0";

    private MemberService memberService = new MemberService();

    @Test
    public void testCreateChain() throws Exception {
        String name = "foo";
        String configtxPath = "/Users/liuruichao/javaSRC/oxchains/fabric_v1/src/main/resources/foo.tx";
        Member member = memberService.enroll("admin", "adminpw");
        Chain chain = new Chain(name, "localhost", 7050, configtxPath, member);
        System.out.println(chain.getGenesisBlock());
        Chaincode.ChaincodeID chaincodeID = Chaincode.ChaincodeID.newBuilder()
                .setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION)
                .setPath(CHAIN_CODE_PATH).build();

    }

    private void genInstallProposalRequest() {
        // TODO 生成安装chaincode请求
    }
}
