package ca.kanoa.localization;

public class LanguageNotFoundException extends Exception {

	private static final long serialVersionUID = -6791257712993113097L;
	
	public LanguageNotFoundException(String language) {
		super(language);
	}

}
