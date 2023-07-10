package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;


public class MasterKey implements SimpleSerializable {
  @Serializable(group = "G1")
  Element h;
  @Serializable
  MKComponent[] comps;

}
