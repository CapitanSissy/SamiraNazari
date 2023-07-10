package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;


public class SecretKey implements SimpleSerializable {

  @Serializable(group = "G1")
  Element GID;
  @Serializable
  SKComponent[] comps;

}
