package jo.util.utils.obj;

import java.io.IOException;
import java.util.StringTokenizer;

import jo.util.utils.io.BitStream;

public class ByteUtils
{
    public static byte flipByte(byte b)
    {
        return (byte)(((b&0x01)<<7)
            |((b&0x02)<<5)
            |((b&0x04)<<3)
            |((b&0x08)<<1)
            |((b&0x10)>>1)
            |((b&0x20)>>3)
            |((b&0x40)>>5)
            |((b&0x80)>>7));
    }
    
    public static boolean getBit(byte[] bytes, int off)
    {
        byte b = bytes[off/8];
        return (b&(0x01<<(off%8))) != 0;
    }
    
    public static void setBit(byte[] bytes, int off, boolean v)
    {
        if (v)
            bytes[off/8] |= (0x01<<(off%8));
        else
            bytes[off/8] &= ~(0x01<<(off%8));
    }
    
    public static void flipBits(byte[] bytes, int start, int bitLength)
    {
        for (int i = 0; i < bitLength/2; i++)
        {
            boolean leftBit = getBit(bytes, start*8 + i);
            boolean rightBit = getBit(bytes, start*8 + bitLength - i - 1);
            setBit(bytes, start*8 + i, rightBit);
            setBit(bytes, start*8 + bitLength - i - 1, leftBit);
        }
    }

    public static void copyBits(byte[] from, int fromByteOffset, int fromBitOffset, byte[] to, int toByteOffset, int toBitOffset, int bits)
    {
        for (int i = 0; i < bits; i++)
        {
            boolean bit = getBit(from, fromByteOffset*8 + fromBitOffset + i);
            setBit(to, toByteOffset*8 + toBitOffset + i, bit);
        }
    }

