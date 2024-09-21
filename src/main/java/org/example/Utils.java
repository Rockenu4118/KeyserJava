package org.example;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static ECPoint ec_mul(BigInteger privateKey) {
        // secp256k1 domain parameters
        BigInteger p = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16);
        BigInteger a = BigInteger.ZERO;
        BigInteger b = new BigInteger("7", 16);
        BigInteger Gx = new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16);
        BigInteger Gy = new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16);

        // Create curve and domain parameters
        org.bouncycastle.math.ec.ECCurve.Fp curve = new org.bouncycastle.math.ec.ECCurve.Fp(p, a, b);
        ECPoint G = curve.createPoint(Gx, Gy);

        // Multiply the scalar (private key) by the generator point G
        ECPoint publicKeyPoint = G.multiply(privateKey);

        return publicKeyPoint;
    }

    public static BigInteger Hs(ECPoint point) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        System.out.println(Hex.toHexString(digest.digest(point.getEncoded(false))));

        return new BigInteger(digest.digest(point.getEncoded(false)));
    }
}