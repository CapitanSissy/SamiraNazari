package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;


public class OutSourceKey implements SimpleSerializable {
  // @Serializable(group="G2")
  // Element D;
//	@Serializable(group="G1")
//		Element h; 
  @Serializable(group = "G1")
  Element OPK2;
  @Serializable(group = "G1")
  Element OPK3;
  @Serializable(group = "G2")
  Element OSK;

  @Serializable
  OSKComponent[] comps;

}
