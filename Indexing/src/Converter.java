import java.util.BitSet;


public class Converter {
	public static byte[] gammaCode(int number){
		String gammacode = getGammaCode(number);		
		return convertToByteArray(gammacode);
	}
	public static byte[] deltaCode(int number){
		String binaryRep = Integer.toBinaryString(number);
		String gammaCode = getGammaCode(binaryRep.length());
		String offset = binaryRep.substring(1);
		return convertToByteArray(gammaCode.concat(offset));
	}
	private static byte[] convertToByteArray(String gammacode) {
		BitSet bitSet = new BitSet(gammacode.length());
		for(int i = 0; i < gammacode.length(); i ++){
			Boolean value = gammacode.charAt(i) == '1' ? true : false;
			bitSet.set(i, value);
		}
		return bitSet.toByteArray();
	}
	private static String getGammaCode(int number) {
		String binaryRep = Integer.toBinaryString(number);
		String offset = binaryRep.substring(1);	
		String unaryValue = getUnaryValue(offset.length());	
		String gammacode =  unaryValue.concat("0").concat(offset);
		return gammacode;
	}
	private static String getUnaryValue(int lenght) {
		String unaryValue="";
		for(int i=0;i<lenght;i++){
			unaryValue=unaryValue.concat("1");
		}
		return unaryValue;
	}
}
