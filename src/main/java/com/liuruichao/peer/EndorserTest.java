package com.liuruichao.peer;

import com.google.protobuf.ByteString;
import common.Common;
import io.grpc.ManagedChannelBuilder;
import org.hyperledger.protos.Chaincode;
import org.hyperledger.protos.ChaincodeSupportGrpc;
import protos.EndorserGrpc;
import protos.ProposalOuterClass;

/**
 * EndorserTest
 *
 * @author liuruichao
 *         Created on 2017/3/17 16:12
 */
public class EndorserTest {
    public static void main(String[] args) {
        ManagedChannelBuilder channelBuilder = ManagedChannelBuilder.forAddress("localhost", 7051).usePlaintext(true);
        EndorserGrpc.EndorserBlockingStub stub = EndorserGrpc.newBlockingStub(channelBuilder.build());

        Chaincode.ChaincodeID chaincodeID = Chaincode.ChaincodeID.newBuilder().setName("qscc").build();

        ProposalOuterClass.ChaincodeHeaderExtension chaincodeHeaderExtension = ProposalOuterClass.ChaincodeHeaderExtension.newBuilder()
                .setChaincodeId(chaincodeID).build();

        String chainID = "testchainid";
        Common.ChannelHeader chainHeader = createChannelHeader(Common.HeaderType.ENDORSER_TRANSACTION);

        Common.SignatureHeader sigHeader = Common.SignatureHeader.newBuilder().build();
                //.setCreator(context.getIdentity().toByteString())
                //.setNonce(context.getNonce()).build();

        Chaincode.ChaincodeInvocationSpec chaincodeInvocationSpec = null;
        /*Chaincode.ChaincodeInvocationSpec chaincodeInvocationSpec = createChaincodeInvocationSpec(
                chaincodeID,
                ccType);*/

        ProposalOuterClass.ChaincodeProposalPayload payload = ProposalOuterClass.ChaincodeProposalPayload.newBuilder()
                .setInput(chaincodeInvocationSpec.toByteString())
                .build();

        Common.Header header = Common.Header.newBuilder()
                .setSignatureHeader(sigHeader.toByteString())
                .setChannelHeader(chainHeader.toByteString())
                .build();

        protos.ProposalOuterClass.Proposal proposal = ProposalOuterClass.Proposal.newBuilder()
                .setHeader(header.toByteString())
                .setPayload(payload.toByteString())
                .build();
        stub.processProposal(getSignedProposal(proposal));
    }

    private static Common.ChannelHeader createChannelHeader(Common.HeaderType type) {
        Common.ChannelHeader.Builder ret = Common.ChannelHeader.newBuilder()
                .setType(type.getNumber())
                .setVersion(1)
                //.setTxId(txID)
                .setChannelId("testchainid");
                //.setEpoch(epoch);
        /*if (null != chaincodeHeaderExtension) {
            ret.setExtension(chaincodeHeaderExtension.toByteString());
        }*/

        return ret.build();
    }

    private static ProposalOuterClass.SignedProposal getSignedProposal(protos.ProposalOuterClass.Proposal proposal) {
        /*byte[] ecdsaSignature = cryptoSuite.sign(getEnrollment().getKey(), proposal.toByteArray());
        ProposalOuterClass.SignedProposal.Builder signedProposal = ProposalOuterClass.SignedProposal.newBuilder();


        signedProposal.setProposalBytes(proposal.toByteString());

        signedProposal.setSignature(ByteString.copyFrom(ecdsaSignature));
        return signedProposal.build();*/
        return null;
    }
}
