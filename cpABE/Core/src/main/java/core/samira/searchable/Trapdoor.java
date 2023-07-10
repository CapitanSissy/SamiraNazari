package core.samira.searchable;

import it.unisa.dia.gas.jpbc.Element;


public class Trapdoor implements SimpleSerializable {

//	 @Serializable(group="G2")
  // public Element gp; // G2 generator

  // @Serializable(group="GT")
  // public Element g_hat_alpha; // GT
  TRComponent[] comps;
  String keyword;

  public static class TRComponent implements SimpleSerializable {
    //		@Serializable(group="GT")
//		public Element galphav;
//		@Serializable(group="GT")
//		public Element gbeta;
    @Serializable(group = "G1")
    public Element t1; // G1 generator
    @Serializable(group = "GT")
    public Element t2;
    @Serializable(group = "GT")
    public Element t3;
    @Serializable
    String keyword;

  }

  // @Serializable(group="G1")
  // public Element h; //G1


}
