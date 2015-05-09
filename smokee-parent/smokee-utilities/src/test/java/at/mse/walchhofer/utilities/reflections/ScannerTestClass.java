package at.mse.walchhofer.utilities.reflections;

public class ScannerTestClass {
	
	@TestAnnotation
	public static String hugoStatic(String a, String b, ScannerTestClass cls, Long x, boolean istHugo) {
		return "";
	}
	@TestAnnotation
	public String hugo(String a, String b, ScannerTestClass cls, Long x, boolean istHugo) {
		return "";
	}
	
	@TestAnnotation
	public String hugo(String a, String b, ScannerTestClass cls, Long x, Boolean istHugo) {
		return "";
	}
	
	@TestAnnotation
	public String hugo(String a, String b, ScannerTestClass cls, Long x) {
		return "";
	}
	@TestAnnotation
	private String hugo(String a, ScannerTestClass cls, Long x) {
		return "";
	}
	@TestAnnotation
	private Long hugoLong(String a, ScannerTestClass cls, Long x) {
		return 0L;
	}
}
