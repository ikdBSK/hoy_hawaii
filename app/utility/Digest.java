package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 文字列のダイジェスト化
 */
class Digest {

    public static final String DEFAULT_ALGORITHM = "SHA-256";

    private final String algorithm;
    private final String salt;

    /**
     * デフォルトアルゴリズムを使用するダイジェスト化
     * @param salt ダイジェスト化に使用するsalt
     */
    public Digest(final String salt) {
        this(DEFAULT_ALGORITHM, salt);
    }

    /**
     * アルゴリズムを指定してダイジェスト化
     * @param algorithm 使用するアルゴリズム
     * @param salt ダイジェスト化に使用するsalt
     */
    public Digest(final String algorithm, final String salt) {
        this.algorithm = algorithm;
        this.salt = salt;
    }

    /**
     * 与えられた文字列をダイジェスト化する
     * @param message ダイジェスト化したい文字列
     * @return ダイジェスト化された文字列
     * @throws NoSuchAlgorithmException ダイジェスト化に使用するアルゴリズムが不正
     */
    public String toDigestString(final String message) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        final byte[] bytes = (message + salt).getBytes();
        messageDigest.update(bytes, 0, bytes.length);
        return bytesToHexString(messageDigest.digest());
    }

    /**
     * バイト列を16進数の文字列に変換
     * @param bytes 変換するバイト列
     * @return 変換後の16進数文字列
     */
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(final byte b : bytes) {
            final String hexString = Integer.toHexString(0xff & b);
            sb.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return sb.toString();
    }
}