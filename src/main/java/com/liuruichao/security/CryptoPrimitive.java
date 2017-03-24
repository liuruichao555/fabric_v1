package com.liuruichao.security;

import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;

/**
 * CryptoPrimitive
 *
 * @author liuruichao
 * Created on 2017/3/23 15:54
 */
public final class CryptoPrimitive {
    private static final String curveName = "P-256";

    public static byte[] ecdsaSignToBytes(PrivateKey privateKey, byte[] data) throws Exception {
        byte[] encoded = hash(data);

        X9ECParameters params = NISTNamedCurves.getByName(curveName);
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

    private static BigInteger[] preventMalleability(BigInteger[] sigs, BigInteger curve_n) {
        BigInteger cmpVal = curve_n.divide(BigInteger.valueOf(2l));

        BigInteger sval = sigs[1];

        if (sval.compareTo(cmpVal) == 1) {

            sigs[1] = curve_n.subtract(sval);
        }

        return sigs;
    }

    private static byte[] hash(byte[] input) {
        Digest digest = getHashDigest();
        byte[] retValue = new byte[digest.getDigestSize()];
        digest.update(input, 0, input.length);
        digest.doFinal(retValue, 0);
        return retValue;
    }

    private static Digest getHashDigest() {
        return new SHA256Digest();
    }
}
