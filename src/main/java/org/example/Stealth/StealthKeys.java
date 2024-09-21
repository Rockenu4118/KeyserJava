package org.example.Stealth;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.example.Utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class StealthKeys {
    private BigInteger _privSpend;
    private ECPoint    _pubSpend;
    private BigInteger _privView;
    private ECPoint    _pubView;

    public StealthKeys() throws NoSuchAlgorithmException {
        SecureRandom rnd = new SecureRandom();
        _privSpend = new BigInteger(256, rnd);
        _pubSpend  = Utils.ec_mul(_privSpend);
        _privView  = Utils.Hs(_pubSpend);
        _pubView   = Utils.ec_mul(_privView);
    }

    public StealthKeys(String seed) throws NoSuchAlgorithmException {
        _privSpend = new BigInteger(seed, 16);
        _pubSpend  = Utils.ec_mul(_privSpend);
        _privView  = Utils.Hs(_pubSpend);
        _pubView   = Utils.ec_mul(_privView);
    }

    @Override
    public String toString() {
        String str = "";
        str += "Priv Spend: " + _privSpend.toString(16) + "\n";
        str += "Pub Spend: " + _pubSpend.getEncoded(false).toString();

        return str;
    }
}
