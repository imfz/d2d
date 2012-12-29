package lv.k2611a.network.resp;

public interface CustomSerialization {
    byte serializerId();
    byte[] toBytes();
    int getSize();
}
