package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;

public class OSKComponent implements SimpleSerializable {
  @Serializable
  String attr;

  @Serializable(group = "G1")
  Element OPKi;

}