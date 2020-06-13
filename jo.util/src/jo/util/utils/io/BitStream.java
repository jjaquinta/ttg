package jo.util.utils.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitStream
{
    private InputStream mRaw;
    private int         mOffset;
    private byte        mValue;
    
    public BitStream(InputStream is)
    {
        mRaw = is;
    }
    
    public BitStream(byte[] bytes)
    {
        mRaw = new ByteArrayInputStream(bytes);
    }
    
    public Boolean readBit() throws IOException
    {
        if (mOffset == 0)
        {
            int ch = mRaw.read();
            if (ch < 0)
                return null;
            mValue = (byte)ch;
        }
        Boolean ret = (mValue&(1<<mOffset)) != 0;
        mOffset = (mOffset + 1)%8;
        return ret;
    }
    
    public int readInt(int size) throws IOException
    {
        int ret = 0;
        for (int i = 0; i < size; i++)
            if (readBit())
                ret |= (1<<i);
        return ret;
    }
}
