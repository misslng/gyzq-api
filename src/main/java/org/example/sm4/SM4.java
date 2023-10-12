package org.example.sm4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SM4 {
    private static final int[] CK =new int[]{0x70E15, 472066609, 0x383F464D, 0x545B6269, 0x70777E85, 0x8C939AA1, 0xA8AFB6BD, 0xC4CBD2D9, 0xE0E7EEF5, 0xFC030A11, 0x181F262D, 0x343B4249, 1347903077, 1819507329, -2003855715, 0xA4ABB2B9, -1060647211, 0xDCE3EAF1, 0xF8FF060D, 0x141B2229, 0x30373E45, 1280531041, 0x686F767D, -2071227751, 0xA0A7AEB5, 0xBCC3CAD1, 0xD8DFE6ED, 0xF4FB0209, 269950501, 0x2C333A41, 1213159005, 0x646B7279};
    private static final int[] FK = new int[]{-1548633402, 0x56AA3350, 0x677D9197, 0xB27022DC};
    public static final int SM4_DECRYPT = 0;
    public static final int SM4_ENCRYPT = 1;
    private static final byte[] SboxTable = new byte[]{-42, (byte)0x90, -23, -2, -52, (byte)0xE1, 61, -73, 22, -74, 20, -62, 40, -5, 44, 5, 43, 103, -102, 0x76, 42, -66, 4, -61, -86, 68, 19, 38, 73, (byte)0x86, 6, -103, -100, 66, 80, -12, (byte)0x91, -17, -104, 0x7A, 51, 84, 11, 67, -19, -49, -84, 98, -28, -77, 28, -87, -55, 8, -24, -107, (byte)0x80, -33, -108, -6, 0x75, (byte)0x8F, 0x3F, -90, 71, 7, -89, -4, -13, 0x73, 23, -70, (byte)0x83, 89, 60, 25, -26, (byte)0x85, 0x4F, -88, 104, 107, (byte)0x81, -78, 0x71, 100, -38, (byte)0x8B, -8, -21, 15, 75, 0x70, 86, -99, 53, 30, 36, 14, 94, 99, 88, (byte)0xD1, -94, 37, 34, 0x7C, 59, 1, 33, 120, (byte)0x87, -44, 0, 70, 87, -97, -45, 39, 82, 76, 54, 2, -25, (byte)0xA0, -60, -56, -98, -22, -65, (byte)0x8A, -46, 0x40, -57, 56, -75, -93, -9, -14, -50, -7, 97, 21, (byte)0xA1, (byte)0xE0, -82, 93, -92, -101, 52, 26, 85, -83, -109, 50, 0x30, -11, (byte)0x8C, (byte)0xB1, -29, 29, -10, -30, 46, (byte)0x82, 102, -54, 0x60, (byte)0xC0, 41, 35, -85, 13, 83, 78, 0x6F, -43, -37, 55, 69, -34, -3, (byte)0x8E, 0x2F, 3, -1, 106, 0x72, 109, 108, 91, 81, (byte)0x8D, 27, -81, -110, -69, -35, -68, 0x7F, 17, -39, 92, 65, 0x1F, 16, 90, -40, 10, (byte)0xC1, 49, -120, -91, -51, 0x7B, -67, 45, 0x74, (byte)0xD0, 18, -72, -27, -76, -80, (byte)0x89, 105, -105, 74, 12, -106, 0x77, 0x7E, 101, -71, -15, 9, -59, 110, -58, (byte)0x84, 24, -16, 0x7D, -20, 58, -36, 77, 0x20, 0x79, -18, 0x5F, 62, -41, -53, 57, 72};;

    private long ROTL(long arg3, int arg5) {
        return arg3 >> 0x20 - arg5 | this.SHL(arg3, arg5);
    }

    private long SHL(long arg3, int arg5) {
        return (arg3 & -1L) << arg5;
    }

    private void SWAP(long[] arg6, int arg7) {
        long v0 = arg6[arg7];
        int v2 = 0x1F - arg7;
        arg6[arg7] = arg6[v2];
        arg6[v2] = v0;
    }

    private long getLongFromByte(byte[] arg5, int arg6) {
        return ((long)(arg5[arg6 + 3] & 0xFF)) & 0xFFFFFFFFL | (((long)(arg5[arg6] & 0xFF)) << 24 | ((long)((arg5[arg6 + 1] & 0xFF) << 16)) | ((long)((arg5[arg6 + 2] & 0xFF) << 8)));
    }

    private byte[] padding(byte[] arg3, int arg4) {
        if(arg3 == null) {
            return null;
        }

        if(arg4 == 1) {
            int v4 = arg3.length % 16;
            byte[] v4_1 = v4 == 0 ? arg3 : new byte[arg3.length + (16 - v4)];
            System.arraycopy(arg3, 0, v4_1, 0, arg3.length);
            return v4_1;
        }

        return arg3;
    }

    private void putLongToByte(long arg7, byte[] arg9, int arg10) {
        arg9[arg10] = (byte)(((int)(arg7 >> 24 & 0xFFL)));
        arg9[arg10 + 1] = (byte)(((int)(arg7 >> 16 & 0xFFL)));
        arg9[arg10 + 2] = (byte)(((int)(arg7 >> 8 & 0xFFL)));
        arg9[arg10 + 3] = (byte)(((int)(arg7 & 0xFFL)));
    }

    private long sm4CalciRK(long arg4) {
        byte[] v1 = new byte[4];
        byte[] v0 = new byte[4];
        this.putLongToByte(arg4, v1, 0);
        v0[0] = this.sm4Sbox(v1[0]);
        v0[1] = this.sm4Sbox(v1[1]);
        v0[2] = this.sm4Sbox(v1[2]);
        v0[3] = this.sm4Sbox(v1[3]);
        long v4 = this.getLongFromByte(v0, 0);
        return this.ROTL(v4, 23) ^ (this.ROTL(v4, 13) ^ v4);
    }

    public byte[] sm4CryptCBC(SM4Context arg11, byte[] arg12, byte[] arg13) throws Exception {
        if(arg12 != null && arg12.length == 16) {
            if(arg13 != null) {
                if((arg11.isPadding) && arg11.mode == 1) {
                    arg13 = this.padding(arg13, 1);
                }

                int v0 = arg13.length;
                ByteArrayInputStream v3 = new ByteArrayInputStream(arg13);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                if(arg11.mode == 1) {
                    while(v0 > 0) {
                        byte[] v2 = new byte[16];
                        byte[] v4 = new byte[16];
                        byte[] v6 = new byte[16];
                        v3.read(v2);
                        int v7;
                        for(v7 = 0; v7 < 16; ++v7) {
                            v4[v7] = (byte)(v2[v7] ^ arg12[v7]);
                        }

                        this.sm4Round(arg11.sk, v4, v6);
                        System.arraycopy(v6, 0, arg12, 0, 16);
                        outputStream.write(v6);
                        v0 += -16;
                    }
                }
                else {
                    byte[] v2_1 = new byte[16];
                    while(v0 > 0) {
                        byte[] v4_1 = new byte[16];
                        byte[] v6_1 = new byte[16];
                        byte[] v7_1 = new byte[16];
                        v3.read(v4_1);
                        System.arraycopy(v4_1, 0, v2_1, 0, 16);
                        this.sm4Round(arg11.sk, v4_1, v6_1);
                        int v4_2;
                        for(v4_2 = 0; v4_2 < 16; ++v4_2) {
                            v7_1[v4_2] = (byte)(v6_1[v4_2] ^ arg12[v4_2]);
                        }

                        System.arraycopy(v2_1, 0, arg12, 0, 16);
                        outputStream.write(v7_1);
                        v0 += -16;
                    }
                }

                arg12 = outputStream.toByteArray();
                v3.close();
                outputStream.close();
                return arg12;
            }

            throw new Exception("input is null!");
        }

        throw new Exception("iv error!");
    }

    public byte[] sm4CryptECB(SM4Context arg6, byte[] arg7) throws Exception {
        if(arg7 != null) {
            if((arg6.isPadding) && arg6.mode == 1) {
                arg7 = this.padding(arg7, 1);
            }

            int v0 = arg7.length;
            ByteArrayInputStream v1 = new ByteArrayInputStream(arg7);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while(v0 > 0) {
                byte[] v3 = new byte[16];
                byte[] v2 = new byte[16];
                v1.read(v3);
                this.sm4Round(arg6.sk, v3, v2);
                outputStream.write(v2);
                v0 += -16;
            }

            byte[] byteArray = outputStream.toByteArray();
            v1.close();
            outputStream.close();
            return byteArray;
        }

        throw new Exception("input is null!");
    }

    private long sm4F(long arg1, long arg3, long arg5, long arg7, long arg9) {
        return arg1 ^ this.sm4Lt(arg3 ^ arg5 ^ arg7 ^ arg9);
    }

    private long sm4Lt(long arg5) {
        byte[] v1 = new byte[4];
        byte[] v0 = new byte[4];
        this.putLongToByte(arg5, v1, 0);
        v0[0] = this.sm4Sbox(v1[0]);
        v0[1] = this.sm4Sbox(v1[1]);
        v0[2] = this.sm4Sbox(v1[2]);
        v0[3] = this.sm4Sbox(v1[3]);
        long v0_1 = this.getLongFromByte(v0, 0);
        return this.ROTL(v0_1, 2) ^ v0_1 ^ this.ROTL(v0_1, 10) ^ this.ROTL(v0_1, 18) ^ this.ROTL(v0_1, 24);
    }

    private void sm4Round(long[] arg21, byte[] arg22, byte[] arg23) {
        long[] v13 = new long[36];
        v13[0] = this.getLongFromByte(arg22, 0);
        v13[1] = this.getLongFromByte(arg22, 4);
        v13[2] = this.getLongFromByte(arg22, 8);
        v13[3] = this.getLongFromByte(arg22, 12);
        int v0;
        int v17;
        for(v0 = 0; v0 < 0x20; v0 = v17) {
            v17 = v0 + 1;
            v13[v0 + 4] = this.sm4F(v13[v0], v13[v17], v13[v0 + 2], v13[v0 + 3], arg21[v0]);
        }

        this.putLongToByte(v13[35], arg23, 0);
        this.putLongToByte(v13[34], arg23, 4);
        this.putLongToByte(v13[33], arg23, 8);
        this.putLongToByte(v13[0x20], arg23, 12);
    }

    private byte sm4Sbox(byte arg2) {
        return SM4.SboxTable[arg2 & 0xFF];
    }

    private void sm4SetKey(long[] arg10, byte[] arg11) {
        long[] v1 = new long[4];
        long[] v2 = new long[36];
        int v3 = 0;
        v1[0] = this.getLongFromByte(arg11, 0);
        v1[1] = this.getLongFromByte(arg11, 4);
        v1[2] = this.getLongFromByte(arg11, 8);
        v1[3] = this.getLongFromByte(arg11, 12);
        v2[0] = v1[0] ^ ((long)SM4.FK[0]);
        v2[1] = v1[1] ^ ((long)SM4.FK[1]);
        v2[2] = v1[2] ^ ((long)SM4.FK[2]);
        v2[3] = v1[3] ^ ((long)SM4.FK[3]);
        while(v3 < 0x20) {
            int v11 = v3 + 4;
            int v4 = v3 + 1;
            v2[v11] = v2[v3] ^ this.sm4CalciRK(v2[v4] ^ v2[v3 + 2] ^ v2[v3 + 3] ^ ((long)SM4.CK[v3]));
            arg10[v3] = v2[v11];
            v3 = v4;
        }
    }

    public void sm4SetKeyDec(SM4Context arg4, byte[] arg5) throws Exception {
        if(arg4 != null) {
            if(arg5 != null && arg5.length == 16) {
                int v0 = 0;
                arg4.mode = 0;
                this.sm4SetKey(arg4.sk, arg5);
                while(v0 < 16) {
                    this.SWAP(arg4.sk, v0);
                    ++v0;
                }

                return;
            }

            throw new Exception("key error!");
        }

        throw new Exception("ctx is null!");
    }

    public void sm4SetKeyEnc(SM4Context arg3, byte[] arg4) throws Exception {
        if(arg3 != null) {
            if(arg4 != null && arg4.length == 16) {
                arg3.mode = 1;
                this.sm4SetKey(arg3.sk, arg4);
                return;
            }

            throw new Exception("key error!");
        }

        throw new Exception("ctx is null!");
    }
}

