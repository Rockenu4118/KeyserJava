package org.example;

import java.sql.Timestamp;

public class Block {
    public int       _index;
    public Timestamp _time;
    public int       _data;
    public int       _nonce = 0;

    public Block (int i, int data) {
        _index = i;
        _time  = new Timestamp(System.nanoTime());
        _data  = data;
    }

    @Override
    public String toString()
    {
        return String.valueOf(_index + _time.getNanos() + _data + _nonce);
    }
}
