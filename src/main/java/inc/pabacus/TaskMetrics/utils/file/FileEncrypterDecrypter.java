package inc.pabacus.TaskMetrics.utils.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

// TODO add logging
public class FileEncrypterDecrypter {

  private static Logger logger = LoggerFactory.getLogger(FileEncrypterDecrypter.class);

  private SecretKey secretKey;
  private Cipher cipher;

  public FileEncrypterDecrypter(SecretKey secretKey, String transformation) throws NoSuchPaddingException, NoSuchAlgorithmException {
    this.secretKey = secretKey;
    this.cipher = Cipher.getInstance(transformation);
  }

  public static FileEncrypterDecrypter defaultInstance() {
    return createDefaultFileEncrypterDecrypter();
  }

  public void encrypt(String content, String fileName) throws InvalidKeyException {
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] i = cipher.getIV();

    try (
        FileOutputStream fileOut = new FileOutputStream(fileName);
        CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)
    ) {
      fileOut.write(i);
      cipherOut.write(content.getBytes());
    } catch (IOException e) {
      logger.error("Encountered problem with encryption. Error: " + e.getMessage());
    }
  }

  public String decrypt(String fileName) {
    String content = "";

    try (FileInputStream fileIn = new FileInputStream(fileName)) {
      byte[] fileIv = new byte[16];
      fileIn.read(fileIv);
      cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));

      try (
          CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
          InputStreamReader inputReader = new InputStreamReader(cipherIn);
          BufferedReader reader = new BufferedReader(inputReader)
      ) {
        content = retrieveDecryptedContent(reader);
      }

    } catch (IOException | InvalidAlgorithmParameterException | InvalidKeyException e) {
      logger.error("Encountered problem while decryption. Error: " + e.getMessage());
    }
    return content;
  }

  private String retrieveDecryptedContent(BufferedReader reader) throws IOException {
    StringBuilder sb = readDecryptedContent(reader);
    return sb.toString();
  }

  private StringBuilder readDecryptedContent(BufferedReader reader) throws IOException {
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null)
      sb.append(line);
    return sb;
  }

  private static FileEncrypterDecrypter createDefaultFileEncrypterDecrypter() {
    byte[] key;
    FileEncrypterDecrypter fileEncrypterDecrypter = null;
    try {
      // should probably use something more proper like current class name but meh
      key = createAesKey();
      SecretKey secretKey = new SecretKeySpec(key, "AES");
      String transformation = "AES/CBC/PKCS5Padding";
      fileEncrypterDecrypter = new FileEncrypterDecrypter(secretKey, transformation);
    } catch (NoSuchPaddingException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
      logger.error("Encountered problem while creating default FileEncrypterDecrypter. " +
                       "Error: " + e.getMessage());
    }
    return fileEncrypterDecrypter;
  }

  private static byte[] createAesKey() throws UnsupportedEncodingException, NoSuchAlgorithmException {
    byte[] key;
    String string = retrieveKeyString();
    key = string.getBytes("UTF-8");
    MessageDigest sha;
    sha = MessageDigest.getInstance("SHA-1");
    key = sha.digest(key);
    key = Arrays.copyOf(key, 16);
    return key;
  }

  private static String retrieveKeyString() {
    return "I" + "am" + "so" + "stressed" + "haha";
  }
}
