/**
 * <p>Title: RSAUtil.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.iipcloud.com</p>
 *
 * @author 肖晓霖
 * @date 2018年6月28日
 * @version 1.0
 */
package com.sliver.util;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;


/**
 * <p>Title: RSAUtil</p>
 * <p>Description: </p>
 * @author 肖晓霖
 * @date 2018年6月28日
 */
public class RSAUtil {
    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";


    public static Map<String, String> createKeys(int keySize) {
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize, new SecureRandom());
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }

    /**
     * 得到公钥
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 得到私钥
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//	            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
            return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static String publicEncrypt(String data, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return publicEncrypt(data, getPublicKey(publicKey));
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */

    public static String privateDecrypt(String data, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return privateDecrypt(data, getPrivateKey(privateKey));
    }

    /**
     * 私钥加密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
//	            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
            return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     * @param data
     * @param privateKey
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */

    public static String privateEncrypt(String data, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return privateEncrypt(data, getPrivateKey(privateKey));
    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     */

    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */

    public static String publicDecrypt(String data, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return publicDecrypt(data, getPublicKey(publicKey));
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }

    public static void main(String[] args) throws Exception {
        testPriEncry();
        testPubEncry();

        testOpenSSlKey();
    }

    /**
     * <p>Title: testOpenSSlKey</p>
     * <p>
     * 	Description: 测试ssl生成密钥的加密和及解密
     * 	生成的命令：openssl genrsa -out rsa_private_key.pem 1024 // pkcs#1编码的密钥，不支持
     * openssl pkcs8 -topk8 -in rsa_private_key.pem -out pkcs8_rsa_private_key.pem -nocrypt //// pkcs#8编码的密钥
     * openssl rsa -in rsa_private_key.pem -out rsa_public_key.pem -pubout // 公钥
     * </p>
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static void testOpenSSlKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("Open SSL 生成的密钥测试");
        String text = "this is test";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1apLvGwMVrNuYwvMrmUaROwuVeENvZrqB31ffMqhOL/5iHWaVwhVEsuG83eF5/WeAb9aM2HZCxZmRTFJyuAyreLklwLuyB4trruQGyUysDUKQzA0WWse3iY5kRQ2S0FmsawuK+WTbzGXsvPDRjpDvR/DfcB0j1hGvvvoR2doadwIDAQAB";
        // pkcs#1编码的密钥，本工具类不支持
//			String privateKey = "MIICXgIBAAKBgQC1apLvGwMVrNuYwvMrmUaROwuVeENvZrqB31ffMqhOL/5iHWaVwhVEsuG83eF5/WeAb9aM2HZCxZmRTFJyuAyreLklwLuyB4trruQGyUysDUKQzA0WWse3iY5kRQ2S0FmsawuK+WTbzGXsvPDRjpDvR/DfcB0j1hGvvvoR2doadwIDAQABAoGAGiMKnExF+391++JKaoOguRfKqXQ0Mv6X7FIjNNL1kJ41hik6OGVH7yfegfltnMe3e7ehL5WYJDKX12kpj8GYgXVuF2syxW6oQQUgfnRXZlqfuzr0eVK6ttKQ+ydhe4stjUvprioPkzN/bobYfnIhGl2B51C9Lw+8K4UHIhomcxECQQDujz1aD1ZN9mkN8Z1zq52id5nbah1pZa+2KdCBz0fKIEIoWm6G8YQjgGwafDtvRMLh1PcVlia36UJ/xLXxZsKVAkEAwq3e0oXzB8jAHEUCOoUqV8HbzvhsvJDk0L27ZFS3rMa280deZIWHL8sKJMlcTPfI4MKvEuGo4VZgTwZQij7R2wJBAJcbVBk/rcMkWdbjPICQLAFFEMfSp16jcnFIezI+QZLebAlzfp8rTC/QoKkGF/+a5nZ1lyytN0k3D1AvIbAPDyECQQCm//XkDCn1pH1AAuatgqxXCaJggnTssTLH6epFX/MMEI7CVJehjXXxRrBV7DH0iJ5WSEtc3B096BXH1Hfkj4z/AkEAkoNiXAvnHI+jKQo7Y8TokI3Z5YYz7gYEkpZ2OmFHrgQRaNiZdYS403aeVS/zQ8RXaJLyHtGkLbxRsCvgILRe2A==";
//			PKCS#8编码的密钥，
        String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALVqku8bAxWs25jC8yuZRpE7C5V4Q29muoHfV98yqE4v/mIdZpXCFUSy4bzd4Xn9Z4Bv1ozYdkLFmZFMUnK4DKt4uSXAu7IHi2uu5AbJTKwNQpDMDRZax7eJjmRFDZLQWaxrC4r5ZNvMZey88NGOkO9H8N9wHSPWEa+++hHZ2hp3AgMBAAECgYAaIwqcTEX7f3X74kpqg6C5F8qpdDQy/pfsUiM00vWQnjWGKTo4ZUfvJ96B+W2cx7d7t6EvlZgkMpfXaSmPwZiBdW4XazLFbqhBBSB+dFdmWp+7OvR5Urq20pD7J2F7iy2NS+muKg+TM39uhth+ciEaXYHnUL0vD7wrhQciGiZzEQJBAO6PPVoPVk32aQ3xnXOrnaJ3mdtqHWllr7Yp0IHPR8ogQihabobxhCOAbBp8O29EwuHU9xWWJrfpQn/EtfFmwpUCQQDCrd7ShfMHyMAcRQI6hSpXwdvO+Gy8kOTQvbtkVLesxrbzR15khYcvywokyVxM98jgwq8S4ajhVmBPBlCKPtHbAkEAlxtUGT+twyRZ1uM8gJAsAUUQx9KnXqNycUh7Mj5Bkt5sCXN+nytML9CgqQYX/5rmdnWXLK03STcPUC8hsA8PIQJBAKb/9eQMKfWkfUAC5q2CrFcJomCCdOyxMsfp6kVf8wwQjsJUl6GNdfFGsFXsMfSInlZIS1zcHT3oFcfUd+SPjP8CQQCSg2JcC+ccj6MpCjtjxOiQjdnlhjPuBgSSlnY6YUeuBBFo2Jl1hLjTdp5VL/NDxFdokvIe0aQtvFGwK+AgtF7Y";
        String encryptText = RSAUtil.publicEncrypt(text, publicKey);
        String decryptText = RSAUtil.privateDecrypt(encryptText, privateKey);
        System.out.println("加密前的数据:\n" + text);
        System.out.println("加密后的数据：\n" + encryptText);
        System.out.println("解密后的数据:\n" + decryptText);
        assert decryptText.equals(text);
    }

    private static void testPubEncry() throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("公钥加密——私钥解密");
        Map<String, String> keyMap = RSAUtil.createKeys(1024);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);
        String text = "加密测试";
        System.out.println("\r明文：\r\n" + text);
        System.out.println("\r明文大小：\r\n" + text.getBytes().length);
        String encodedData = RSAUtil.publicEncrypt(text, publicKey);
        System.out.println("密文：\r\n" + encodedData);
        System.out.println("密文长度：\r\n" + encodedData.length());
        String decodedData = RSAUtil.privateDecrypt(encodedData, RSAUtil.getPrivateKey(privateKey));
        System.out.println("解密后文字: \r\n" + decodedData);
        assert decodedData.equals(text);
    }

    private static void testPriEncry() throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("私钥加密——公钥解密");
        Map<String, String> keyMap = RSAUtil.createKeys(1024);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);
        String text = "加密测试";
        System.out.println("\r明文：\r\n" + text);
        System.out.println("\r明文大小：\r\n" + text.getBytes().length);
        String encodedData = RSAUtil.privateEncrypt(text, privateKey);
        System.out.println("密文：\r\n" + encodedData);
        System.out.println("密文长度：\r\n" + encodedData.length());
        String decodedData = RSAUtil.publicDecrypt(encodedData, publicKey);
        System.out.println("解密后文字: \r\n" + decodedData);
        assert decodedData.equals(text);
    }

}
