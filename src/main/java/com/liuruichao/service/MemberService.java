package com.liuruichao.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.liuruichao.dto.EnrollResponse;
import com.liuruichao.model.Member;
import com.liuruichao.model.RegistrationRequest;
import com.liuruichao.security.CryptoPrimitive;
import lombok.extern.slf4j.Slf4j;
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

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.security.*;
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
@Slf4j
public class MemberService {
    private CryptoPrimitive cryptoPrimitive;

    private final String curveName = "P-256";

    private final Base64.Encoder b64enc = Base64.getEncoder();

    private final Base64.Decoder b64dec = Base64.getDecoder();

    private Gson gson = new Gson();

    public MemberService() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        cryptoPrimitive = new CryptoPrimitive();
    }

    public Member enroll(String username, String password) throws Exception {
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

        EnrollResponse enrollResponse = gson.fromJson(responseBody, EnrollResponse.class);

        if (!enrollResponse.isSuccess()) {
            throw new RuntimeException("admin登录失败！");
        }

        log.debug("pem: {}", enrollResponse.getResult());
        String signedPem = new String(b64dec.decode(enrollResponse.getResult().getCert().getBytes(UTF_8)));

        Member member = new Member();
        member.setPrivateKey(signingKeyPair.getPrivate());
        member.setCert(signedPem);
        return member;

        // 必须要用一样的signingKeyPair
        //register(signedPem, signingKeyPair);
    }

    public void register(String signedPem, KeyPair signingKeyPair) throws Exception {
        /*JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("enrollmentID", "liuruichao");
        jsonObject.addProperty("affiliation", "org1.department1");
        jsonObject.addProperty("type", "user");*/
        RegistrationRequest req = new RegistrationRequest("liuruichao", "org1.department1");

        String body = req.toJson();

        /*ECGenParameterSpec ecGenSpec = new ECGenParameterSpec(curveName);
        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        g.initialize(ecGenSpec, new SecureRandom());
        KeyPair signingKeyPair = g.generateKeyPair();*/

        String authHdr = getHTTPAuthCertificate(signedPem, signingKeyPair.getPrivate(), body);

        String result = httpPost("http://localhost:7054/api/v1/cfssl/register", body, authHdr);
        System.out.println(result);
    }

    private String getHTTPAuthCertificate(String signedPem, PrivateKey privateKey, String body) throws Exception {
        String cert = b64enc.encodeToString(signedPem.getBytes(UTF_8));
        body = b64enc.encodeToString(body.getBytes(UTF_8));
        String signString = body + "." + cert;
        byte[] signature = cryptoPrimitive.ecdsaSignToBytes(privateKey, signString.getBytes(UTF_8));
        return cert + "." + b64enc.encodeToString(signature);
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
