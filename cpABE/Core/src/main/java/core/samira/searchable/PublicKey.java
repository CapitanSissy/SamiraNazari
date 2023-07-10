package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;


public class PublicKey implements SimpleSerializable {
  @Serializable(group = "G1")
  public Element g;

  @Serializable(group = "GT")
  public Element gh;

  @Serializable(group = "GT")
  PKComponent[] comps;

}
