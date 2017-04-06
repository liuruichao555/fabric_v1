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
import java.util.*;
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
    private static final String TEST_FIXTURES_PATH = "/Users/liuruichao/develop/opensource/golang/gopath/src/github.com/hyperledger/fabric-sdk-java/src/test/fixture";

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

        /*Properties orderProperties = new Properties();
        String pemPath = "/Users/liuruichao/develop/opensource/golang/gopath/src/github.com/hyperledger/fabric/examples/e2e_cli/crypto/orderer/localMspConfig/cacerts/ordererOrg0.pem";
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

        //Chain chain = createChain(hfClient, configPath, orderer, chainName);

        Chain chain = getChain(hfClient, chainName, orderer);

        //installChaincode(chain, hfClient);

        //instantiateChaincode(chain, hfClient);

        //invoke(hfClient, chain);

        query(hfClient, chain);
    }

    private static void installChaincode(Chain chain, HFClient hfClient) throws InvalidArgumentException, ProposalException {
        ChainCodeID chainCodeID = ChainCodeID.newBuilder().setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION)
                .setPath(CHAIN_CODE_PATH).build();
        InstallProposalRequest installProposalRequest = hfClient.newInstallProposalRequest();
        installProposalRequest.setChaincodeID(chainCodeID);

        installProposalRequest.setChaincodeSourceLocation(new File(TEST_FIXTURES_PATH + "/sdkintegration/gocc/sample1"));
        installProposalRequest.setChaincodeVersion(CHAIN_CODE_VERSION);

        Collection<Peer> peersFromOrg = chain.getPeers();
        Collection<ProposalResponse> responses = chain.sendInstallProposal(installProposalRequest, peersFromOrg);
        for (ProposalResponse response : responses) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                System.out.println(String.format("Successful install proposal response Txid: %s from peer %s",
                        response.getTransactionID(),
                        response.getPeer().getName()));
            } else {
                System.out.println("install chaincode error!");
            }
        }
    }

    public static void instantiateChaincode(Chain chain, HFClient hfClient) throws IOException, ProposalException, InvalidArgumentException, InterruptedException, ExecutionException, TimeoutException {
        ChainCodeID chainCodeID = ChainCodeID.newBuilder().setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION)
                .setPath(CHAIN_CODE_PATH).build();
        InstantiateProposalRequest instantiateProposalRequest = hfClient.newInstantiationProposalRequest();
        instantiateProposalRequest.setChaincodeID(chainCodeID);
        instantiateProposalRequest.setFcn("init");
        instantiateProposalRequest.setArgs(new String[]{"a", "500", "b", "" + 200});

            /*
              policy OR(Org1MSP.member, Org2MSP.member) meaning 1 signature from someone in either Org1 or Org2
              See README.md Chaincode endorsement policies section for more details.
            */
        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy(new File(TEST_FIXTURES_PATH + "/sdkintegration/e2e-2Orgs/channel/members_from_org1_or_2.policy"));
        instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);

        Collection<ProposalResponse> successful = new ArrayList<>();
        // Send instantiate transaction to peers
        Collection<ProposalResponse> responses = chain.sendInstantiationProposal(instantiateProposalRequest, chain.getPeers());
        for (ProposalResponse response : responses) {
            if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
                successful.add(response);
                System.out.println(String.format("Succesful instantiate proposal response Txid: %s from peer %s",
                        response.getTransactionID(),
                        response.getPeer().getName()));
            } else {
                System.out.println("Instantiate Chaincode error! " + response.getMessage());
            }
        }

        /// Send instantiate transaction to orderer
        chain.sendTransaction(successful, chain.getOrderers()).get(120, TimeUnit.SECONDS);
        System.out.println("instantiateChaincode done");
    }

    private static Chain getChain(HFClient hfClient, String chainName, Orderer orderer) throws InvalidArgumentException, TransactionException {
        Chain chain = hfClient.newChain(chainName);

        Set<Peer> peers = getPeers(hfClient);
        for (Peer peer : peers) {
            chain.addPeer(peer);
        }

        chain.addOrderer(orderer);
        chain.initialize();
        return chain;
    }

    private static Chain createChain(HFClient hfClient, String configPath, Orderer orderer, String chainName) throws IOException, InvalidArgumentException, TransactionException, ProposalException {
        ChainConfiguration chainConfiguration = new ChainConfiguration(new File(configPath));
        Chain newChain = hfClient.newChain(chainName, orderer, chainConfiguration);

        Set<Peer> peers = getPeers(hfClient);
        for (Peer peer : peers) {
            newChain.joinPeer(peer);
        }

        newChain.initialize();
        return newChain;
    }

    private static Set<Peer> getPeers(HFClient hfClient) throws InvalidArgumentException {
        Set<Peer> peers = new HashSet<>();
        // TODO 可以配置
        peers.add(hfClient.newPeer("peer0", "grpc://localhost:7051"));
        peers.add(hfClient.newPeer("peer1", "grpc://localhost:7056"));
        return peers;
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
