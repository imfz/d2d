package lv.k2611a.util;

public class ByteUtils {

    private ByteUtils() {

    }

    public static byte booleanToByte(boolean input) {
        if (input) {
            return (byte)1;
        }
        return 0;
    }

    public static byte[] shortToBytes(short input) {
        byte [] b = new byte[2];
        for(int i= 0; i < 2; i++){
            b[1 - i] = (byte)(input >>> (i * 8));
        }
        return b;
    }

    public static byte[] intToBytes(int input) {
        byte [] b = new byte[4];
        for(int i= 0; i < 4; i++){
            b[3 - i] = (byte)(input >>> (i * 8));
        }
        return b;
    }

    public static byte[] longToBytes(long input) {
        byte [] b = new byte[8];
        for(int i= 0; i < 8; i++){
            b[7 - i] = (byte)(input >>> (i * 8));
        }
        return b;
    }
}
