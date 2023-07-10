package core.samira.searchable;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import javax.crypto.Cipher;
import java.io.*;


@SuppressWarnings("unused")
public class CPABE {
  public static final String Default_PKFileName = "PublicKey";
  public static final String Default_MKFileName = "MasterKey";
  public static final String Default_SKFileName = "SecretKey";
  public static final String Ciphertext_Suffix = ".cpabe";
  public static final String Error_PK_Missing = "Must set the name of the file that holds the PublicKey!";
  public static final String Error_MK_Missing = "Must set the name of the file that holds the MasterKey!";
  public static final String Error_SK_Missing = "Must set the name of the file that holds the SecretKey!";
  public static final String Error_OK_Missing = "Must set the name of the file that holds the OutSourceKey!";
  public static final String Error_EncFile_Missing = "Must set the file to be encrypted!";
  public static final String Error_Policy_Missing = "Must set a policy for the file to be encrypted!";
  public static final String Error_Attributes_Missing = "Must set the attributes of the key to be generated!";
  public static final String Error_Ciphertext_Missing = "Must set the name of the file that to be decrypted!";
  public static final String Error_Enc_Directory = "Can not encrypt a directory!";

  @SuppressWarnings("unused")
  public static void main(String[] args) throws IOException {
//		mysearch.debug = true;

    File ciphertextFile;
    System.out.println("setup");
    setup(Constants.attrs, Constants.MKFileName, Constants.PKFileName, Constants.SKFileName);
    System.out.println("encryption");
//    ciphertextFile = enc(encFileName, policy, PKFileName, ciphertextFileName);
    System.out.println("userkeygenerate");
    UserKeyGen(Constants.attrs, Constants.SKFileName, Constants.OKFileName);
    System.out.println("decryption");
    dec(Constants.ciphertextFileName, Constants.SKFileName, Constants.OKFileName);
//    dec(ciphertextFile.getName(), SKFileName, OKFileName);
    System.out.println("index");
    SerializeUtils.serialize(mysearch.pedersen(), new File(Constants.PedersenFileName));
    index(Constants.keywords, Constants.PKFileName, Constants.PedersenFileName);
    System.out.println("trapdoor");
    trapdoor(Constants.keyword, Constants.PKFileName, Constants.PedersenFileName);
    System.out.println("search");
  }

  private static void err(String s) {
    System.err.println(s);
  }

  private static boolean isEmptyString(String s) {
    return s == null || s.trim().equals("");
  }


  public static String[] setup(String[] attrs, String MKFileName, String PKFileName, String SKFileName) {
    PKFileName = isEmptyString(PKFileName) ? Default_PKFileName : PKFileName;
    MKFileName = isEmptyString(MKFileName) ? Default_MKFileName : MKFileName;
    SKFileName = isEmptyString(SKFileName) ? Default_SKFileName : SKFileName;
    String[] keypairArr = new String[3];
    keypairArr = mysearch.KeyGen(attrs, MKFileName, PKFileName, SKFileName);
    return keypairArr;
  }

