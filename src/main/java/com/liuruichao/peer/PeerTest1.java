package com.liuruichao.peer;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.Assert;
import org.junit.Test;
import protos.AdminGrpc;
import protos.EndorserGrpc;
import protos.ProposalOuterClass;
import protos.ProposalResponseOuterClass;

/**
 * PeerTest1
 *
 * @author liuruichao
 * Created on 2017/2/27 10:52
 */
public class PeerTest1 {
    ManagedChannelBuilder channelBuilder = ManagedChannelBuilder.forAddress("localhost", 7051).usePlaintext(true);

    @Test
    public void test1() {
        //AdminGrpc.AdminBlockingStub stub = AdminGrpc.newBlockingStub(channelBuilder.build());
        //stub.stopServer(Empty.getDefaultInstance());
        //stub.startServer(Empty.getDefaultInstance());
        //System.out.println(stub.getStatus(Empty.getDefaultInstance()));
        EndorserGrpc.EndorserBlockingStub stub = EndorserGrpc.newBlockingStub(channelBuilder.build());
        ProposalResponseOuterClass.ProposalResponse response = stub.processProposal(createRequest());
        System.out.println(response.getResponse().getMessage());

    }

    private ProposalOuterClass.SignedProposal createRequest() {
        /*byte[] ecdsaSignature = cryptoSuite.sign(getEnrollment().getKey(), proposal.toByteArray());
        SignedProposal.Builder signedProposal = SignedProposal.newBuilder();


        signedProposal.setProposalBytes(proposal.toByteString());

        signedProposal.setSignature(ByteString.copyFrom(ecdsaSignature));
        return signedProposal.build();*/
        return null;
    }
}
