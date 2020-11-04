/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swings.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Utility class for encrypting/decrypting files.
 *
 * @author Taha BEN SALAH
 */
public class DataEncryptionHelper {

    private static byte[] MAGIC_ID = new byte[]{8, 2, 1, 9, 7, 7};
    public static final int AES_Key_Size = 128;
    Cipher pkCipher, aesCipher;
    //byte[] aesKey;
//    SecretKeySpec aeskeySpec;

    /**
     * Constructor: creates ciphers
     */
    public DataEncryptionHelper() throws GeneralSecurityException {
        // create RSA public key cipher
        pkCipher = Cipher.getInstance("RSA");
        // create AES shared key cipher
        aesCipher = Cipher.getInstance("AES");
    }

//    public static void main(String[] args) {
//        try {
//            DataEncryptionHelper secure = new DataEncryptionHelper();
////            File encryptedKeyFile = new File("encryptedKeyFile.key");
//            File publicKeyFile = new File("tt/public.der");
//            File privateKeyFile = new File("tt/private.der");
//            File fileToEncrypt = new File("build.xml");
//            File encryptedFile = new File("tt/encryptedFile.bin");
//            File unencryptedFile = new File("tt/build.xml");
//            secure.generateKeyPair(publicKeyFile, privateKeyFile);
//            secure.encode(fileToEncrypt, encryptedFile, publicKeyFile, privateKeyFile);
//            secure.decode(encryptedFile, unencryptedFile);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (GeneralSecurityException ex) {
//            ex.printStackTrace();
//        }
//    }
    public void generateKeyPair(File publicKeyFile, File privateKeyFile) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kpair = kpg.generateKeyPair();
        writeBytes(kpair.getPublic().getEncoded(), publicKeyFile);
        writeBytes(kpair.getPrivate().getEncoded(), privateKeyFile);
    }

    public void encodeObject(Object in, File out) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        FileOutputStream f=null;    
        try{
            f=new FileOutputStream(out);
            encodeObject(in, f);
        }finally{
            if(f!=null){
                f.close();
            }
        }
    }
    
    public void encodeObject(Object in, OutputStream out) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        PublicPrivateLey k = generateRSAKeys();
        encodeObject(in, out, k.pub, k.priv);
    }
    
    private PublicPrivateLey generateRSAKeys() throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kpair = kpg.generateKeyPair();
        PublicPrivateLey k=new PublicPrivateLey();
        k.pub = new ByteArrayInputStream(kpair.getPublic().getEncoded());
        k.priv = new ByteArrayInputStream(kpair.getPrivate().getEncoded());
        return k;
    }
    
    private static class PublicPrivateLey{
        InputStream pub ;
        InputStream priv ;
        
    }

    public void encodeObject(Object in, OutputStream out, File publicKeyFile, File privateKeyFile) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        FileInputStream publicKey = null;
        FileInputStream privateKey = null;
        try {
            publicKey = new FileInputStream(publicKeyFile);
            privateKey = new FileInputStream(privateKeyFile);
            encodeObject(in, out, publicKey, privateKey);
        } finally {
            if (publicKey != null) {
                publicKey.close();
            }
            if (privateKey != null) {
                privateKey.close();
            }
        }
    }

    public void encodeObject(Object in, OutputStream out, InputStream publicKey, InputStream privateKey) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        ByteArrayOutputStream bar = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bar);
        oos.writeObject(in);
        oos.flush();
        encode(new ByteArrayInputStream(bar.toByteArray()), out, publicKey, privateKey);
    }

    public void encodeBytes(byte[] in, OutputStream out, File publicKeyFile, File privateKeyFile) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        ByteArrayInputStream fin = null;
        FileInputStream publicKey = null;
        FileInputStream privateKey = null;
        try {
            fin = new ByteArrayInputStream(in);
            publicKey = new FileInputStream(publicKeyFile);
            privateKey = new FileInputStream(privateKeyFile);
            encode(fin, out, publicKey, privateKey);
        } finally {
            if (fin != null) {
                fin.close();
            }
            if (publicKey != null) {
                publicKey.close();
            }
            if (privateKey != null) {
                privateKey.close();
            }
        }
    }

    public void encodeFile(File in, File out, File publicKeyFile, File privateKeyFile) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        FileOutputStream fout = null;
        FileInputStream fin = null;
        FileInputStream publicKey = null;
        FileInputStream privateKey = null;
        try {
            fout = new FileOutputStream(out);
            fin = new FileInputStream(in);
            publicKey = new FileInputStream(publicKeyFile);
            privateKey = new FileInputStream(privateKeyFile);
            encode(fin, fout, publicKey, privateKey);
        } finally {
            if (fout != null) {
                fout.close();
            }
            if (fin != null) {
                fin.close();
            }
            if (publicKey != null) {
                publicKey.close();
            }
            if (privateKey != null) {
                privateKey.close();
            }
        }
    }

    public void encodeFile(File in, File out) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        FileOutputStream fout = null;
        FileInputStream fin = null;
        InputStream publicKey = null;
        InputStream privateKey = null;
        try {
            PublicPrivateLey k = generateRSAKeys();
            fout = new FileOutputStream(out);
            fin = new FileInputStream(in);
            publicKey = k.pub;
            privateKey = k.priv;
            encode(fin, fout, publicKey, privateKey);
        } finally {
            if (fout != null) {
                fout.close();
            }
            if (fin != null) {
                fin.close();
            }
            if (publicKey != null) {
                publicKey.close();
            }
            if (privateKey != null) {
                privateKey.close();
            }
        }
    }

    public void decodeFile(File in, File out) throws GeneralSecurityException, IOException {
        FileOutputStream fout = null;
        FileInputStream fin = null;
        try {
            fout = new FileOutputStream(out);
            fin = new FileInputStream(in);
            decode(fin, fout);
        } finally {
            if (fout != null) {
                fout.close();
            }
            if (fin != null) {
                fin.close();
            }
        }

    }

    public void encode(InputStream in, OutputStream out, InputStream publicKeyFile, InputStream privateKeyFile) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(AES_Key_Size);
        SecretKeySpec aeskeySpec = new SecretKeySpec(kgen.generateKey().getEncoded(), "AES");

        // read public key to be used to encrypt the AES key
        byte[] privateKey = readBytes(privateKeyFile);

        // write AES key
        pkCipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKey)));
        out.write(MAGIC_ID);
        byte[] publicKey = readBytes(publicKeyFile);
        out.write(ByteBuffer.allocate(4).putInt(publicKey.length).array());
        out.write(publicKey);

        ByteArrayOutputStream encryptedAesKeyBuffer = new ByteArrayOutputStream();
        CipherOutputStream os = null;
        try {
            os = new CipherOutputStream(encryptedAesKeyBuffer, pkCipher);
            os.write(aeskeySpec.getEncoded());
        } finally {
            if (os != null) {
                os.close();
            }
        }
        byte[] encryptedAesKey = encryptedAesKeyBuffer.toByteArray();
        out.write(ByteBuffer.allocate(4).putInt(encryptedAesKey.length).array());
        out.write(encryptedAesKey);

        aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);
        try {
            os = new CipherOutputStream(out, aesCipher);
            copy(in, os);
        } finally {
            if (os != null) {
                os.close();
            }
        }

    }

    public <T> T decodeObject(File in) throws GeneralSecurityException, IOException, ClassNotFoundException {
        FileInputStream f = null;
        try {
            f=new FileInputStream(in);
            return decodeObject(f);
        } finally {
            if (f != null) {
                f.close();
            }
        }
    }

    public <T> T decodeObject(InputStream in) throws GeneralSecurityException, IOException, ClassNotFoundException {
        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        decode(in, bb);
        return (T) new ObjectInputStream(new ByteArrayInputStream(bb.toByteArray())).readObject();
    }

    public void decode(InputStream in, OutputStream out) throws GeneralSecurityException, IOException {
        // read private key to be used to decrypt the AES key
        DataInputStream din = new DataInputStream(in);
        byte[] magic = new byte[MAGIC_ID.length];
        din.readFully(magic);
        if (!Arrays.equals(magic, MAGIC_ID)) {
            throw new IllegalArgumentException("Bad Format");
        }
        byte[] publicLenArr = new byte[4];
        din.readFully(publicLenArr);
        int publicLen = ByteBuffer.wrap(publicLenArr).getInt();
        byte[] publicKey = new byte[publicLen];
        din.readFully(publicKey);
        // create private key
        // read AES key
        pkCipher.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey)));
        byte[] aesLenArr = new byte[4];
        din.readFully(aesLenArr);
        int aesLen = ByteBuffer.wrap(aesLenArr).getInt();
        byte[] aesArr = new byte[aesLen];
        din.readFully(aesArr);

        CipherInputStream pkis = new CipherInputStream(new ByteArrayInputStream(aesArr), pkCipher);
        byte[] aesKey = new byte[AES_Key_Size / 8];
        pkis.read(aesKey);
        SecretKeySpec aeskeySpec = new SecretKeySpec(aesKey, "AES");
        aesCipher.init(Cipher.DECRYPT_MODE, aeskeySpec);
        pkis.close();

        CipherInputStream aesis = new CipherInputStream(in, aesCipher);
        copy(aesis, out);
        aesis.close();
    }

    /**
     * Copies a stream.
     */
    private void copy(InputStream is, OutputStream os) throws IOException {
        int i;
        byte[] b = new byte[1024];
        while ((i = is.read(b)) != -1) {
            os.write(b, 0, i);
        }
    }

    private void writeBytes(byte[] b, File file) throws IOException {
        FileOutputStream bos = null;
        try {
            bos = new FileOutputStream(file);
            bos.write(b);
        } finally {
            if (bos != null) {
                bos.close();
            }
        }
    }

    private byte[] readBytes(InputStream file) throws IOException {
        ByteArrayOutputStream dos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[2048];
            int r;
            while ((r = file.read(buffer)) > 0) {
                dos.write(buffer, 0, r);
            }
            return dos.toByteArray();
        } finally {
//            if (d != null) {
//                d.close();
//            }
        }
    }
}
