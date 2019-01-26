package com.zerra.common.network;

import simplenet.Client;

/**
 * A wrapper for {@link Client} so that only blocking read methods are accessible
 */
public class ClientWrapper
{
    private final Client client;

    public ClientWrapper(Client client)
    {
        this.client = client;
    }

    public boolean readBoolean()
    {
        return client.readBoolean();
    }

    public byte readByte()
    {
        return client.readByte();
    }

    public byte[] readBytes(int numBytes)
    {
        byte[] bytes = new byte[numBytes];
        for(int i = 0; i < numBytes; i++)
        {
            bytes[i] = client.readByte();
        }
        return bytes;
    }

    public char readChar()
    {
        return client.readChar();
    }

    public double readDouble()
    {
        return client.readDouble();
    }

    public float readFloat()
    {
        return client.readFloat();
    }

    public int readInt()
    {
        return client.readInt();
    }

    public long readLong()
    {
        return client.readLong();
    }

    public short readShort()
    {
        return client.readShort();
    }

    public String readString()
    {
        return client.readString();
    }
}
