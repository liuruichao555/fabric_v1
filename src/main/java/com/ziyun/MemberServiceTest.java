package com.ziyun;

import com.ziyun.model.Customer;
import com.ziyun.util.CryptoUtils;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * MemberServiceTest
 *
 * @author liuruichao
 * Created on 2017/3/29 21:54
 */
public class MemberServiceTest {
    public static void main(String[] args) throws Exception {
        String username = "admin";
        ArrayList<String> roles = new ArrayList<>();
        String account = "ziyun";
        String affiliation = "affiliation";
        String mspID = "Org0MSP";
        //String mspID = "ziyun.MSP";

        MemberServices memberServices = new HFCAClient("http://localhost:7054", null);
        memberServices.setCryptoSuite(CryptoUtils.createCryptoSuite());
        Enrollment enrollment = memberServices.enroll("admin", "adminpw");

        Customer customer = new Customer(username, enrollment, roles, account, affiliation, mspID);

        HFClient hfClient = HFClient.createNewInstance();
        hfClient.setCryptoSuite(CryptoUtils.createCryptoSuite());
        hfClient.setMemberServices(memberServices);
        hfClient.setUserContext(customer);

        Properties orderProperties = new Properties();
        /*String pemPath = "/Users/liuruichao/develop/opensource/golang/gopath/src/github.com/hyperledger/fabric/examples/e2e_cli/crypto/orderer/localMspConfig/cacerts/ordererOrg0.pem";
        orderProperties.setProperty("pemFile", pemPath);
        orderProperties.setProperty("trustServerCertificate", "true");*/

        Orderer orderer = hfClient.newOrderer("mychannel", "grpc://localhost:7050", orderProperties);
        Peer peer = hfClient.newPeer("peer0", "grpc://localhost:7051");
        Chain chain = hfClient.newChain("mychannel");
        chain.addOrderer(orderer);
        chain.joinPeer(peer);
        chain.initialize();
        Collection<Peer> peers = chain.getPeers();
        System.out.println(peers);
    }
}
