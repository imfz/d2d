package lv.k2611a.util;

public class ByteUtils {

    private ByteUtils() {

    }

    public static byte[] intToBytes(int input) {
        byte [] b = new byte[4];
        for(int i= 0; i < 4; i++){
            b[3 - i] = (byte)(input >>> (i * 8));
        }
        return b;
    }


    public static byte[] intToBytes(long input) {
        byte [] b = new byte[4];
        for(int i= 0; i < 4; i++){
            b[3 - i] = (byte)(input >>> (i * 8));
        }
        return b;
    }
}
