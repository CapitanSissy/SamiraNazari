package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;

public class MKComponent implements SimpleSerializable {
  @Serializable
  String attr;
  @Serializable(group = "G1")
  Element alpha;
  @Serializable(group = "G1")
  Element beta;
  @Serializable(group = "G1")
  Element v;
}
