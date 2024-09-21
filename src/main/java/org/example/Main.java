package org.example;
import com.google.protobuf.Message;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.test.FixedSecureRandom;
import org.example.Stealth.StealthKeys;
import org.example.protobuf.PersonProtos;
import org.bouncycastle.util.encoders.Hex;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;

import static org.bouncycastle.jce.provider.BouncyCastleProvider.getPublicKey;
import static org.example.Utils.hexStringToByteArray;


public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
//        Security.addProvider(new BouncyCastleProvider());
        StealthKeys sk = new StealthKeys();
//
//
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "BC");
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256k1");
//        keyGen.initialize(ecSpec, new SecureRandom());
//
//        KeyPair keyPair = keyGen.generateKeyPair();
//
//        PrivateKey privateKey = keyPair.getPrivate();
//        PublicKey publicKey = keyPair.getPublic();
//
//        // Print the keys in their encoded form (e.g., for storage or transmission)
//        System.out.println("Private Key: " + Hex.toHexString(privateKey.getEncoded()));
//        System.out.println("Public Key: " + Hex.toHexString(publicKey.getEncoded()));



        BigInteger bigahhint = new BigInteger("18e14a7b6a307f426a94f8114701e7c8e774e7f9a47e2c2035db29a206321725", 16);

        ECPoint point = Utils.ec_mul(bigahhint);

        System.out.println(bigahhint.toString(16));


        System.out.println("Public Key: " + Hex.toHexString(point.getEncoded(false)));  // false to get the uncompressed form
//
        MessageDigest mdigest = MessageDigest.getInstance("SHA-256");
        System.out.println(Hex.toHexString(mdigest.digest(point.getEncoded(false))));


//        System.out.println("815bfe7bb2c662d6cb864d94b23491fbf1e3dfad345f709a02694af3d0c95e70" + );

//        JFrame frame = new JFrame("Test");
//        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
//        frame.setSize(500, 500);
//        frame.setVisible(true);

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.createContext("/shutdown", new MyHandler2());
        server.setExecutor(null); // creates a default executor
        server.start();



//        System.out.println("Main thread is finished. Daemon thread will continue to run.");

//        System.out.println("Bruh");
//        String name = "fefe";
//        int age = 21;
//        byte[] privKey = new byte[32];
//        privKey[31] = 2;

//        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256k1");
//        ECPoint pointQ = spec.getG().multiply(new BigInteger(1, privKey));
//
//        pointQ = pointQ.normalize();
//
//        System.out.println(pointQ.getRawXCoord());
//        System.out.println(pointQ.getRawYCoord());


//


//


//        PersonProtos.Person person = PersonProtos.Person.newBuilder()
//                .setName(name)
//                .setAge(age)
//                .build();
//
//        System.out.println(person);
    }


    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    static public void daemonize()
    {
        System.out.close();
        System.err.close();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class MyHandler2 implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            System.exit(0);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}