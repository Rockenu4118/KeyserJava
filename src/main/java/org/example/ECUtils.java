package org.example;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class ECUtils {
    public static BigInteger Gx = new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16);
    public static BigInteger Gy = new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16);
    public static BigInteger order = new BigInteger("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16);
    public static BigInteger p = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16);
    public static BigInteger a = BigInteger.ZERO;
    public static BigInteger b = new BigInteger("7", 16);

    // Create curve and domain parameters
    public static org.bouncycastle.math.ec.ECCurve.Fp curve = new org.bouncycastle.math.ec.ECCurve.Fp(p, a, b);

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static ECPoint hex2point(String hexStr) {
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
        return ecSpec.getCurve().decodePoint(hexStringToByteArray(hexStr));
    }

    public static ECCurve secp256k1() {
        return new ECCurve.Fp(p, a, b);
    }

    public static ECPoint G() {
        return secp256k1().createPoint(Gx, Gy);
    }

    public static ECPoint ec_mul(BigInteger scalar, ECPoint point) {
        if (point == null)
            return G().multiply(scalar);
        else
            return point.multiply(scalar);
    }

    public static byte[] hashS(BigInteger s) {
        SHA3.DigestSHA3 digest = new SHA3.Digest256();
        return digest.digest(s.toByteArray());
    }

    public static byte[] hashP(ECPoint point) {
        SHA3.DigestSHA3 digest = new SHA3.Digest256();
        return digest.digest(point.getEncoded(true));
    }

    public static BigInteger Hs(byte[] bytes) throws NoSuchAlgorithmException {
        return new BigInteger(bytes);
    }

    public static BigInteger Rand(int size) {
        SecureRandom rnd = new SecureRandom();
        BigInteger r = new BigInteger(size, rnd);
        r = r.mod(order);
        return r;
    }
}