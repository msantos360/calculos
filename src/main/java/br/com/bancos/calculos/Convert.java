package br.com.bancos.calculos;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Convert {
	
	private String regex = "\\d+";

	public LocalDate convertToLocalDate(String data) {

		String[] split = data.split(" ");

		String[] split2 = split[0].split("-");

		return LocalDate.of(Integer.parseInt(split2[0]), Integer.parseInt(split2[1]), Integer.parseInt(split2[2]));
	}
	
	public String convertNumberFromString(String text) {
		
		String linha = "";

		if (text != null) {
			Matcher matcher = Pattern.compile(this.regex).matcher(text);
			
			while(matcher.find()) {
				linha += matcher.group();
			}
		}
		return linha;
	}
	
}
