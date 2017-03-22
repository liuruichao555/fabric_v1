//package com.liuruichao.chaincode;
//
//import org.hyperledger.fabric.sdk.Enrollment;
//import org.hyperledger.fabric.sdk.EnrollmentRequest;
//import org.hyperledger.fabric.sdk.MemberServices;
//import org.hyperledger.fabric.sdk.MemberServicesFabricCAImpl;
//import org.hyperledger.fabric.sdk.exception.EnrollmentException;
//import org.junit.Test;
//
//import java.net.MalformedURLException;
//import java.security.cert.CertificateException;
//
///**
// * MemberServiceTest
// *
// * @author liuruichao
// * Created on 2017/2/22 10:27
// */
//public class MemberServiceTest {
//    @Test
//    public void test1() throws CertificateException, MalformedURLException, EnrollmentException {
//        MemberServices memberServices = new MemberServicesFabricCAImpl("http://localhost:7054", null);
//        EnrollmentRequest req = new EnrollmentRequest();
//        req.setEnrollmentID("admin");
//        req.setEnrollmentSecret("adminpw");
//        Enrollment enrollment = memberServices.enroll(req);
//        System.out.println(enrollment.getCert());
//    }
//}
