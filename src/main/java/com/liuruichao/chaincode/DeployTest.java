//package com.liuruichao.chaincode;
//
//import org.hyperledger.fabric.sdk.*;
//import org.hyperledger.fabric.sdk.events.EventHub;
//
//import java.io.File;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.LinkedList;
//
///**
// * DeployTest
// *
// * @author liuruichao
// * Created on 2017/2/20 15:35
// */
//public class DeployTest {
//    private static final String CHAIN_CODE_NAME = "chaincode_example02.go";
//
//    private static final String CHAIN_CODE_PATH = "/Users/liuruichao/develop/opensource/golang/gopath/src/github.com/hyperledger/fabric/examples/chaincode/go/chaincode_example02";
//
//    private static final String CHAIN_NAME = "testchainid";
//
//    final static Collection<String> PEER_LOCATIONS = Collections.singletonList("grpc://localhost:7051");
//
//    final static Collection<String> ORDERER_LOCATIONS = Collections.singletonList("grpc://localhost:7050"); //Vagrant maps to this
//
//    //final static Collection<String> EVENTHUB_LOCATIONS = Collections.singletonList("grpc://localhost:7053"); //Vagrant maps to this
//
//    final static String FABRIC_CA_SERVICES_LOCATION = "http://localhost:7054";
//
//    public static void main(String[] args) throws Exception {
//        HFClient hfClient = HFClient.createNewInstance();
//        Chain newChain = hfClient.newChain(CHAIN_NAME);
//
//        for (String peerloc : PEER_LOCATIONS) {
//            Peer peer = hfClient.newPeer(peerloc);
//            peer.setName("peer1");
//            newChain.addPeer(peer);
//        }
//
//        for (String orderloc : ORDERER_LOCATIONS) {
//            Orderer orderer = hfClient.newOrderer(orderloc);
//            newChain.addOrderer(orderer);
//        }
//
//        /*for (String eventHub : EVENTHUB_LOCATIONS) {
//            EventHub orderer = hfClient.newEventHub(eventHub);
//            newChain.addEventHub(orderer);
//        }*/
//
//        hfClient.setUserContext(new User("admin"));
//
//        Chain chain = hfClient.getChain(CHAIN_NAME);
//        chain.setInvokeWaitTime(1000);
//        chain.setDeployWaitTime(12000);
//        chain.setMemberServicesUrl(FABRIC_CA_SERVICES_LOCATION, null);
//        File fileStore = new File(System.getProperty("user.home") + "/test.properties");
//        if (fileStore.exists()) {
//            fileStore.delete();
//        }
//        chain.setKeyValStore(new FileKeyValStore(fileStore.getAbsolutePath()));
//        chain.enroll("admin", "adminpw");
//        chain.initialize();
//
//        Collection<Peer> peers = chain.getPeers();
//        Collection<Orderer> orderers = chain.getOrderers();
//        System.out.println(peers);
//
//        // Deploy Proposal Request
//        /*System.out.println("Creating deployment proposal");
//        DeploymentProposalRequest deploymentProposalRequest = hfClient.newDeploymentProposalRequest();
//        deploymentProposalRequest.setChaincodeName(CHAIN_NAME);
//        //deploymentProposalRequest.setChaincodePath(CHAIN_CODE_PATH);
//        deploymentProposalRequest.setFcn("init");
//        deploymentProposalRequest.setArgs(new String[]{"a", "100", "b", "200"});
//        System.out.println("Deploying chain code with a and b set to 100 and 200 respectively");
//        Collection<ProposalResponse> responses = chain.sendDeploymentProposal(deploymentProposalRequest, peers);
//
//        // Deploy Transaction
//        Collection<ProposalResponse> successful = new LinkedList<>();
//        Collection<ProposalResponse> failed = new LinkedList<>();
//
//        for (ProposalResponse response : responses) {
//            if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
//                successful.add(response);
//            } else {
//                failed.add(response);
//            }
//        }
//        System.out.println("Received %d deployment proposal responses. Successful+verified: %d . Failed: %d\", responses.size(), successful.size(), failed.size()");
//*/
//    }
//}
