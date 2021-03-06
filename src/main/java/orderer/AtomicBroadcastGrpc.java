package orderer;

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
    comments = "Source: orderer/ab.proto")
public class AtomicBroadcastGrpc {

  private AtomicBroadcastGrpc() {}

  public static final String SERVICE_NAME = "orderer.AtomicBroadcast";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<common.Common.Envelope,
      orderer.Ab.BroadcastResponse> METHOD_BROADCAST =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "orderer.AtomicBroadcast", "Broadcast"),
          io.grpc.protobuf.ProtoUtils.marshaller(common.Common.Envelope.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(orderer.Ab.BroadcastResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<common.Common.Envelope,
      orderer.Ab.DeliverResponse> METHOD_DELIVER =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "orderer.AtomicBroadcast", "Deliver"),
          io.grpc.protobuf.ProtoUtils.marshaller(common.Common.Envelope.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(orderer.Ab.DeliverResponse.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AtomicBroadcastStub newStub(io.grpc.Channel channel) {
    return new AtomicBroadcastStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AtomicBroadcastBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new AtomicBroadcastBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static AtomicBroadcastFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new AtomicBroadcastFutureStub(channel);
  }

  /**
   */
  public static abstract class AtomicBroadcastImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * broadcast receives a reply of Acknowledgement for each common.Envelope in order, indicating success or type of failure
     * </pre>
     */
    public io.grpc.stub.StreamObserver<common.Common.Envelope> broadcast(
        io.grpc.stub.StreamObserver<orderer.Ab.BroadcastResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_BROADCAST, responseObserver);
    }

    /**
     * <pre>
     * deliver first requires an Envelope of type DELIVER_SEEK_INFO with Payload data as a mashaled SeekInfo message, then a stream of block replies is received.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<common.Common.Envelope> deliver(
        io.grpc.stub.StreamObserver<orderer.Ab.DeliverResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_DELIVER, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_BROADCAST,
            asyncBidiStreamingCall(
              new MethodHandlers<
                common.Common.Envelope,
                orderer.Ab.BroadcastResponse>(
                  this, METHODID_BROADCAST)))
          .addMethod(
            METHOD_DELIVER,
            asyncBidiStreamingCall(
              new MethodHandlers<
                common.Common.Envelope,
                orderer.Ab.DeliverResponse>(
                  this, METHODID_DELIVER)))
          .build();
    }
  }

  /**
   */
  public static final class AtomicBroadcastStub extends io.grpc.stub.AbstractStub<AtomicBroadcastStub> {
    private AtomicBroadcastStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AtomicBroadcastStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AtomicBroadcastStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AtomicBroadcastStub(channel, callOptions);
    }

    /**
     * <pre>
     * broadcast receives a reply of Acknowledgement for each common.Envelope in order, indicating success or type of failure
     * </pre>
     */
    public io.grpc.stub.StreamObserver<common.Common.Envelope> broadcast(
        io.grpc.stub.StreamObserver<orderer.Ab.BroadcastResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_BROADCAST, getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * deliver first requires an Envelope of type DELIVER_SEEK_INFO with Payload data as a mashaled SeekInfo message, then a stream of block replies is received.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<common.Common.Envelope> deliver(
        io.grpc.stub.StreamObserver<orderer.Ab.DeliverResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_DELIVER, getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class AtomicBroadcastBlockingStub extends io.grpc.stub.AbstractStub<AtomicBroadcastBlockingStub> {
    private AtomicBroadcastBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AtomicBroadcastBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AtomicBroadcastBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AtomicBroadcastBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class AtomicBroadcastFutureStub extends io.grpc.stub.AbstractStub<AtomicBroadcastFutureStub> {
    private AtomicBroadcastFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AtomicBroadcastFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AtomicBroadcastFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AtomicBroadcastFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_BROADCAST = 0;
  private static final int METHODID_DELIVER = 1;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AtomicBroadcastImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(AtomicBroadcastImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_BROADCAST:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.broadcast(
              (io.grpc.stub.StreamObserver<orderer.Ab.BroadcastResponse>) responseObserver);
        case METHODID_DELIVER:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.deliver(
              (io.grpc.stub.StreamObserver<orderer.Ab.DeliverResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class AtomicBroadcastDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return orderer.Ab.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AtomicBroadcastGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AtomicBroadcastDescriptorSupplier())
              .addMethod(METHOD_BROADCAST)
              .addMethod(METHOD_DELIVER)
              .build();
        }
      }
    }
    return result;
  }
}
