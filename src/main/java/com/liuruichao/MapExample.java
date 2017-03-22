//package com.liuruichao;
//
//import org.hyperledger.java.shim.ChaincodeBase;
//import org.hyperledger.java.shim.ChaincodeStub;
//
///**
// * MapExample
// *
// * @author liuruichao
// * Created on 2017/2/17 14:18
// */
//public class MapExample extends ChaincodeBase {
//    @Override
//    public String run(ChaincodeStub stub, String function, String[] args) {
//        switch (function) {
//            case "put":
//                for (int i = 0; i < args.length; i += 2)
//                    stub.putState(args[i], args[i + 1]);
//                break;
//            case "del":
//                for (String arg : args)
//                    stub.delState(arg);
//                break;
//        }
//        return null;
//    }
//
//    @Override
//    public String query(ChaincodeStub stub, String function, String[] args) {
////		if ("range".equals(function)) {
////			String build = "";
////			HashMap<String, String> range = stub.getStateByRange(args[0], args[1], 10);
////			for (String s : range.keySet()) {
////				build += s + ":" + range.get(s) + " ";
////			}
////			return build;
////		}
//        return stub.getState(args[0]);
//    }
//
//    @Override
//    public String getChaincodeID() {
//        return "map";
//    }
//
//    public static void main(String[] args) throws Exception {
//        new MapExample().start(args);
//    }
//
//}
