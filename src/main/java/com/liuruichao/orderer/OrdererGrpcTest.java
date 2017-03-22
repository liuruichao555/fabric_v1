package com.liuruichao.orderer;

import com.google.protobuf.Empty;
import common.Ledger;
import gossip.GossipGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import orderer.Ab;
import orderer.AtomicBroadcastGrpc;
import org.hyperledger.protos.ChaincodeSupportGrpc;
import org.junit.Before;
import org.junit.Test;
import protos.AdminGrpc;
import protos.EndorserGrpc;
import protos.EventsOuterClass;

/**
 * OrdererGrpcTest
 *
 * @author liuruichao
 * Created on 2017/2/27 10:54
 */
public class OrdererGrpcTest {
    private AtomicBroadcastGrpc.AtomicBroadcastBlockingStub atomicBroadcastBlockingStub;
    ManagedChannelBuilder channelBuilder = ManagedChannelBuilder.forAddress("localhost", 7051).usePlaintext(true);

    @Test
    public void before() {
        atomicBroadcastBlockingStub = AtomicBroadcastGrpc.newBlockingStub(channelBuilder.build());
        /*AtomicBroadcastGrpc.AtomicBroadcastStub stub = AtomicBroadcastGrpc.newStub(channelBuilder.build());
        AdminGrpc.AdminBlockingStub adminBlockingStub = AdminGrpc.newBlockingStub(channelBuilder.build());
        System.out.println(adminBlockingStub.getStatus(Empty.getDefaultInstance()));*/

        //ChaincodeSupportGrpc.ChaincodeSupportBlockingStub chaincodeSupportBlockingStub = ChaincodeSupportGrpc.newBlockingStub(channelBuilder.build());

        /*EndorserGrpc.EndorserBlockingStub endorserBlockingStub = EndorserGrpc.newBlockingStub(channelBuilder.build());
        endorserBlockingStub.processProposal();*/

        //GossipGrpc.GossipBlockingStub gossipBlockingStub = GossipGrpc.newBlockingStub(channelBuilder.build());

        /*StreamObserver<EventsOuterClass.Event> observer = eventsGrpcClient.chat(new ChaincodeEventHandler());
        EventsOuterClass.Interest.Builder blockInterest = EventsOuterClass.Interest.newBuilder().setEventType(EventsOuterClass.EventType.BLOCK);
        EventsOuterClass.Interest.Builder rejectionInterest = EventsOuterClass.Interest.newBuilder().setEventType(EventsOuterClass.EventType.REJECTION);

        EventsOuterClass.Register.Builder register = EventsOuterClass.Register.newBuilder()
                .addEvents(blockInterest)
                .addEvents(rejectionInterest);
        EventsOuterClass.Event event = EventsOuterClass.Event.newBuilder().setRegister(register).build();
        observer.onNext(event);*/
    }

    @Test
    public void test1() {
    }
}