  public static File enc(String encFileName, String policy, String PKFileName, String outputFileName) throws IOException {
    Pairing pairing = PairingFactory.getPairing("e.properties");
    Element g = pairing.getG1().newRandomElement().getImmutable();

    if (isEmptyString(encFileName)) {
      err(Error_EncFile_Missing);
      return null;
    }
    File encFile = new File(encFileName);
    if (!encFile.exists()) {
      err(Error_EncFile_Missing);
      return null;
    }
    if (encFile.isDirectory()) {
      err(Error_Enc_Directory);
      return null;
    }
    try {
      outputFileName = isEmptyString(outputFileName) ? encFile.getCanonicalPath() + Ciphertext_Suffix : outputFileName;
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (isEmptyString(policy)) {
      err(Error_Policy_Missing);
      return null;
    }
    if (isEmptyString(PKFileName)) {
      err(Error_PK_Missing);
      return null;
    }
    PublicKey PK = SerializeUtils.unserialize(PublicKey.class, new File(PKFileName));

    MasterKey MK = new MasterKey();
    PK.comps = new PKComponent[1];
    MK.comps = new MKComponent[1];
    Element a = pairing.getZr().newElement().setToRandom();
    Element b = pairing.getZr().newElement().setToRandom();
    Element vi = pairing.getZr().newElement().setToRandom();


    MK.comps[0] = new MKComponent();
    MK.comps[0].v = vi;
    MK.comps[0].alpha = a;
    MK.comps[0].beta = b;

    Element galpha = g.duplicate().powZn(MK.comps[0].alpha);
    Element gv = g.duplicate().powZn(MK.comps[0].v);

    PK.comps[0] = new PKComponent();
    PK.comps[0] = new PKComponent();
    PK.comps[0].r = pairing.getZr().newElement().setToRandom();
    PK.comps[0].galphav = galpha.mul(gv);
    PK.comps[0].gbeta = g.duplicate().powZn(MK.comps[0].beta);


    if (PK == null) {
      err(Error_PK_Missing);
      return null;
    }
    Parser parser = new Parser();
    Policy p = parser.parse(policy);
    if (p == null) {
      err(Error_Policy_Missing);
      return null;
    }
    File ciphertextFile = mysearch.enc(encFileName, p, PK, outputFileName);
    return ciphertextFile;
  }


  public static OutSourceKey UserKeyGen(String[] attrs, String SKFileName, String OKFileName) {
    if (attrs == null || attrs.length == 0) {
      err(Error_Attributes_Missing);
      return null;
    }
    if (isEmptyString(OKFileName)) {
      err(Error_OK_Missing);
      return null;
    }

    SKFileName = isEmptyString(SKFileName) ? Default_SKFileName : SKFileName;
    SecretKey SK = SerializeUtils.unserialize(SecretKey.class, new File(SKFileName));
    if (SK == null) {
      err(Error_PK_Missing);
      return null;
    }
//		MasterKey MK = SerializeUtils.unserialize(MasterKey.class, new File(MKFileName));
    OutSourceKey OK = new OutSourceKey();
    OK = mysearch.UserKeyGen(attrs, SK, OKFileName);

    return OK;
  }

  public static boolean dec(String ciphertextFileName, String SKFileName, String OKFileName) throws IOException {
    if (isEmptyString(ciphertextFileName)) {
      err(Error_Ciphertext_Missing);
      return false;
    }
    if (isEmptyString(SKFileName)) {
      err(Error_PK_Missing);
      return false;
    }
    if (isEmptyString(OKFileName)) {
      err(Error_SK_Missing);
      return false;
    }

    DataInputStream dis = null;
    try {
      dis = new DataInputStream(new FileInputStream(new File(ciphertextFileName)));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    Ciphertext ciphertext = SerializeUtils._unserialize(Ciphertext.class, dis);
    if (ciphertext == null) {
      err(Error_Ciphertext_Missing);
      return false;
    }
    SecretKey SK = SerializeUtils.unserialize(SecretKey.class, new File(SKFileName));
    if (SK == null) {
      err(Error_PK_Missing);
      return false;
    }
    OutSourceKey OK = SerializeUtils.unserialize(OutSourceKey.class, new File(OKFileName));
    OK = mysearch.UserKeyGen(new String[]{"Rajas", "mail", "KA", "India"}, SK, OKFileName);
    if (OK == null) {
      err(Error_SK_Missing);
      return false;
    }

//		String output = null;
//		if (ciphertextFileName.endsWith(".cpabe")) {
//			int end = ciphertextFileName.indexOf(".cpabe");
//			output = ciphertextFileName.substring(0, end);
//		} else {
//			output = ciphertextFileName + ".out";
//		}

    String output = ciphertextFileName + "dec";
    File outputFile = mysearch.createNewFile(output);
    OutputStream os = null;
    try {
      os = new FileOutputStream(outputFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Element m = mysearch.dec(ciphertext, SK, OK);
//    System.out.println(m);
    if (null == m || m.isEqual(null)) {
      System.out.println("pls work");
      os.close();
      dis.close();
      return false;
    }
    try {
      AES.crypto(Cipher.DECRYPT_MODE, dis, os, m);
    } catch (NullPointerException npe) {
      os.close();
      dis.close();
      return false;
    }
    os.close();
    dis.close();
    System.out.println("returning true");
    return true;
  }

  public static Index index(String[] keywords, String PKFileName, String PedersenFileName) {
//    PKFileName = isEmptyString(PKFileName) ? Default_PKFileName : PKFileName;
//    PedersenFileName = isEmptyString(PedersenFileName) ? Default_MKFileName : PedersenFileName;
//		String[] indexArr = new String[2];

//    PublicKey PK = SerializeUtils.unserialize(PublicKey.class, new File(PKFileName));
//    if (PK == null) {
//      err(Error_PK_Missing);
//      return null;
//    }
    Pedersen Pedersen = SerializeUtils.unserialize(Pedersen.class, new File(PedersenFileName));
    if (Pedersen == null) {
      err(Error_PK_Missing);
      return null;
    }
//    Index indexArr = mysearch.index(keywords, PK, Pedersen);
//    return indexArr;
    return null;
  }

  public static Trapdoor trapdoor(String keyword, String PKFileName, String PedersenFileName) {
    PKFileName = isEmptyString(PKFileName) ? Default_PKFileName : PKFileName;
    PedersenFileName = isEmptyString(PedersenFileName) ? Default_MKFileName : PedersenFileName;
//		String[] indexArr = new String[2];
    PublicKey PK = SerializeUtils.unserialize(PublicKey.class, new File(PKFileName));
    if (PK == null) {
      err(Error_PK_Missing);
      return null;
    }
    Pedersen Pedersen = SerializeUtils.unserialize(Pedersen.class, new File(PedersenFileName));
    if (Pedersen == null) {
      err(Error_PK_Missing);
      return null;
    }
    Trapdoor trapdoor = mysearch.trapdoor(keyword, PK, Pedersen);
    return trapdoor;
  }

}
