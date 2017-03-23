package com.liuruichao.peer;

import com.google.protobuf.ByteString;
import com.liuruichao.model.Member;
import com.liuruichao.security.CryptoPrimitive;
import com.liuruichao.service.MemberService;
import common.Common;
import io.grpc.ManagedChannelBuilder;
import msp.Identities;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.hyperledger.protos.Chaincode;
import org.hyperledger.protos.ChaincodeSupportGrpc;
import org.junit.Before;
import org.junit.Test;
import protos.EndorserGrpc;
import protos.ProposalOuterClass;
import protos.ProposalResponseOuterClass;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * EndorserTest
 *
 * @author liuruichao
 *         Created on 2017/3/17 16:12
 */
public class EndorserTest {
    private static CryptoPrimitive cryptoPrimitive = new CryptoPrimitive();

    private static final String chainID = "lrc1";

    private final String curveName = "P-256";

    private MemberService memberService;

    @Before
    public void before() {
        memberService = new MemberService();
    }

    @Test
    public void test() throws Exception {
        Member member = memberService.enroll("admin", "adminpw");
        ManagedChannelBuilder channelBuilder = ManagedChannelBuilder.forAddress("localhost", 7051).usePlaintext(true);
        EndorserGrpc.EndorserBlockingStub stub = EndorserGrpc.newBlockingStub(channelBuilder.build());

        Chaincode.ChaincodeID chaincodeID = Chaincode.ChaincodeID.newBuilder().setName("qscc").build();

        ProposalOuterClass.ChaincodeHeaderExtension chaincodeHeaderExtension = ProposalOuterClass.ChaincodeHeaderExtension.newBuilder()
                .setChaincodeId(chaincodeID).build();

        Common.ChannelHeader chainHeader = createChannelHeader(Common.HeaderType.ENDORSER_TRANSACTION);


        Identities.SerializedIdentity identity = Identities.SerializedIdentity.newBuilder()
                .setIdBytes(ByteString.copyFromUtf8(member.getCert()))
                .setMspid("DEFAULT").build();
        Common.SignatureHeader sigHeader = Common.SignatureHeader.newBuilder()
                .setCreator(identity.toByteString())
                .setNonce(ByteString.copyFromUtf8(UUID.randomUUID().toString())).build();

        List<ByteString> allArgs = new ArrayList<>();
        Chaincode.ChaincodeInput chaincodeInput = Chaincode.ChaincodeInput.newBuilder().addAllArgs(allArgs).build();

        Chaincode.ChaincodeSpec chaincodeSpec = Chaincode.ChaincodeSpec.newBuilder()
                .setType(Chaincode.ChaincodeSpec.Type.GOLANG)
                .setChaincodeId(chaincodeID)
                .setInput(chaincodeInput)
                .build();

        Chaincode.ChaincodeInvocationSpec chaincodeInvocationSpec = Chaincode.ChaincodeInvocationSpec.newBuilder()
                .setChaincodeSpec(chaincodeSpec).build();

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
        ProposalResponseOuterClass.ProposalResponse response = stub.processProposal(getSignedProposal(member.getPrivateKey(), proposal));
        System.out.println(response.getResponse().getMessage());
    }

    private static Common.ChannelHeader createChannelHeader(Common.HeaderType type) {
        Common.ChannelHeader.Builder ret = Common.ChannelHeader.newBuilder()
                .setType(type.getNumber())
                .setVersion(1)
                //.setTxId(txID)
                .setChannelId(chainID);
                //.setEpoch(epoch);
        /*if (null != chaincodeHeaderExtension) {
            ret.setExtension(chaincodeHeaderExtension.toByteString());
        }*/

        return ret.build();
    }

    private static ProposalOuterClass.SignedProposal getSignedProposal(PrivateKey privateKey, protos.ProposalOuterClass.Proposal proposal) throws Exception {
        byte[] ecdsaSignature = cryptoPrimitive.ecdsaSignToBytes(privateKey, proposal.toByteArray());
        ProposalOuterClass.SignedProposal.Builder signedProposal = ProposalOuterClass.SignedProposal.newBuilder();


        signedProposal.setProposalBytes(proposal.toByteString());

        signedProposal.setSignature(ByteString.copyFrom(ecdsaSignature));
        return signedProposal.build();
    }

    public byte[] ecdsaSignToBytes(PrivateKey privateKey, byte[] data) throws Exception {
        byte[] encoded = data;
        encoded = hash(data);

        // char[] hexenncoded = Hex.encodeHex(encoded);
        // encoded = new String(hexenncoded).getBytes();

        X9ECParameters params = NISTNamedCurves.getByName(this.curveName);
        BigInteger curve_N = params.getN();

        ECDomainParameters ecParams = new ECDomainParameters(params.getCurve(), params.getG(), curve_N,
                params.getH());

        ECDSASigner signer = new ECDSASigner();

        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(((ECPrivateKey) privateKey).getS(), ecParams);
        signer.init(true, privKey);
        BigInteger[] sigs = signer.generateSignature(encoded);

        sigs = preventMalleability(sigs, curve_N);

        ByteArrayOutputStream s = new ByteArrayOutputStream();

        DERSequenceGenerator seq = new DERSequenceGenerator(s);
        seq.addObject(new ASN1Integer(sigs[0]));
        seq.addObject(new ASN1Integer(sigs[1]));
        seq.close();
        byte[] ret = s.toByteArray();
        return ret;
    }

    private byte[] hash(byte[] input) {
        Digest digest = getHashDigest();
        byte[] retValue = new byte[digest.getDigestSize()];
        digest.update(input, 0, input.length);
        digest.doFinal(retValue, 0);
        return retValue;
    }

    private Digest getHashDigest() {
        return new SHA256Digest();
    }

    private BigInteger[] preventMalleability(BigInteger[] sigs, BigInteger curve_n) {
        BigInteger cmpVal = curve_n.divide(BigInteger.valueOf(2l));

        BigInteger sval = sigs[1];

        if (sval.compareTo(cmpVal) == 1) {

            sigs[1] = curve_n.subtract(sval);
        }

        return sigs;
    }
}
