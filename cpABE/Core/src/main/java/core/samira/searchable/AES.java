package core.samira.searchable;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;

import it.unisa.dia.gas.jpbc.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

//import searchable.PairingManager;

public class AES {
  public static void main(String[] args) throws IOException {
    File f = new File("README.md");
    File enc_out = new File("enc_out");
    if (!enc_out.exists()) {
      enc_out.createNewFile();
    }
    File dec_out = new File("dec_out");
    if (!dec_out.exists()) {
      dec_out.createNewFile();
    }
    Element e = PairingManager.defaultPairing.getGT().newRandomElement();
    InputStream is = new FileInputStream(f);
    OutputStream os = new FileOutputStream(enc_out);
    crypto(Cipher.ENCRYPT_MODE, is, os, e);
    is.close();
    os.close();
    is = new FileInputStream(enc_out);
    os = new FileOutputStream(dec_out);
    crypto(Cipher.DECRYPT_MODE, is, os, e);
  }

  public static void crypto(int mode, @NotNull InputStream is, OutputStream os, Element e) {
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      SecretKey secKey = generateSecretKeyFromElement(e);
      cipher.init(mode, secKey);

      CipherOutputStream cos = new CipherOutputStream(os, cipher);
      byte[] block = new byte[8];
      int i;
      while ((i = is.read(block)) != -1) {
        cos.write(block, 0, i);
      }
      cos.close();
    } catch (NoSuchAlgorithmException e1) {
      e1.printStackTrace();
    } catch (NoSuchPaddingException e1) {
      e1.printStackTrace();
    } catch (InvalidKeyException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  @Contract("_ -> new")
  private static @NotNull SecretKey generateSecretKeyFromElement(@NotNull Element e) {
//    System.out.println("e:" + e);
    byte[] b = e.toBytes();
    b = Arrays.copyOf(b, 16);
    return new SecretKeySpec(b, "AES");
  }
}
