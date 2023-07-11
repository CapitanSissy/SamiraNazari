package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@SuppressWarnings("unused")
public class mysearch {


  public static boolean debug = false;
  static Pairing pairing = PairingFactory.getPairing("e.properties");
//  PairingFactory.getInstance().setUsePBCWhenPossible(true);
//  private static final Pairing pairing = PairingManager.defaultPairing;

  File ciphertextFile;
  private Element Element;

  public static @Nullable String createFile(String filename) {
    File file = new File(filename);
    try {
      if (file.createNewFile()) {
        return file.getPath();
      } else {
        System.out.println("file already exist");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static File createNewFile(String fileName) {
    ClassLoader classLoader = mysearch.class.getClassLoader();

    File file = new File(fileName);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      try {
        String path = file.getCanonicalPath();
        file.delete();
        file = new File(path);
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return file;
  }

  //*************************************************************************************
  @Contract(pure = true)
  public static byte @NotNull [] xor(byte @NotNull [] a, byte @NotNull [] b) {
    if (a.length != b.length) {
      throw new IllegalArgumentException("Array must have same length");
    }
    byte[] result = new byte[a.length];
    for (int i = 0; i < a.length; i++) {
      result[i] = (byte) (a[i] ^ b[i]);
    }
    return result;
  }

  //*****************************************************************************************/
  public static Element hashcode(@NotNull Element a) {

//		Element GID = pairing.getG1().newElement().setToRandom();
    byte[] g1Bytes = a.toCanonicalRepresentation();
    MessageDigest hasher = null;
    try {
      hasher = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    byte[] GIDBytes = hasher.digest(g1Bytes);
    Element aHash = pairing.getZr().newElementFromHash(GIDBytes, 0, GIDBytes.length);
    return aHash;
  }

  public static @NotNull Pedersen pedersen() {
    Pedersen PedersenKey = new Pedersen();
    Element g = pairing.getZr().newElement().setToRandom();
    PedersenKey.setG(g);
    PedersenKey.setMo(pairing.getZr().newElement().setToRandom());
    PedersenKey.setGmo(g.powZn(PedersenKey.getMo()));
    PedersenKey.setC(pairing.getZr().newElement().setToRandom());
    PedersenKey.setGc(g.powZn(PedersenKey.getC()));
    PedersenKey.setGama(pairing.getZr().newElement().setToRandom());
    PedersenKey.setGgama(g.powZn(PedersenKey.getGama()));
//    PedersenKey.g = g.powZn(g);
//    PedersenKey.mo = pairing.getZr().newElement().setToRandom();
//    PedersenKey.gmo = g.powZn(PedersenKey.mo);
//    PedersenKey.c = pairing.getZr().newElement().setToRandom();
//    PedersenKey.gc = g.powZn(PedersenKey.c);
//    PedersenKey.gama = pairing.getZr().newElement().setToRandom();
//    PedersenKey.ggama = g.powZn(PedersenKey.gama);
    return PedersenKey;
  }

  /*********************************************************************************************/
  @SuppressWarnings("unused")
  public static String @NotNull [] KeyGen(String[] attrs, String MKFileName, String PKFileName, String SKFileName) {
    if (MKFileName == null || MKFileName.trim().equals("")) {
      MKFileName = Constants.MKFileName;
    }
    if (PKFileName == null || PKFileName.trim().equals("")) {
      PKFileName = Constants.PKFileName;
    }
    if (SKFileName == null || SKFileName.trim().equals("")) {
      SKFileName = Constants.SKFileName;
    }

    File MKFile = createNewFile(MKFileName);
    File PKFile = createNewFile(PKFileName);
    File SKFile = createNewFile(SKFileName);

    MasterKey MK = new MasterKey();
    PublicKey PK = new PublicKey();
    SecretKey SK = new SecretKey();

    Element GID = pairing.getG1().newElement().setToRandom();
    Element GIDHash = hashcode(GID);

    /*private key */
    Element h = pairing.getZr().newElement().setToRandom();
    MK.h = h;

    Element g = pairing.getG1().newRandomElement().getImmutable();
    PK.g = g;
    Element gh = g.duplicate().powZn(h);
    PK.gh = gh;

    MK.comps = new MKComponent[attrs.length];
    PK.comps = new PKComponent[attrs.length];
    SK.comps = new SKComponent[attrs.length];

    for (int i = 0; i < attrs.length; i++) {
      Element a = pairing.getZr().newElement().setToRandom();
      Element b = pairing.getZr().newElement().setToRandom();
      Element vi = pairing.getZr().newElement().setToRandom();

      MK.comps[i] = new MKComponent();
      MK.comps[i].v = vi;
      MK.comps[i].alpha = a;
      MK.comps[i].beta = b;
      MK.comps[i].attr = attrs[i];

      Element galpha = g.duplicate().powZn(MK.comps[i].alpha);
      Element gv = g.duplicate().powZn(MK.comps[i].v);
      Element GIDpowbeta = GIDHash.powZn(MK.comps[i].beta);

      PK.comps[i] = new PKComponent();
      PK.comps[i].r = pairing.getZr().newElement().setToRandom();
      PK.comps[i].galphav = galpha.mul(gv);
      PK.comps[i].gbeta = g.duplicate().powZn(MK.comps[i].beta);
      PK.comps[i].attr = attrs[i];

      SK.comps[i] = new SKComponent();
      SK.comps[i].attr = attrs[i];
      SK.comps[i].secret = PK.comps[i].galphav.duplicate().mul(pairing.getG1().newElement());
    }


    byte[] b1 = SerializeUtils.convertToByteArray(PK);
    String pkey = Base64.getEncoder().encodeToString(b1);
    byte[] b2 = SerializeUtils.convertToByteArray(MK);
    String mkey = Base64.getEncoder().encodeToString(b2);
    byte[] b3 = SerializeUtils.convertToByteArray(SK);
    String secretkey = Base64.getEncoder().encodeToString(b3);

    System.out.println("[PK] " + pkey);
    System.out.println("[MK] " + mkey);
    System.out.println("[SK] " + secretkey);

    SerializeUtils.serialize(SK, SKFile);
    SerializeUtils.serialize(PK, PKFile);
    SerializeUtils.serialize(MK, MKFile);


    String[] keypairArr = new String[3];
    keypairArr[0] = pkey;
    keypairArr[1] = mkey;
    keypairArr[2] = secretkey;

    return keypairArr;


  }


  /**********************************************************************************************************/
  @SuppressWarnings("unused")
  public static @NotNull OutSourceKey UserKeyGen(String[] attrs, SecretKey SK, String OKFileName) {
    if (OKFileName == null || OKFileName.trim().equals("")) {
      OKFileName = "SecretKey";
    }
    File OKFile = createNewFile(OKFileName);
    OutSourceKey OK = new OutSourceKey();

    Element g = pairing.getG1().newRandomElement().getImmutable();
    Element X = pairing.getZr().newElement().setToRandom();
    Element GX = g.duplicate().powZn(X);
    Element mo = pairing.getZr().newElement().setToRandom();
    Element GMO = g.duplicate().powZn(mo);
    Element IPK = GX.div(GMO);
    Element ISK = X;

    /***outsource key****/
    OK.OSK = pairing.getZr().newElement().setToRandom();
    Element oskinvert = OK.OSK.invert();
    OK.OPK3 = g.duplicate().powZn(oskinvert);

    Element GID = pairing.getG1().newElement().setToRandom();
    Element GIDHash = hashcode(GID);

    OK.OPK2 = GIDHash.powZn(oskinvert);

    OK.comps = new OSKComponent[attrs.length];
    for (int i = 0; i < attrs.length; i++) {
      OK.comps[i] = new OSKComponent();
      OK.comps[i].OPKi = SK.comps[i].secret.powZn(oskinvert);
    }

//		byte[] b = SerializeUtils.convertToByteArray(OK);
//		String okey = Base64.getEncoder().encodeToString(b);
//		String[] OkeyArr = new String[1];
//		OkeyArr[0] = okey;
//		System.out.println("[PK] " + okey);
//		SerializeUtils.serialize(okey, OKFile);
    return OK;
  }

  /****************************************************************************************************************** */

  public static File enc(String fileInput, Policy p, PublicKey PK, String outputFileName) throws IOException {
    File ciphertextFile = createNewFile(outputFileName);
    Element m = PairingManager.defaultPairing.getGT().newRandomElement();
    if (debug) {
      System.out.println("in function enc() m:" + m);
    }
//		Element R = PairingManager.defaultPairing.getGT().newRandomElement();


    Element s = pairing.getZr().newElement().setToRandom();

    fill_policy(p, s, PK);

    Ciphertext ciphertext = new Ciphertext();
    ciphertext.p = p;

//    ciphertext.C = m.duplicate().mul(PK.g.duplicate().powZn(s));

    // here m.duplicate() is for the latter AES encryption also need to use m
		ciphertext.C = m.duplicate().mul(PK.gh.duplicate().powZn(s));
//		ciphertext.C = PK.h.duplicate().powZn(s);

    SerializeUtils.serialize(ciphertext, ciphertextFile);
    FileInputStream fis = null;
    FileOutputStream fos = null;
    try {
      fis = new FileInputStream(fileInput);
      fos = new FileOutputStream(ciphertextFile, true);
      AES.crypto(Cipher.ENCRYPT_MODE, fis, fos, m);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
      try {
        fis.close();
        fos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    fos.close();

    return ciphertextFile;
  }
/********************************************/

  /*********************************************************/
  public static void fill_policy(@NotNull Policy p, Element e, PublicKey PK) {
    int i;
    int size = p.children == null ? 0 : p.children.length;
    Element zero = pairing.getZr().newElement().setToZero();
    Element r, t, tw;
    p.q = rand_poly(p.k - 1, e);
    p.w = rand_poly2(p.k - 1, zero);

    if (p.children == null || size == 0) {
      PKComponent comp = PK.comps[p.attri];
//			OutSourceKey.OSKComponent comp = OK.comps[p.attri];

      Element sigma = PK.g.duplicate().powZn(p.q.coef.get(0));
      Element c3 = comp.galphav.duplicate().powZn(comp.r);
      p.Ci1 = c3.mul(sigma);


      Element w = PK.g.duplicate().powZn(p.w.coef.get(0));
      Element c2 = comp.gbeta.duplicate().powZn(comp.r);
      p.Ci2 = c2.mul(w);
      p.Ci3 = PK.g.duplicate().powZn(comp.r);

    } else {
      for (i = 0; i < size; i++) {
        r = pairing.getZr().newElement().set(i + 1);
        t = Polynomial.eval_poly(p.q, r);
        tw = Polynomial.eval_poly(p.w, r);
        fill_policy(p.children[i], t, PK);
        fill_policy(p.children[i], tw, PK);
      }
    }
  }

  /***********************************************************/

  public static @NotNull Polynomial rand_poly(int deg, @NotNull Element zero_val) {
    int i;
    Polynomial q = new Polynomial();
    q.deg = deg;
    q.coef = new ArrayList<Element>();
    q.c = new ArrayList<Element>();

    q.coef.add(zero_val.duplicate());
    q.c.add(zero_val.duplicate());
    for (i = 1; i < q.deg + 1; i++) {
      q.coef.add(pairing.getZr().newElement().setToRandom());
      q.c.add(pairing.getZr().newElement().setToRandom());
    }

    return q;
  }

  /**********************************************************/
  public static @NotNull Polynomial rand_poly2(int deg, @NotNull Element zero_val) {
    int i;
    Polynomial w = new Polynomial();
    w.deg = deg;
    w.coef = new ArrayList<Element>();
    w.c = new ArrayList<Element>();

    w.coef.add(zero_val.duplicate());
    w.c.add(zero_val.duplicate());
    for (i = 1; i < w.deg + 1; i++) {
      w.coef.add(pairing.getZr().newElement().setToRandom());
      w.c.add(pairing.getZr().newElement().setToRandom());
    }

    return w;
  }

  /********************************************************************************************************************** */

  public static @Nullable Element dec(@NotNull Ciphertext ciphertext, SecretKey SK, OutSourceKey OK) {
    check_sat(SK, ciphertext.p);
    if (ciphertext.p.satisfiable != 1) {
      System.err.println("SK does not satisfies the policy!");

      return null;
    }
    pick_sat_min_leaves(ciphertext.p, SK);
//		Element r = dec_flatten(ciphertext.p, SK);
//		Element m = ciphertext.Cs.mul(r);
//		r = pairing.pairing(ciphertext.C, SK.D);
//		r.invert();
//		m.mul(r);
    Element r = dec_flatten(ciphertext.p, SK, OK);
    Element R = ciphertext.C.div(r.powZn(OK.OSK));
    if (debug) {
      System.out.println("In function dec() m:" + R);
    }
    return R;
  }

  /**************************************************/
  private static void check_sat(SecretKey SK, @NotNull Policy p) {
    int i, l;
    int size = p.children == null ? 0 : p.children.length;
    p.satisfiable = 0;
    if (p.children == null || size == 0) {
      for (i = 0; i < SK.comps.length; i++) {
        if (SK.comps[i].attr.equals(p.attr)) {
          p.satisfiable = 1;
          p.attri = i;
          break;
        }
      }
    } else {
      for (i = 0; i < size; i++) {
        check_sat(SK, p.children[i]);
      }

      l = 0;
      for (i = 0; i < size; i++) {
        if (p.children[i].satisfiable == 1) {
          l++;
        }
      }
      if (l >= p.k) {
        p.satisfiable = 1;
      }
    }
  }

  /***********************************************/
  private static void pick_sat_min_leaves(@NotNull Policy p, SecretKey SK) {
    int i, k, l;
    int size = p.children == null ? 0 : p.children.length;
    Integer[] c;
    assert (p.satisfiable == 1);
    if (p.children == null || p.children.length == 0) {
      p.min_leaves = 1;
    } else {
      for (i = 0; i < p.children.length; i++) {
        if (p.children[i].satisfiable == 1) {
          pick_sat_min_leaves(p.children[i], SK);
        }
      }

      c = new Integer[p.children.length];
      for (i = 0; i < size; i++) {
        c[i] = i;
      }

      Arrays.sort(c, new PolicyInnerComparator(p));
      p.satl = new ArrayList<Integer>();
      p.min_leaves = 0;
      l = 0;
      for (i = 0; i < size && l < p.k; i++) {
        if (p.children[i].satisfiable == 1) {
          l++;
          p.min_leaves += p.children[i].min_leaves;
          k = c[i] + 1;
          p.satl.add(k);
        }
      }
      assert (l == p.k);
    }
  }

  /*********************************************/
  private static Element dec_flatten(Policy p, SecretKey SK, OutSourceKey OK) {
    Element r = pairing.getGT().newElement().setToOne();
    Element one = pairing.getZr().newElement().setToOne();
    dec_node_flatten(r, one, p, SK, OK);
    return r;
  }

  /******************************************/
  private static void dec_node_flatten(Element r, Element exp, @NotNull Policy p, SecretKey SK, OutSourceKey OK) {
    assert (p.satisfiable == 1);
    if (p.children == null || p.children.length == 0) {
      dec_leaf_flatten(r, exp, p, SK, OK);
    } else {
      dec_internal_flatten(r, exp, p, SK, OK);
    }
  }

  /***********************************************/
  private static void dec_leaf_flatten(@NotNull Element r, Element exp, @NotNull Policy p, SecretKey SK, @NotNull OutSourceKey OK) {
//		SecretKey.SKComponent comp = SK.comps[p.attri];

    OSKComponent comp = OK.comps[p.attri];

//		Element s = pairing.pairing(p.Cy, comp.Dj);
//		Element t = pairing.pairing(p._Cy, comp._Dj);
//    CurveElement pCi2 = (CurveElement) p.Ci2;
//    ZrElement convertedCi2 = pCi2.getField().newZr().set(pCi2.toBigInteger());
//    p.Ci2.mulZn(p.Ci2).mul(0);


    Element p1 = pairing.pairing(OK.OPK2, p.Ci2);
    Element p2 = pairing.pairing(OK.OPK3, p.Ci1);
    Element p3 = pairing.pairing(comp.OPKi, p.Ci3);
    p1.mul(p2);
    p3.invert();
    p3.mul(p1);
    p3.powZn(exp);
    r.mul(p3);
//		t.invert();
//		s.mul(t);
//		s.powZn(exp);
//		r.mul(s);
  }

  /***************************************************/
  private static void dec_internal_flatten(Element r, Element exp, @NotNull Policy p, SecretKey SK, OutSourceKey OK) {
    int i;
    Element t;
    Element expnew;
    Element zero = pairing.getZr().newElement().setToZero();

    for (i = 0; i < p.satl.size(); i++) {
      t = lagrange_coef(p.satl, p.satl.get(i), zero);
      expnew = exp.duplicate().mul(t); //duplicate
      dec_node_flatten(r, expnew, p.children[p.satl.get(i) - 1], SK, OK);
    }
  }

  /****************************************/
  public static Element lagrange_coef(@NotNull List<Integer> S, int i, Element x) {
    int j, k;
    Element r = pairing.getZr().newElement().setToOne();
    Element t;
    for (k = 0; k < S.size(); k++) {
      j = S.get(k);
      if (j == i) {
        continue;
      }
      t = x.duplicate().sub(pairing.getZr().newElement().set(j));
      r.mul(t);
      t.set(i - j).invert();
      r.mul(t);
    }

    return r;
  }

  /*******************************************************************************************************/

  public static @NotNull Index index(String @NotNull [] keyword, PublicKey PK, Pedersen pedersen) {

    Index I = new Index();
    I.comps = new Index.INComponent[keyword.length];

    for (int i = 0; i < keyword.length; i++) {
      Element t = pairing.getZr().newElement().setToRandom();
      I.comps[i].i1 = PK.g.duplicate().powZn(t);

//		Element GID = pairing.getG1().newElement().setToRandom();
      byte[] KeywordBytes = keyword[i].getBytes();
      MessageDigest hasher = null;
      try {
        hasher = MessageDigest.getInstance("SHA-512");
      } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      byte[] Bytes = hasher.digest(KeywordBytes);
      Element KeywordHash = pairing.getZr().newElementFromHash(Bytes, 0, Bytes.length);

      I.comps[i].i2 = pairing.pairing(pedersen.ggama.powZn(t), KeywordHash);
    }


//	byte[] b1 = SerializeUtils.convertToByteArray(I);
//	String strindex = Base64.getEncoder().encodeToString(b1);
//	byte[] b2 = SerializeUtils.convertToByteArray(i2)
//	String i2= Base64.getEncoder().encodeToString(b2);

//	System.out.println("[index] " + strindex);
//	System.out.println("[i2] " + i2);

//	SerializeUtils.serialize(pkey, PKFile);
//	SerializeUtils.serialize(mkey, MKFile);
//
//
//	String[] outindex = new String[1];
//	outindex[0] = strindex;
//	index[1] = i2;

    return I;
  }//index

  /*********************************************************************************************/
  public static @NotNull Trapdoor trapdoor(String keyword, @NotNull PublicKey PK, @NotNull Pedersen pedersen) {
//	File TrapdoorFile = createNewFile(TrFileName);

    //authentication key

    //generate trapdoor

    Trapdoor trapdoor = new Trapdoor();
    trapdoor.keyword = keyword;

    Element ta = pairing.getZr().newElement().setToRandom();

    int intkeyword = Integer.parseInt(keyword);
    trapdoor.comps[intkeyword].t2 = PK.g.powZn(ta);

    trapdoor.comps[intkeyword].t1 = pairing.pairing(pedersen.ggama, pedersen.gc.powZn(ta));


    byte[] KeywordBytes = keyword.getBytes();
    MessageDigest hasher = null;
    try {
      hasher = MessageDigest.getInstance("SHA-512");
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    byte[] Bytes = hasher.digest(KeywordBytes);
    trapdoor.comps[intkeyword].t3 = pairing.getZr().newElementFromHash(Bytes, 0, Bytes.length);
//
//
//	byte[] b1 = SerializeUtils.convertToByteArray(trapdoor);
//	String t1 = Base64.getEncoder().encodeToString(b1);
//
//
//	System.out.println("[T] " + t1);
//
//	String[] Trapdoor = new String[1];
//	Trapdoor[0] = t1;
//	Trapdoor[1] = t2;
//	Trapdoor[2] = t3;

    return trapdoor;
  }

  /**
   * @return
   *******************************************************************************************/
  public String searchalg(@NotNull Trapdoor trapdoor, @NotNull Index index, @NotNull Pedersen pedersen) {
    String searchkeyword = trapdoor.keyword;
    int intkeyword = Integer.parseInt(searchkeyword);
    Trapdoor.TRComponent comp = trapdoor.comps[intkeyword];
    Index.INComponent comps = index.comps[intkeyword];
//	OutSourceKey.OSKComponent comp = OK.comps[p.attri];
    Element fi = pairing.pairing(comp.t2.powZn(pedersen.c), pedersen.ggama);
    byte[] fibyte = fi.toBytes();
    byte[] KeywordBytes = searchkeyword.getBytes();
    MessageDigest hasher = null;
    try {
      hasher = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    byte[] Bytes = hasher.digest(KeywordBytes);
    Element hash = pairing.getZr().newElementFromHash(Bytes, 0, Bytes.length);
    Element x = hash.powZn(pedersen.gama);
    byte[] teta = xor(x.toBytes(), comp.t1.toBytes());

    byte[] elxor = xor(teta, fibyte);
    Element pair = null;
    int v = pair.setFromBytes(elxor);
    Element ele = pair.set(v);
    Element pairi1 = pairing.pairing(comps.i1, ele);

    if (pairi1 == comps.i2) {
      return comps.Addr;
    }
    return "Done";


  }

  /******************************************************/

  private static class PolicyInnerComparator implements Comparator<Integer> {
    Policy p;

    public PolicyInnerComparator(Policy p) {
      this.p = p;
    }

    @Contract(pure = true)
    public int compare(@NotNull Integer o1, @NotNull Integer o2) {
      int k, l;
      k = p.children[o1].min_leaves;
      l = p.children[o2].min_leaves;
      return k < l ? -1 : k == l ? 0 : 1;
    }

  }

}

