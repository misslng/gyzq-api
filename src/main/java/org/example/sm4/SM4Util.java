package org.example.sm4;

public class SM4Util {
    private static String cancelOrderId;
    public static boolean isCancelTrading;

    static {
    }

    public String decryptDataCBC(String arg2) {
        return this.decryptDataCBC(arg2, null, null);
    }

    public String decryptDataCBC(String arg2, String arg3, String arg4) {
        return this.decryptDataCBC(arg2, arg3, arg4, true);
    }

    public String decryptDataCBC(String arg4, String arg5, String arg6, boolean arg7) {
        try {
            SM4Context v0 = new SM4Context();
            v0.isPadding = true;
            v0.mode = 0;
            if (arg5 == null || (arg5.isEmpty())) {
                v0.key = new String(Base64.decode(v0.key, 2));
            } else if (arg7) {
                v0.key = new String(Base64.decode(arg5, 2));
            } else {
                v0.key = arg5;
            }

            if (arg6 == null || (arg6.isEmpty())) {
                v0.iv = new String(Base64.decode(v0.iv, 2));
            } else if (arg7) {
                v0.iv = new String(Base64.decode(arg6, 2));
            } else {
                v0.iv = arg6;
            }

            byte[] v5 = v0.key.getBytes();
            byte[] v6 = v0.iv.getBytes();
            SM4 v7 = new SM4();
            v7.sm4SetKeyDec(v0, v5);
            byte[] mock = {-116, -92, 52, 123, 52, -93, 66, -73, 112, 14, 81, 58, 40, 33, -92, 111, -22, 120, 119, -37, 72, 101, -38, 38, -62, 40, 120, 59, -43, -18, -73, -58, 111, -81, -16, -72, 50, -119, 120, -110, -33, -58, 40, 96, -45, -46, 114, -10, 108, 44, 119, 99, -5, -113, 111, 35, 26, 18, 76, -18, 69, 96, 26, 16};
            return !SM4Util.isCancelTrading || SM4Util.cancelOrderId == null ? new String(v7.sm4CryptCBC(v0, v6, Base64.decode(arg4, 2)), v0.charset).trim() : "{\"cljg\":[{\"code\":\"MP1B000000\",\"message\":\"成功\"}],\"htxx\":[{\"fhxx\":\"\",\"htxh\":\"\",\"wtxh\":\"" + SM4Util.cancelOrderId + "\"}]}";
        } catch (Exception v4) {
            v4.printStackTrace();
            return null;
        }
    }

    public String decryptDataCBCToBase64(String arg2) {
        return this.decryptDataCBCToBase64(arg2, null, null);
    }

    public String decryptDataCBCToBase64(String arg1, String arg2, String arg3) {
        String v1 = this.decryptDataCBC(arg1, arg2, arg3);
        if (v1 != null) {
            try {
                return Base64.encodeToString(v1.getBytes(new SM4Context().charset), 2);
            } catch (Exception v1_1) {
                v1_1.printStackTrace();
                return null;
            }
        }

        return null;
    }

    public String decryptDataECB(String arg2) {
        return this.decryptDataECB(arg2, null);
    }

    public String decryptDataECB(String arg4, String arg5) {
        try {
            SM4Context v0 = new SM4Context();
            v0.isPadding = true;
            v0.mode = 0;
            v0.key = arg5 != null && !arg5.isEmpty() ? new String(Base64.decode(arg5, 2)) : new String(Base64.decode(v0.key, 2));
            byte[] v5 = v0.key.getBytes();
            SM4 v2 = new SM4();
            v2.sm4SetKeyDec(v0, v5);
            return new String(v2.sm4CryptECB(v0, Base64.decode(arg4, 2)), v0.charset).trim();
        } catch (Exception v4) {
            v4.printStackTrace();
            return null;
        }
    }

    public String decryptDataECBToBase64(String arg2) {
        return this.decryptDataECBToBase64(arg2, null);
    }

    public String decryptDataECBToBase64(String arg2, String arg3) {
        String v2 = this.decryptDataECB(arg2, arg3);
        if (v2 != null) {
            try {
                return Base64.encodeToString(v2.getBytes(new SM4Context().charset), 2);
            } catch (Exception v2_1) {
                v2_1.printStackTrace();
                return null;
            }
        }

        return null;
    }

    public String decryptShareDataCBC(String arg3) {
        SM4Context v0 = new SM4Context();
        return this.decryptDataCBC(arg3, v0.shareKey, v0.shareIv);
    }

    public String encryptBunnerDataCBC(String arg3) {
        SM4Context v0 = new SM4Context();
        return this.encryptDataCBC(arg3, v0.bunnerKey, v0.bunnerIv);
    }

    public String encryptDataCBC(String arg2) {
        return this.encryptDataCBC(arg2, null, null);
    }

    public String encryptDataCBC(String arg2, String arg3, String arg4) {
        return this.encryptDataCBC(arg2, arg3, arg4, true);
    }

    public String encryptDataCBC(String arg4, String arg5, String arg6, boolean arg7) {
        String v5_1;
        try {
            SM4Context v0 = new SM4Context();
            v0.isPadding = true;
            v0.mode = 1;
            if (arg5 == null || (arg5.isEmpty())) {
                v0.key = new String(Base64.decode(v0.key, 2));
            } else if (arg7) {
                v0.key = new String(Base64.decode(arg5, 2));
            } else {
                v0.key = arg5;
            }

            if (arg6 == null || (arg6.isEmpty())) {
                v0.iv = new String(Base64.decode(v0.iv, 2));
            } else if (arg7) {
                v0.iv = new String(Base64.decode(arg6, 2));
            } else {
                v0.iv = arg6;
            }

            byte[] v5 = v0.key.getBytes();
            byte[] v6 = v0.iv.getBytes();
            SM4 v7 = new SM4();
            v7.sm4SetKeyEnc(v0, v5);
            v5_1 = Base64.encodeToString(v7.sm4CryptCBC(v0, v6, arg4.getBytes(v0.charset)), 2);
            if (v5_1 != null && v5_1.trim().length() > 0) {
                v5_1 = v5_1.replaceAll("\\s*|\t|\r|\n", "");
            }

            if (SM4Util.isCancelTrading) {
//                SM4Util.cancelOrderId = new JSONObject(arg4).optString("wtxh");
                return v5_1;
            }
        } catch (Exception v4) {
            v4.printStackTrace();
            return null;
        }

        return v5_1;
    }

    public String encryptDataECB(String arg2) {
        return this.encryptDataECB(arg2, null);
    }

    public String encryptDataECB(String arg4, String arg5) {
        String v4_1;
        try {
            SM4Context v0 = new SM4Context();
            v0.isPadding = true;
            v0.mode = 1;
            v0.key = arg5 != null && !arg5.isEmpty() ? new String(Base64.decode(arg5, 2)) : new String(Base64.decode(v0.key, 2));
            byte[] v5 = v0.key.getBytes();
            SM4 v2 = new SM4();
            v2.sm4SetKeyEnc(v0, v5);
            v4_1 = Base64.encodeToString(v2.sm4CryptECB(v0, arg4.getBytes(v0.charset)), 2);
            if (v4_1 != null && v4_1.trim().length() > 0) {
                return v4_1.replaceAll("\\s*|\t|\r|\n", "");
            }
        } catch (Exception v4) {
            v4.printStackTrace();
            return null;
        }

        return v4_1;
    }

    public String encryptShareDataCBC(String arg3) {
        SM4Context v0 = new SM4Context();
        return this.encryptDataCBC(arg3, v0.shareKey, v0.shareIv);
    }
}

