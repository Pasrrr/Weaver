package esteem.jun.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class Base64 {
    private static final int BASELENGTH = 255;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int FOURBYTE = 4;
    private static final int SIGN = -128;
    private static final byte PAD = 61;
    private static final boolean fDebug = false;
    private static byte[] base64Alphabet = new byte[255];
    private static byte[] lookUpBase64Alphabet = new byte[64];

    public Base64() {
    }

    protected static boolean isWhiteSpace(byte octect) {
        return octect == 32 || octect == 13 || octect == 10 || octect == 9;
    }

    protected static boolean isPad(byte octect) {
        return octect == 61;
    }

    protected static boolean isData(byte octect) {
        return base64Alphabet[octect] != -1;
    }

    public static boolean isBase64(String isValidString) {
        return isValidString == null ? false : isArrayByteBase64(isValidString.getBytes());
    }

    public static boolean isBase64(byte octect) {
        return isWhiteSpace(octect) || isPad(octect) || isData(octect);
    }

    public static synchronized byte[] removeWhiteSpace(byte[] data) {
        if (data == null) {
            return null;
        } else {
            int newSize = 0;
            int len = data.length;

            int i;
            for(i = 0; i < len; ++i) {
                if (!isWhiteSpace(data[i])) {
                    ++newSize;
                }
            }

            if (newSize == len) {
                return data;
            } else {
                byte[] arrayWithoutSpaces = new byte[newSize];
                int j = 0;

                for(i = 0; i < len; ++i) {
                    if (!isWhiteSpace(data[i])) {
                        arrayWithoutSpaces[j++] = data[i];
                    }
                }

                return arrayWithoutSpaces;
            }
        }
    }

    public static synchronized boolean isArrayByteBase64(byte[] arrayOctect) {
        return getDecodedDataLength(arrayOctect) >= 0;
    }

    public static synchronized byte[] encode(byte[] binaryData) {
        if (binaryData == null) {
            return null;
        } else {
            int lengthDataBits = binaryData.length * 8;
            int fewerThan24bits = lengthDataBits % 24;
            int numberTriplets = lengthDataBits / 24;
            byte[] encodedData;
            if (fewerThan24bits != 0) {
                encodedData = new byte[(numberTriplets + 1) * 4];
            } else {
                encodedData = new byte[numberTriplets * 4];
            }

            byte val1;
            byte val2;
            byte k;
            byte l;
            byte b1;
            byte b2;
            int encodedIndex;
            int dataIndex;
            int i;
            for(i = 0; i < numberTriplets; ++i) {
                dataIndex = i * 3;
                b1 = binaryData[dataIndex];
                b2 = binaryData[dataIndex + 1];
                byte b3 = binaryData[dataIndex + 2];
                l = (byte)(b2 & 15);
                k = (byte)(b1 & 3);
                encodedIndex = i * 4;
                val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
                val2 = (b2 & -128) == 0 ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 240);
                byte val3 = (b3 & -128) == 0 ? (byte)(b3 >> 6) : (byte)(b3 >> 6 ^ 252);
                encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
                encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | k << 4];
                encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2 | val3];
                encodedData[encodedIndex + 3] = lookUpBase64Alphabet[b3 & 63];
            }

            dataIndex = i * 3;
            encodedIndex = i * 4;
            if (fewerThan24bits == 8) {
                b1 = binaryData[dataIndex];
                k = (byte)(b1 & 3);
                val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
                encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
                encodedData[encodedIndex + 1] = lookUpBase64Alphabet[k << 4];
                encodedData[encodedIndex + 2] = 61;
                encodedData[encodedIndex + 3] = 61;
            } else if (fewerThan24bits == 16) {
                b1 = binaryData[dataIndex];
                b2 = binaryData[dataIndex + 1];
                l = (byte)(b2 & 15);
                k = (byte)(b1 & 3);
                val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
                val2 = (b2 & -128) == 0 ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 240);
                encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
                encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | k << 4];
                encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2];
                encodedData[encodedIndex + 3] = 61;
            }

            return encodedData;
        }
    }

    public static synchronized byte[] decode(byte[] base64Data) {
        if (base64Data == null) {
            return null;
        } else {
            byte[] normalizedBase64Data = removeWhiteSpace(base64Data);
            if (normalizedBase64Data.length % 4 != 0) {
                return null;
            } else {
                int numberQuadruple = normalizedBase64Data.length / 4;
                if (numberQuadruple == 0) {
                    return new byte[0];
                } else {
                    int i = 0;
                    int encodedIndex = 0;
                    int dataIndex = 0;

                    byte[] decodedData;
                    byte b1;
                    byte b2;
                    byte b3;
                    byte b4;
                    byte d1;
                    byte d2;
                    byte d3;
                    byte d4;
                    for(decodedData = new byte[numberQuadruple * 3]; i < numberQuadruple - 1; ++i) {
                        if (!isData(d1 = normalizedBase64Data[dataIndex++]) || !isData(d2 = normalizedBase64Data[dataIndex++]) || !isData(d3 = normalizedBase64Data[dataIndex++]) || !isData(d4 = normalizedBase64Data[dataIndex++])) {
                            return null;
                        }

                        b1 = base64Alphabet[d1];
                        b2 = base64Alphabet[d2];
                        b3 = base64Alphabet[d3];
                        b4 = base64Alphabet[d4];
                        decodedData[encodedIndex++] = (byte)(b1 << 2 | b2 >> 4);
                        decodedData[encodedIndex++] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                        decodedData[encodedIndex++] = (byte)(b3 << 6 | b4);
                    }

                    if (isData(d1 = normalizedBase64Data[dataIndex++]) && isData(d2 = normalizedBase64Data[dataIndex++])) {
                        b1 = base64Alphabet[d1];
                        b2 = base64Alphabet[d2];
                        d3 = normalizedBase64Data[dataIndex++];
                        d4 = normalizedBase64Data[dataIndex++];
                        if (isData(d3) && isData(d4)) {
                            b3 = base64Alphabet[d3];
                            b4 = base64Alphabet[d4];
                            decodedData[encodedIndex++] = (byte)(b1 << 2 | b2 >> 4);
                            decodedData[encodedIndex++] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                            decodedData[encodedIndex++] = (byte)(b3 << 6 | b4);
                            return decodedData;
                        } else {
                            byte[] tmp;
                            if (isPad(d3) && isPad(d4)) {
                                if ((b2 & 15) != 0) {
                                    return null;
                                } else {
                                    tmp = new byte[i * 3 + 1];
                                    System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                                    tmp[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
                                    return tmp;
                                }
                            } else if (!isPad(d3) && isPad(d4)) {
                                b3 = base64Alphabet[d3];
                                if ((b3 & 3) != 0) {
                                    return null;
                                } else {
                                    tmp = new byte[i * 3 + 2];
                                    System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                                    tmp[encodedIndex++] = (byte)(b1 << 2 | b2 >> 4);
                                    tmp[encodedIndex] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                                    return tmp;
                                }
                            } else {
                                return null;
                            }
                        }
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public static synchronized int getDecodedDataLength(byte[] base64Data) {
        if (base64Data == null) {
            return -1;
        } else if (base64Data.length == 0) {
            return 0;
        } else {
            byte[] decodedData;
            return (decodedData = decode(base64Data)) == null ? -1 : decodedData.length;
        }
    }

    public static synchronized String getEncodeString(String string) {
        byte[] oldby = string.getBytes();
        byte[] newby = encode(oldby);
        String newStr = new String(newby);
        return newStr;
    }

    public static synchronized String getEncodeString(byte[] oldby) {
        byte[] newby = encode(oldby);
        String newStr = new String(newby);
        return newStr;
    }

    public static synchronized String getDecodeString(String string) {
        byte[] oldby = string.getBytes();
        byte[] newby = decode(oldby);
        String newStr = new String(newby);
        return newStr;
    }

    public static synchronized byte[] getDecodeBytes(String string) {
        byte[] oldby = string.getBytes();
        byte[] newby = decode(oldby);
        return newby;
    }

    public String gZip(String str) throws IOException {
        String retStr = "";
        ByteArrayOutputStream bOs = new ByteArrayOutputStream();
        bOs.write(this.compress(str.getBytes()));
        retStr = new String(encode(bOs.toByteArray()));
        return retStr;
    }

    public String gUnZip(String str) throws IOException {
        byte[] byteArray = decode(str.getBytes());
        return new String(this.decompress(byteArray));
    }

    public byte[] compress(byte[] b) throws IOException {
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(outBuffer);
        gzip.write(b);
        gzip.close();
        byte[] c = outBuffer.toByteArray();
        outBuffer.reset();
        return c;
    }

    public byte[] decompress(byte[] b) throws IOException {
        try {
            ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
            ByteArrayInputStream inBuffer = new ByteArrayInputStream(b);
            GZIPInputStream gunzip = new GZIPInputStream(inBuffer);
            byte[] buffer = new byte[256];

            int n;
            while((n = gunzip.read(buffer)) >= 0) {
                outBuffer.write(buffer, 0, n);
            }

            return outBuffer.toByteArray();
        } catch (IOException var7) {
            var7.printStackTrace();
            throw var7;
        }
    }

    public static void main(String[] args) {
        System.out.println(getEncodeString("adcdefg"));
    }

    static {
        int i;
        for(i = 0; i < 255; ++i) {
            base64Alphabet[i] = -1;
        }

        for(i = 90; i >= 65; --i) {
            base64Alphabet[i] = (byte)(i - 65);
        }

        for(i = 122; i >= 97; --i) {
            base64Alphabet[i] = (byte)(i - 97 + 26);
        }

        for(i = 57; i >= 48; --i) {
            base64Alphabet[i] = (byte)(i - 48 + 52);
        }

        base64Alphabet[43] = 62;
        base64Alphabet[47] = 63;

        for(i = 0; i <= 25; ++i) {
            lookUpBase64Alphabet[i] = (byte)(65 + i);
        }

        i = 26;

        int j;
        for(j = 0; i <= 51; ++j) {
            lookUpBase64Alphabet[i] = (byte)(97 + j);
            ++i;
        }

        i = 52;

        for(j = 0; i <= 61; ++j) {
            lookUpBase64Alphabet[i] = (byte)(48 + j);
            ++i;
        }

        lookUpBase64Alphabet[62] = 43;
        lookUpBase64Alphabet[63] = 47;
    }
}

