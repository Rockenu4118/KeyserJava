package org.example;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.example.Stealth.StealthKeys;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.*;


public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        StealthKeys aj = new StealthKeys();
        StealthKeys bob = new StealthKeys();

        String bobsAddr = bob.genKeyserAddr();
        String[] res = aj.genStealthAddr(bobsAddr);

        System.out.println("Stealth Addr: " + res[0]);
        System.out.println("R: " + res[1]);

        BigInteger x = bob.deriveStealthKey(res[1]);

        ECPoint P = ECUtils.ec_mul(x, null);
        System.out.println(StealthKeys.verifyStealthKey(Hex.toHexString(P.getEncoded(true)), Hex.toHexString(x.toByteArray())));

        System.out.println(bob.verifyStealthAddrOwnership(res[0], res[1]));

//        JFrame frame = new JFrame("Test");
//        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
//        frame.setSize(500, 500);
//        frame.setVisible(true);

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.createContext("/shutdown", new MyHandler2());
        server.setExecutor(null); // creates a default executor
        server.start();


//        int age = 21;
//        byte[] privKey = new byte[32];
//        privKey[31] = 2;



//        PersonProtos.Person person = PersonProtos.Person.newBuilder()
//                .setName(name)
//                .setAge(age)
//                .build();
//
//        System.out.println(person);
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