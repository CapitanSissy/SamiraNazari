package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;
//import searchable.Serializable;
//import searchable.SimpleSerializable;

public class Ciphertext implements SimpleSerializable {
  @Serializable
  Policy p;
  @Serializable(group = "GT")
  Element C0; //GT
  @Serializable(group = "G1")
  Element C;  //G1
}

