package com.liuruichao.utils;

import com.google.protobuf.ByteString;
import common.Common;
import protos.ProposalOuterClass;

import java.util.UUID;

/**
 * ProtoUtils
 *
 * @author liuruichao
 * Created on 2017/3/24 16:08
 */
public final class ProtoUtils {
    public static Common.ChannelHeader createChannelHeader(Common.HeaderType type, String txID, String chainID, long epoch, ProposalOuterClass.ChaincodeHeaderExtension chaincodeHeaderExtension) {
        Common.ChannelHeader.Builder ret = Common.ChannelHeader.newBuilder()
                .setType(type.getNumber())
                .setVersion(1)
                .setTxId(txID)
                .setChannelId(chainID)
                .setEpoch(epoch);
        if (null != chaincodeHeaderExtension) {
            ret.setExtension(chaincodeHeaderExtension.toByteString());
        }

        return ret.build();
    }

    public static ByteString getNonce() {
        //TODO right now the server does not care need to figure out
        return ByteString.copyFromUtf8(generateUUID());
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
