package com.liuruichao;

import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.junit.Before;
import org.junit.Test;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * MemberService
 *
 * @author liuruichao
 * Created on 2017/3/22 16:15
 */
public class MemberService {
    private String adminCert = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUIyakNDQVlDZ0F3SUJBZ0lVY21XcUdFM2Q1QThuQnBJVzJvY3U3TjlERytjd0NnWUlLb1pJemowRUF3SXcKYURFTE1Ba0dBMVVFQmhNQ1ZWTXhGekFWQmdOVkJBZ1REazV2Y25Sb0lFTmhjbTlzYVc1aE1SUXdFZ1lEVlFRSwpFd3RJZVhCbGNteGxaR2RsY2pFUE1BMEdBMVVFQ3hNR1JtRmljbWxqTVJrd0Z3WURWUVFERXhCbVlXSnlhV010ClkyRXRjMlZ5ZG1WeU1CNFhEVEUzTURNeU1qQTRORGd3TUZvWERURTRNREl4T0RFMk5EZ3dNRm93RURFT01Bd0cKQTFVRUF4TUZZV1J0YVc0d1dUQVRCZ2NxaGtqT1BRSUJCZ2dxaGtqT1BRTUJCd05DQUFUZjlCaldyNC9wajVRZwpCN2FvL08ydG5KWE9NT0I5Q2haT0xxVVAwY3pFYUk4eXp0eldoSktyTFhlT0tPV0NqUm52emVkRWtHTkw3ZXZkClNyWjA2Z050bzJBd1hqQU9CZ05WSFE4QkFmOEVCQU1DQWdRd0RBWURWUjBUQVFIL0JBSXdBREFkQmdOVkhRNEUKRmdRVWdvdGI0ZGtwa2dtZm82T25GclV2NXJUWG16UXdId1lEVlIwakJCZ3dGb0FVaFNtZFRlYy94V0tFQXExOApFb2FBN2pYQ2hiVXdDZ1lJS29aSXpqMEVBd0lEU0FBd1JRSWhBTkl2UVZVVFE0VGtNU1NERVgwREZJOCtsVEQ5CjE4MWtueTlqaEVVOWdTdHNBaUFTSDd2aFdoaUNyVHR3OUFGNnFoVGJhK0xoeHlPMEphVk1lbkVYNEpJUEV3PT0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=";
    private String curveName = "P-256";

    @Before
    public void before() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    @Test
    public void enroll() throws Exception {
        /*
            this.curveName = "P-256";
            this.curveName = "secp384r1";
         */
        String username = "admin";
        String password = "adminpw";

        ECGenParameterSpec ecGenSpec = new ECGenParameterSpec(curveName);
        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        g.initialize(ecGenSpec, new SecureRandom());
        KeyPair signingKeyPair = g.generateKeyPair();

        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                new X500Principal("CN=" + username), signingKeyPair.getPublic());
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withECDSA");
        ContentSigner signer = csBuilder.build(signingKeyPair.getPrivate());
        PKCS10CertificationRequest csr = p10Builder.build(signer);


        PemObject pemCSR = new PemObject("CERTIFICATE REQUEST", csr.getEncoded());

        StringWriter str = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(str);
        pemWriter.writeObject(pemCSR);
        pemWriter.close();
        str.close();
        String pem = str.toString();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("certificate_request", pem);

        String responseBody = httpPost("http://localhost:7054/api/v1/cfssl/enroll", jsonObject.toString(), username, password);

        System.out.println(responseBody);
        /*Base64.Decoder b64dec = Base64.getDecoder();
        String signedPem = new String(b64dec.decode(adminCert.getBytes(UTF_8)));*/

    }

    @Test
    public void register() throws Exception {
        /*JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("enrollmentID", "liuruichao");
        jsonObject.addProperty("affiliation", "org1.department1");
        jsonObject.addProperty("type", "user");*/
        RegistrationRequest rr = new RegistrationRequest("liuruichao", "org1.department1");

        String body = rr.toJson();
        System.out.println(body);

        ECGenParameterSpec ecGenSpec = new ECGenParameterSpec(curveName);
        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        g.initialize(ecGenSpec, new SecureRandom());
        KeyPair signingKeyPair = g.generateKeyPair();

        Base64.Decoder b64dec = Base64.getDecoder();
        String signedPem = new String(b64dec.decode(adminCert.getBytes(UTF_8)));

        String authHdr = getHTTPAuthCertificate(signedPem, signingKeyPair.getPrivate(), body);

        String result = httpPost("http://localhost:7054/api/v1/cfssl/register", body, authHdr);
        System.out.println(result);
    }

    private String getHTTPAuthCertificate(String adminCert, PrivateKey privateKey, String body) throws Exception {
        Base64.Encoder b64 = Base64.getEncoder();
        String cert = b64.encodeToString(adminCert.getBytes(UTF_8));
        body = b64.encodeToString(body.getBytes(UTF_8));
        String signString = body + "." + cert;
        byte[] signature = ecdsaSignToBytes(privateKey, signString.getBytes(UTF_8));
        return cert + "." + b64.encodeToString(signature);
    }

    private byte[] ecdsaSignToBytes(PrivateKey privateKey, byte[] data) throws Exception {
        byte[] encoded = hash(data);

        // char[] hexenncoded = Hex.encodeHex(encoded);
        // encoded = new String(hexenncoded).getBytes();

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

    private BigInteger[] preventMalleability(BigInteger[] sigs, BigInteger curve_n) {
        BigInteger cmpVal = curve_n.divide(BigInteger.valueOf(2l));

        BigInteger sval = sigs[1];

        if (sval.compareTo(cmpVal) == 1) {

            sigs[1] = curve_n.subtract(sval);
        }

        return sigs;
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

    private String httpPost(String url, String body, String authHTTPCert) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        HttpClient client = HttpClientBuilder.create().build();
        final HttpClientContext context = HttpClientContext.create();
        httpPost.setEntity(new StringEntity(body));
        httpPost.addHeader("Authorization", authHTTPCert);

        HttpResponse response = client.execute(httpPost, context);
        int status = response.getStatusLine().getStatusCode();

        HttpEntity entity = response.getEntity();
        String responseBody = entity != null ? EntityUtils.toString(entity) : null;

        return responseBody;
    }

    private String httpPost(String url, String body, String username, String password) throws Exception {
        CredentialsProvider provider = new BasicCredentialsProvider();

        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));


        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

        HttpPost httpPost = new HttpPost(url);

        AuthCache authCache = new BasicAuthCache();

        HttpHost targetHost = new HttpHost(httpPost.getURI().getHost(), httpPost.getURI().getPort());

        authCache.put(targetHost, new BasicScheme());

        final HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(provider);
        context.setAuthCache(authCache);

        httpPost.setEntity(new StringEntity(body));

        HttpResponse response = client.execute(httpPost, context);
        int status = response.getStatusLine().getStatusCode();

        HttpEntity entity = response.getEntity();
        String responseBody = entity != null ? EntityUtils.toString(entity) : null;

        if (status >= 400) {

            Exception e = new Exception(format("POST request to %s failed with status code: %d. Response: %s", url, status, responseBody));
            throw e;
        }

        return responseBody;
    }
}
