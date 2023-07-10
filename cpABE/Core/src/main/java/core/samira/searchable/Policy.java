package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;


public class Policy implements SimpleSerializable {
  /* serialized */
  @Serializable
  int k;            /* one if leaf, otherwise threshold */
  @Serializable
  String attr;       /* attribute string if leaf, otherwise null */
  @Serializable(group = "G1")
  Element Ci1;      /* G_1, only for leaves */
  @Serializable(group = "G1")
  Element Ci2;     /* G_1, only for leaves */
  @Serializable(group = "G1")
  Element Ci3;     /* G_1, only for leaves */

  //	List<Policy> children; /* pointers to bswabe_policy_t's, len == 0 for leaves */
  @Serializable
  Policy[] children;

  /* only used during encryption */
  transient Polynomial q;
  transient Polynomial w;

  /* only used during decryption */
  transient int satisfiable;
  transient int min_leaves;
  transient int attri;
  transient List<Integer> satl;
}