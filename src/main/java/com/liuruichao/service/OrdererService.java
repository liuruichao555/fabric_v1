package com.liuruichao.service;

import common.Common;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import orderer.Ab;
import orderer.AtomicBroadcastGrpc;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

    @Test
    public void testCreateChain() throws Exception {
        InputStream is = new FileInputStream(lrcBlockPath);
        byte[] configBytes = IOUtils.toByteArray(is);
        Common.Envelope envelope = Common.Envelope.parseFrom(configBytes);
        Ab.BroadcastResponse response = sendTransaction(envelope);
        System.out.println(response.getStatusValue());
    }

    private Ab.BroadcastResponse sendTransaction(Common.Envelope envelope) throws Exception {
        try {
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
        } finally {
            shutdown();
        }
    }

    private Common.Block getGenesisBlock() {

        return null;
    }

    private void shutdown() {
        if (!channel.isShutdown()) {
            channel.shutdownNow();
        }
    }
}