    public static Object[] toArray(byte[] byteArray)
    {
        if (byteArray == null)
            return null;
        Byte[] objArray = new Byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++)
            objArray[i] = new Byte(byteArray[i]);
        return objArray;
    }
    
    public static void flipBytes(byte[] bytes, int chunk)
    {
        for (int o = 0; o < bytes.length; o += chunk)
        {
            for (int i = 0; i < chunk/2; i++)
            {
                int j = chunk - 1 - i;
                byte b = bytes[o+i];
                bytes[o+i] = bytes[o+j];
                bytes[o+j] = b;
            }
        }
    }

    // LONG CONVERSION


    public static long toLong(byte[] readBuffer, int o)
    {
        return (((long)readBuffer[0] << 56) +
                                ((long)(readBuffer[1] & 255) << 48) +
                                ((long)(readBuffer[2] & 255) << 40) +
                                ((long)(readBuffer[3] & 255) << 32) +
                                ((long)(readBuffer[4] & 255) << 24) +
                                ((readBuffer[5] & 255) << 16) +
                                ((readBuffer[6] & 255) <<  8) +
                                ((readBuffer[7] & 255) <<  0));
    }
    
    public static long toLong(byte[] readBuffer)
    {
        return toLong(readBuffer, 0);
    }
    
    public static long[] toLongs(byte[] readBuffer, int o, int l)
    {
        long[] v = new long[l/8];
        for (int i = 0; i < v.length; i++)
            v[i] = toLong(readBuffer, o + i*8);
        return v;
    }
  
    public static byte[] toBytes(long v, byte[] writeBuffer, int o)
    {
        writeBuffer[o+0] = (byte)(v >>> 56);
        writeBuffer[o+1] = (byte)(v >>> 48);
        writeBuffer[o+2] = (byte)(v >>> 40);
        writeBuffer[o+3] = (byte)(v >>> 32);
        writeBuffer[o+4] = (byte)(v >>> 24);
        writeBuffer[o+5] = (byte)(v >>> 16);
        writeBuffer[o+6] = (byte)(v >>>  8);
        writeBuffer[o+7] = (byte)(v >>>  0);
        return writeBuffer;
    }
    public static byte[] toBytes(long v, byte[] writeBuffer)
    {
        return toBytes(v, writeBuffer, 0);
    }
    
    public static byte[] toBytes(long v)
    {
        byte[] writeBuffer = new byte[8];
        return toBytes(v, writeBuffer, 0);
    }
    
    public static byte[] toBytes(long[] v, byte[] writeBuffer, int o)
    {
        for (int i = 0; i < v.length; i++)
            toBytes(v[i], writeBuffer, o + i*8);
        return writeBuffer;
    }
    
    public static byte[] toBytes(long[] v)
    {
        byte[] writeBuffer = new byte[8*v.length];
        return toBytes(v, writeBuffer, 0);
    }
    
    // INT CONVERSION

    public static int toInt(byte[] readBuffer, int o)
    {
        int ch1 = readBuffer[o++];
        int ch2 = readBuffer[o++];
        int ch3 = readBuffer[o++];
        int ch4 = readBuffer[o++];
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new IllegalStateException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));    
    }
    
    public static int toInt(byte[] readBuffer)
    {
        return toInt(readBuffer, 0);
    }
    
    public static int[] toInts(byte[] readBuffer, int o, int l)
    {
        int[] v = new int[l/4];
        for (int i = 0; i < v.length; i++)
            v[i] = toInt(readBuffer, o + i*4);
        return v;
    }
    
    public static int[] toInts(byte[] readBuffer, int bitsize)
    {
        int size = readBuffer.length*8/bitsize;
        int[] v = new int[size];
        BitStream bs = new BitStream(readBuffer);
        for (int i = 0; i < size; i++)
            try
            {
                v[i] = bs.readInt(bitsize);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        return v;
    }
    
    public static byte[] toBytes(int v, byte[] writeBuffer, int o)
    {
        writeBuffer[o+0] = (byte)(v >>> 24);
        writeBuffer[o+1] = (byte)(v >>> 16);
        writeBuffer[o+2] = (byte)(v >>>  8);
        writeBuffer[o+3] = (byte)(v >>>  0);
        return writeBuffer;
    }
    
    public static byte[] toBytes(int v)
    {
        byte[] writeBuffer = new byte[4];
        return toBytes(v, writeBuffer, 0);
    }
    
    public static byte[] toBytes(int[] v, byte[] writeBuffer, int o)
    {
        for (int i = 0; i < v.length; i++)
            toBytes(v[i], writeBuffer, o + i*4);
        return writeBuffer;
    }
    
    public static byte[] toBytes(int[] v)
    {
        byte[] writeBuffer = new byte[4*v.length];
        return toBytes(v, writeBuffer, 0);
    }

    // SHORT CONVERSION

    // SHORT CONVERSION

    public static short toShort(byte[] readBuffer, int o)
    {
        return (short)(((readBuffer[o+0] & 255) <<  8) +
                ((readBuffer[o+1] & 255) <<  0));
    }
    
    public static short toShort(byte[] readBuffer)
    {
        return toShort(readBuffer, 0);
    }
    
    public static short[] toShorts(byte[] readBuffer, int o, int l)
    {
        short[] v = new short[l/2];
        for (int i = 0; i < v.length; i++)
            v[i] = toShort(readBuffer, o + i*2);
        return v;
    }

    public static byte[] toBytes(short v, byte[] writeBuffer, int o)
    {
        writeBuffer[o+0] = (byte)(v >>>  8);
        writeBuffer[o+1] = (byte)(v >>>  0);
        return writeBuffer;
    }
    
    public static byte[] toBytes(short v)
    {
        byte[] writeBuffer = new byte[2];
        return toBytes(v, writeBuffer, 0);
    }
    
    public static byte[] toBytes(short[] v, byte[] writeBuffer, int o)
    {
        for (int i = 0; i < v.length; i++)
            toBytes(v[i], writeBuffer, o + i*2);
        return writeBuffer;
    }
    
    public static byte[] toBytes(short[] v)
    {
        byte[] writeBuffer = new byte[2*v.length];
        return toBytes(v, writeBuffer, 0);
    }

    // assumes MSB first
    public static int toUnsignedInt(byte[] bytes)
    {
        return toUnsignedInt(bytes, 0, bytes.length);
    }

    public static int toUnsignedInt(byte[] bytes, int o, int l)
    {
        long v = 0;
        for (int i = 0; i < l; i++)
            v = (v<<8) | (bytes[o + i]&0xff);
        return (int)v;
    }

    public static int toUnsignedInt(byte b1, byte b2)
    {
        return ((((int)b1)&0xff)<<8 | ((int)b2&0xff));
    }

    // assumes MSB first
    public static int toSignedInt(byte[] bytes)
    {
        return toSignedInt(bytes, 0, bytes.length);
    }
    public static int toSignedInt(byte[] bytes, int o, int l)
    {
        int v = 0;
        int max = 1;
        for (int i = 0; i < l; i++)
        {
            v = (v<<8) | (bytes[o + i]&0xff);
            max = max<<8;
        }
        if ((bytes[0]&0x80) == 0x80)
            return v - max;
        else
            return v;
    }

    public static String toString(byte[] bytes)
    {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes)
        {
            if (sb.length() > 0)
                sb.append('.');
            String s = Integer.toHexString(b);
            if (s.length() > 2)
                s = s.substring(s.length() - 2);
            else if (s.length() == 1)
                s = "0"+s;
            sb.append(s);
        }
        return sb.toString();
    }
    
    public static byte[] fromString(String str)
    {
        StringTokenizer st = new StringTokenizer(str, ".");
        byte[] bytes = new byte[st.countTokens()];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte)Integer.parseInt(st.nextToken(), 16);
        return bytes;
    }
    
    public static String toStringDump(byte[] b)
    {
        StringBuffer sb = new StringBuffer();
        if (b != null)
        {
            for (int i = 0; i < b.length; i += 16)
            {
                sb.append(StringUtils.zeroPrefix(Integer.toHexString(i), 4));
                for (int j = 0; j < 16; j++)
                {
                    if (j%4 == 0)
                        sb.append("  ");
                    sb.append(" ");
                    if (i + j < b.length)
                        sb.append(StringUtils.zeroPrefix(Integer.toHexString(b[i+j]&0xff), 2));
                    else
                        sb.append("  ");
                }
                sb.append("    ");
                for (int j = 0; j < 16; j++)
                {
                    if (i + j < b.length)
                    {
                        char ch = (char)(b[i+j]&0xff);
                        if ((ch >= ' ') && (ch <= '~'))
                            sb.append(ch);
                        else
                            sb.append('?');
                    }
                    else
                        sb.append(" ");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
