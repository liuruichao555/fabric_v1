package protos;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.1.3-SNAPSHOT)",
    comments = "Source: peer/peer.proto")
public class EndorserGrpc {

  private EndorserGrpc() {}

  public static final String SERVICE_NAME = "protos.Endorser";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<protos.ProposalOuterClass.SignedProposal,
      protos.ProposalResponseOuterClass.ProposalResponse> METHOD_PROCESS_PROPOSAL =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "protos.Endorser", "ProcessProposal"),
          io.grpc.protobuf.ProtoUtils.marshaller(protos.ProposalOuterClass.SignedProposal.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(protos.ProposalResponseOuterClass.ProposalResponse.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EndorserStub newStub(io.grpc.Channel channel) {
    return new EndorserStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EndorserBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new EndorserBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static EndorserFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new EndorserFutureStub(channel);
  }

  /**
   */
  public static abstract class EndorserImplBase implements io.grpc.BindableService {

    /**
     */
    public void processProposal(protos.ProposalOuterClass.SignedProposal request,
        io.grpc.stub.StreamObserver<protos.ProposalResponseOuterClass.ProposalResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_PROCESS_PROPOSAL, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_PROCESS_PROPOSAL,
            asyncUnaryCall(
              new MethodHandlers<
                protos.ProposalOuterClass.SignedProposal,
                protos.ProposalResponseOuterClass.ProposalResponse>(
                  this, METHODID_PROCESS_PROPOSAL)))
          .build();
    }
  }

  /**
   */
  public static final class EndorserStub extends io.grpc.stub.AbstractStub<EndorserStub> {
    private EndorserStub(io.grpc.Channel channel) {
      super(channel);
    }

    private EndorserStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EndorserStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new EndorserStub(channel, callOptions);
    }

    /**
     */
    public void processProposal(protos.ProposalOuterClass.SignedProposal request,
        io.grpc.stub.StreamObserver<protos.ProposalResponseOuterClass.ProposalResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_PROCESS_PROPOSAL, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class EndorserBlockingStub extends io.grpc.stub.AbstractStub<EndorserBlockingStub> {
    private EndorserBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private EndorserBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EndorserBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new EndorserBlockingStub(channel, callOptions);
    }

    /**
     */
    public protos.ProposalResponseOuterClass.ProposalResponse processProposal(protos.ProposalOuterClass.SignedProposal request) {
      return blockingUnaryCall(
          getChannel(), METHOD_PROCESS_PROPOSAL, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class EndorserFutureStub extends io.grpc.stub.AbstractStub<EndorserFutureStub> {
    private EndorserFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private EndorserFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EndorserFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new EndorserFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.ProposalResponseOuterClass.ProposalResponse> processProposal(
        protos.ProposalOuterClass.SignedProposal request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_PROCESS_PROPOSAL, getCallOptions()), request);
    }
  }

  private static final int METHODID_PROCESS_PROPOSAL = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final EndorserImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(EndorserImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PROCESS_PROPOSAL:
          serviceImpl.processProposal((protos.ProposalOuterClass.SignedProposal) request,
              (io.grpc.stub.StreamObserver<protos.ProposalResponseOuterClass.ProposalResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class EndorserDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return protos.Peer.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (EndorserGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EndorserDescriptorSupplier())
              .addMethod(METHOD_PROCESS_PROPOSAL)
              .build();
        }
      }
    }
    return result;
  }
}
