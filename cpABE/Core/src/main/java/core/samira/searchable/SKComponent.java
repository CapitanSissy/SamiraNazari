package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;

public class SKComponent implements SimpleSerializable {
  @Serializable
  String attr;

  @Serializable(group = "G1")
  Element secret;
}