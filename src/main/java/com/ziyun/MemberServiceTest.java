package com.ziyun;

import com.ziyun.model.Customer;
import com.ziyun.util.CryptoUtils;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * MemberServiceTest
 *
 * @author liuruichao
 * Created on 2017/3/29 21:54
 */
public class MemberServiceTest {
    private static final String CHAIN_CODE_NAME = "example_cc.go";

    private static final String CHAIN_CODE_PATH = "github.com/example_cc";

    private static final String CHAIN_CODE_VERSION = "1.0";

    public static void main(String[] args) throws Exception {
        String username = "admin";
        String password = "adminpw";
        ArrayList<String> roles = new ArrayList<>();
        String account = null;
        String affiliation = "peerOrg1";
        String mspID = "Org1MSP";
        //String mspID = "ziyun.MSP";
        String configPath = "/Users/liuruichao/javaSRC/oxchains/fabric_v1/src/main/resources/foo.tx";
        String ordererName = "orderer0";
        String chainName = "foo";

        MemberServices memberServices = new HFCAClient("http://localhost:7054", null);
        memberServices.setCryptoSuite(CryptoUtils.createCryptoSuite());
        Enrollment enrollment = memberServices.enroll(username, password);

        Customer customer = new Customer(username, enrollment, roles, account, affiliation, mspID);

        HFClient hfClient = HFClient.createNewInstance();
        hfClient.setCryptoSuite(CryptoUtils.createCryptoSuite());
        hfClient.setMemberServices(memberServices);
        hfClient.setUserContext(customer);

        Properties orderProperties = new Properties();
        /*String pemPath = "/Users/liuruichao/develop/opensource/golang/gopath/src/github.com/hyperledger/fabric/examples/e2e_cli/crypto/orderer/localMspConfig/cacerts/ordererOrg0.pem";
        orderProperties.setProperty("pemFile", pemPath);
        orderProperties.setProperty("trustServerCertificate", "true");*/

        Orderer orderer = hfClient.newOrderer(ordererName, "grpc://localhost:7050", null);
        /*Peer peer = hfClient.newPeer("peer0", "grpc://localhost:7051");
        Peer peer1 = hfClient.newPeer("peer1", "grpc://localhost:7056");
        Chain chain = hfClient.newChain("foo");
        chain.addOrderer(orderer);
        chain.addPeer(peer);
        chain.addPeer(peer1);
        chain.initialize();*/

        //chain.addPeer(peer);
        //chain.initialize();
        //Collection<Peer> peers = chain.getPeers();
        //System.out.println(peers);

        Chain chain = createChain(hfClient, configPath, orderer, chainName);

        //invoke(hfClient, chain);

        //query(hfClient, chain);
    }

    private static void installChaincode() {

    }

    private static Chain createChain(HFClient hfClient, String configPath, Orderer orderer, String chainName) throws IOException, InvalidArgumentException, TransactionException, ProposalException {
        ChainConfiguration chainConfiguration = new ChainConfiguration(new File(configPath));

        Chain newChain = hfClient.newChain(chainName, orderer, chainConfiguration);
        Peer peer0 = hfClient.newPeer("peer0", "grpc://localhost:7051");
        Peer peer1 = hfClient.newPeer("peer1", "grpc://localhost:7056");
        newChain.joinPeer(peer0);
        newChain.joinPeer(peer1);
        newChain.initialize();
        return newChain;
    }

    private static void invoke(HFClient hfClient, Chain chain) throws InvalidArgumentException, ProposalException, InterruptedException, ExecutionException, TimeoutException {
        ChainCodeID chainCodeID = ChainCodeID.newBuilder().setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION).build();
        InvokeProposalRequest invokeProposalRequest = hfClient.newInvokeProposalRequest();
        invokeProposalRequest.setChaincodeID(chainCodeID);
        invokeProposalRequest.setFcn("invoke");
        invokeProposalRequest.setArgs(new String[]{"move", "a", "b", "100"});
        Collection<ProposalResponse> invokeProposals = chain.sendInvokeProposal(invokeProposalRequest, chain.getPeers());
        BlockEvent.TransactionEvent completableFuture = chain.sendTransaction(invokeProposals, chain.getOrderers()).get(120, TimeUnit.SECONDS);
        System.out.println("TransactionID: " + completableFuture.getTransactionID());
    }

    private static void query(HFClient hfClient, Chain chain) throws ProposalException, InvalidArgumentException {
        ChainCodeID chainCodeID = ChainCodeID.newBuilder().setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION).build();
        // .setPath(CHAIN_CODE_PATH).build(); // 查询不需要path
        QueryProposalRequest queryProposalRequest = hfClient.newQueryProposalRequest();
        queryProposalRequest.setArgs(new String[]{"query", "b"});
        queryProposalRequest.setFcn("invoke");
        queryProposalRequest.setChaincodeID(chainCodeID);

        Collection<ProposalResponse> queryProposals = chain.sendQueryProposal(queryProposalRequest, chain.getPeers());
        for (ProposalResponse response : queryProposals) {
            System.out.println(response.getProposalResponse().getResponse().getPayload().toStringUtf8());
        }
    }
}
