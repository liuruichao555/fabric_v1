package com.liuruichao.sdk;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import protos.EndorserGrpc;

/**
 * EndorserClient
 *
 * @author liuruichao
 * Created on 2017/3/24 15:10
 */
public class EndorserClient {
    private final ManagedChannel channel;

    private final EndorserGrpc.EndorserBlockingStub blockingStub;

    private final EndorserGrpc.EndorserFutureStub futureStub;

    public EndorserClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = EndorserGrpc.newBlockingStub(channel);
        futureStub = EndorserGrpc.newFutureStub(channel);
    }
}
