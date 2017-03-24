package com.liuruichao.service;

import com.google.protobuf.ByteString;
import com.liuruichao.model.Member;
import com.liuruichao.security.CryptoPrimitive;
import com.liuruichao.utils.ProtoUtils;
import common.Common;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import msp.Identities;
import orderer.Ab;
import orderer.AtomicBroadcastGrpc;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static orderer.Ab.DeliverResponse.TypeCase.STATUS;

/**
 * OrdererService
 *
 * @author liuruichao
 *         Created on 2017/3/24 14:27
 */
@Slf4j
public class OrdererService {
    private String lrcBlockPath = "/Users/liuruichao/javaSRC/oxchains/fabric_v1/src/main/resources/foo.configtx";

    private ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 7050).usePlaintext(true).build();

    private MemberService memberService = new MemberService();

    // TODO chain name(channel name)
    private String name = "foo";

    private CryptoPrimitive cryptoPrimitive = new CryptoPrimitive();

    @Test
    public void testCreateChain() throws Exception {
        try {
            InputStream is = new FileInputStream(lrcBlockPath);
            byte[] configBytes = IOUtils.toByteArray(is);
            Common.Envelope envelope = Common.Envelope.parseFrom(configBytes);
            Ab.BroadcastResponse response = sendTransaction(envelope);
            System.out.println(response.getStatusValue());
            Common.Block genesisBlock = getGenesisBlock(memberService.enroll("admin", "adminpw"));
            System.out.println("genesisBlock: " + genesisBlock);
        } finally {
            shutdown();
        }
    }

    private Ab.BroadcastResponse sendTransaction(Common.Envelope envelope) throws Exception {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        AtomicBroadcastGrpc.AtomicBroadcastStub broadcast = AtomicBroadcastGrpc.newStub(channel);
            /*AtomicBroadcastGrpc.AtomicBroadcastBlockingStub bsc = AtomicBroadcastGrpc.newBlockingStub(channel);
            bsc.withDeadlineAfter(2, TimeUnit.MINUTES);*/

        final Ab.BroadcastResponse[] ret = new Ab.BroadcastResponse[1];
        final Throwable[] throwable = new Throwable[]{null};

        StreamObserver<Common.Envelope> nso = broadcast.broadcast(new StreamObserver<Ab.BroadcastResponse>() {
            @Override
            public void onNext(Ab.BroadcastResponse resp) {
                // log.info("Got Broadcast response: " + resp);
                log.debug("resp status value: " + resp.getStatusValue() + ", resp: " + resp.getStatus());
                ret[0] = resp;
                finishLatch.countDown();

            }

            @Override
            public void onError(Throwable t) {
                throwable[0] = t;
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                log.debug("onCompleted");
                finishLatch.countDown();
            }
        });

        nso.onNext(envelope);
        //nso.onCompleted();
        finishLatch.await(2, TimeUnit.MINUTES);
        return ret[0];
    }

    private Common.Block getGenesisBlock(Member member) {
        Common.Block genesisBlock = null;
        try {
            final long start = System.currentTimeMillis();

            do {

                Ab.SeekSpecified seekSpecified = Ab.SeekSpecified.newBuilder()
                        .setNumber(0)
                        .build();
                Ab.SeekPosition seekPosition = Ab.SeekPosition.newBuilder()
                        .setSpecified(seekSpecified)
                        .build();

                Ab.SeekSpecified seekStopSpecified = Ab.SeekSpecified.newBuilder()
                        .setNumber(0)
                        .build();

                Ab.SeekPosition seekStopPosition = Ab.SeekPosition.newBuilder()
                        .setSpecified(seekStopSpecified)
                        .build();

                Ab.SeekInfo seekInfo = Ab.SeekInfo.newBuilder()
                        .setStart(seekPosition)
                        .setStop(seekStopPosition)
                        .setBehavior(Ab.SeekInfo.SeekBehavior.BLOCK_UNTIL_READY)
                        .build();

                Common.ChannelHeader deliverChainHeader = ProtoUtils.
                        createChannelHeader(Common.HeaderType.DELIVER_SEEK_INFO, "4", name, 0, null);


                String mspid = member.getMspID();
                String cert = member.getCert();

                Identities.SerializedIdentity identity = Identities.SerializedIdentity.newBuilder()
                        .setIdBytes(ByteString.copyFromUtf8(cert)).
                                setMspid(mspid).build();


                Common.SignatureHeader deliverSignatureHeader = Common.SignatureHeader.newBuilder()
                        .setCreator(identity.toByteString())
                        .setNonce(ProtoUtils.getNonce())
                        .build();

                Common.Header deliverHeader = Common.Header.newBuilder()
                        .setSignatureHeader(deliverSignatureHeader.toByteString())
                        .setChannelHeader(deliverChainHeader.toByteString())
                        .build();

                Common.Payload deliverPayload = Common.Payload.newBuilder()
                        .setHeader(deliverHeader)
                        .setData(seekInfo.toByteString())
                        .build();

                byte[] deliverPayload_bytes = deliverPayload.toByteArray();

                byte[] deliver_signature = cryptoPrimitive.ecdsaSignToBytes(member.getPrivateKey(), deliverPayload_bytes);

                Common.Envelope deliverEnvelope = Common.Envelope.newBuilder()
                        .setSignature(ByteString.copyFrom(deliver_signature))
                        .setPayload(ByteString.copyFrom(deliverPayload_bytes))
                        .build();

                Ab.DeliverResponse[] deliver = sendDeliver(deliverEnvelope);
                if (deliver.length < 1) {
                    log.warn("Genesis block for channel {} fetch bad deliver missing status block only got blocks:{}", name, deliver.length);
                    //odd so lets try again....
                } else {

                    Ab.DeliverResponse status = deliver[0];
                    if (status.getStatusValue() == 404) {
                        log.warn("Bad deliver expected status 200  got  {}, Chain {}", status.getStatusValue(), name);
                        // keep trying...
                    } else if (status.getStatusValue() != 200) {
                        throw new RuntimeException(format("Bad deliver expected status 200  got  %d, Chain %s", status.getStatusValue(), name));

                    } else {

                        if (deliver.length < 2) {
                            log.warn("Genesis block for channel {} fetch bad deliver missing genesis block only got {}:", name, deliver.length);
                            //odd try again
                        } else {

                            Ab.DeliverResponse blockresp = deliver[1];
                            genesisBlock = blockresp.getBlock();

                        }
                    }
                }

                    /*if (genesisBlock == null) {
                        long now = System.currentTimeMillis();

                        long duration = now - start;

                        if (duration > config.getGenesisBlockWaitTime()) {
                            throw new TransactionException(format("Getting genesis block time exceeded %s seconds for chain %s", Long.toString(TimeUnit.MILLISECONDS.toSeconds(duration)), name));
                        }
                        try {
                            Thread.sleep(200);//try again
                        } catch (InterruptedException e) {
                            TransactionException te = new TransactionException("getGenesisBlock thread Sleep", e);
                            logger.warn(te.getMessage(), te);
                        }
                    }*/
            } while (genesisBlock == null);
        } catch (Exception e) {
            log.error("getGenesisBlock is error!", e);
        }
        return genesisBlock;
    }

    public Ab.DeliverResponse[] sendDeliver(Common.Envelope envelope) throws Exception {

        final CountDownLatch finishLatch = new CountDownLatch(1);
        AtomicBroadcastGrpc.AtomicBroadcastStub broadcast = AtomicBroadcastGrpc.newStub(channel);
        AtomicBroadcastGrpc.AtomicBroadcastBlockingStub bsc = AtomicBroadcastGrpc.newBlockingStub(channel);
        bsc.withDeadlineAfter(2, TimeUnit.MINUTES);

        // final DeliverResponse[] ret = new DeliverResponse[1];
        final List<Ab.DeliverResponse> retList = new ArrayList<>();
        final List<Throwable> throwableList = new ArrayList<>();
        //   ret[0] = null;

        StreamObserver<Ab.DeliverResponse> so = new StreamObserver<Ab.DeliverResponse>() {
            boolean done = false;

            @Override
            public void onNext(Ab.DeliverResponse resp) {

                // logger.info("Got Broadcast response: " + resp);
                log.debug("resp status value: " + resp.getStatusValue() + ", resp: " + resp.getStatus() + ", type case" + resp.getTypeCase());

                if (done) {
                    return;
                }

                if (resp.getTypeCase() == STATUS) {
                    done = true;
                    retList.add(0, resp);

                    finishLatch.countDown();

                } else {
                    retList.add(resp);
                }

            }

            @Override
            public void onError(Throwable t) {
                log.error("broadcase error " + t);
                throwableList.add(t);
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                log.debug("onCompleted");
                finishLatch.countDown();
            }
        };

        StreamObserver<Common.Envelope> nso = broadcast.deliver(so);
        nso.onNext(envelope);
        //nso.onCompleted();

        try {
            boolean res = finishLatch.await(2, TimeUnit.MINUTES);
            log.debug("Done waiting for reply! Got:" + retList);

        } catch (InterruptedException e) {
            log.error("sendDeliver is error!", e);
        }

        if(!throwableList.isEmpty()){
            Throwable throwable = throwableList.get(0);
            RuntimeException e = new RuntimeException(throwable.getMessage(), throwable);
            log.error(e.getMessage(), e);
            throw e;
        }

        return retList.toArray(new Ab.DeliverResponse[retList.size()]);
    }

    private void shutdown() {
        if (!channel.isShutdown()) {
            channel.shutdownNow();
        }
    }
}
