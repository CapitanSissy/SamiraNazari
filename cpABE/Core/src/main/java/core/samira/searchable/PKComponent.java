package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;

public class PKComponent implements SimpleSerializable {
  @Serializable
  public Element r;
  @Serializable(group = "GT")
  public Element galphav;
  @Serializable(group = "GT")
  public Element gbeta;
  @Serializable
  String attr;
}
