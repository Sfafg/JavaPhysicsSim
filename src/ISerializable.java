package src;

import java.nio.ByteBuffer;

public interface ISerializable {
    public ByteBuffer ToBytes();

    public int Sizeof();

}
