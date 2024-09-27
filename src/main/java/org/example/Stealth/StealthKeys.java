package org.example.Stealth;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.example.ECUtils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class StealthKeys {
    private BigInteger _privSpend;
    private ECPoint    _pubSpend;
    private BigInteger _privView;
    private ECPoint    _pubView;

    public StealthKeys() throws NoSuchAlgorithmException {
        _privSpend = ECUtils.Rand(256);
        _pubSpend  = ECUtils.ec_mul(_privSpend, null);
        _privView  = ECUtils.Hs(ECUtils.hashS(_privSpend));
        _privView = _privView.mod(ECUtils.order);
        _pubView   = ECUtils.ec_mul(_privView, null);
    }

    public StealthKeys(String seed) throws NoSuchAlgorithmException {
        _privSpend = new BigInteger(seed, 16);
        _pubSpend  = ECUtils.ec_mul(_privSpend, null);
        _privView  = ECUtils.Hs(ECUtils.hashS(_privSpend));
        _privView = _privView.mod(ECUtils.order);
        _pubView   = ECUtils.ec_mul(_privView, null);
    }

    @Override
    public String toString() {
        String str = "";
        str += "Priv Spend: " + _privSpend.toString(16) + "\n";
        str += "Pub Spend:  " + Hex.toHexString(_pubSpend.getEncoded(true)) + "\n";
        str += "Priv View:  " + _privView.toString(16) + "\n";
        str += "Pub View:   " + Hex.toHexString(_pubView.getEncoded(true)) + "\n";

        return str;
    }

    public String genKeyserAddr()
    {
        return Hex.toHexString(_pubSpend.getEncoded(true)) + Hex.toHexString(_pubView.getEncoded(true));
    }

    public String[] genStealthAddr(String recipient) throws NoSuchAlgorithmException {
        String[] strs = new String[2];

        String pubSpendKey = recipient.substring(0, 66);
        String pubViewKey = recipient.substring(66);

        BigInteger r = ECUtils.Rand(256);
        ECPoint R = ECUtils.ec_mul(r, null);
        ECPoint A = ECUtils.hex2point(pubViewKey);
        ECPoint B = ECUtils.hex2point(pubSpendKey);

        ECPoint D = ECUtils.ec_mul(r, A);

        BigInteger f = ECUtils.Hs(ECUtils.hashP(D));
        ECPoint F = ECUtils.ec_mul(f, null);
        ECPoint P = F.add(B);

        strs[0] = Hex.toHexString(P.getEncoded(true));
        strs[1] = Hex.toHexString(R.getEncoded(true));

        return strs;
    }

    public boolean verifyStealthAddrOwnership(String stealthAddr, String RStr) throws NoSuchAlgorithmException {
        ECPoint P = ECUtils.hex2point(stealthAddr);
        ECPoint R = ECUtils.hex2point(RStr);
        BigInteger a = _privView;
        ECPoint D = ECUtils.ec_mul(a, R);
        BigInteger f = ECUtils.Hs(ECUtils.hashP(D));
        ECPoint F = ECUtils.ec_mul(f, null);
        ECPoint B = _pubSpend;
        ECPoint Pdash = F.add(B);
        return P.equals(Pdash);
    }

    public BigInteger deriveStealthKey(String RStr) throws NoSuchAlgorithmException {
        ECPoint R = ECUtils.hex2point(RStr);
        BigInteger a = _privView;
        ECPoint D = ECUtils.ec_mul(a, R);
        BigInteger f = ECUtils.Hs(ECUtils.hashP(D));
        BigInteger b = _privSpend;
        return f.add(b);
    }

    public static boolean verifyStealthKey(String PStr, String xStr) {
        ECPoint P = ECUtils.hex2point(PStr);
        BigInteger x = new BigInteger(xStr, 16);
        ECPoint Pdash = ECUtils.ec_mul(x, null);
        return P.equals(Pdash);
    }
}
