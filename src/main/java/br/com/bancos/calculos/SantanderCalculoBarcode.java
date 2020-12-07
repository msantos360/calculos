package br.com.bancos.calculos;

import java.time.LocalDate;

public class SantanderCalculoBarcode extends BarcodeGenerics {

	private String barcode;

	public SantanderCalculoBarcode(LocalDate dataVencimento, String linhaDigitavel) {
		String linhaDigitavelSemFormatacao = new Convert().convertNumberFromString(linhaDigitavel);

		String identificacaoDoBanco = getIdentificacaoDoBanco(linhaDigitavelSemFormatacao);
		String codigoMoeda = getCodigoMoeda(linhaDigitavelSemFormatacao);
		String fatorDeVencimentoBoleto = getFatorDeVencimento(dataVencimento);
		String valorDoBoleto = getValorDoBoleto(linhaDigitavelSemFormatacao);
		String nove = getNove();
		String codigoCedentePadraoSantander = getCodigoCedentePadraoSantander(linhaDigitavelSemFormatacao);
		String nossoNumero = getNossoNumero(linhaDigitavelSemFormatacao);
		String zero = getZero();
		String carteira = getCarteira(linhaDigitavelSemFormatacao);
		
		
		StringBuilder barcodeCom43posicoes = new StringBuilder();

		barcodeCom43posicoes.append(identificacaoDoBanco);
		barcodeCom43posicoes.append(codigoMoeda);
		barcodeCom43posicoes.append(fatorDeVencimentoBoleto);
		barcodeCom43posicoes.append(valorDoBoleto);
		barcodeCom43posicoes.append(nove);
		barcodeCom43posicoes.append(codigoCedentePadraoSantander);
		barcodeCom43posicoes.append(nossoNumero);
		barcodeCom43posicoes.append(zero);
		barcodeCom43posicoes.append(carteira);
		
		String digitoVerificadorCodigoDeBarras = getDigitoVerificadorCodigoDeBarras(barcodeCom43posicoes.toString());

		this.barcode = getBarcodeCompleto(digitoVerificadorCodigoDeBarras,
				barcodeCom43posicoes.toString());
	}
	
	public String getBarcode() {
		return barcode;
	}
	
	private String getZero() {
		return "0";
	}

	private String getNossoNumero(String linhaDigitavel) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(linhaDigitavel.substring(13, 20));
		sb.append(linhaDigitavel.substring(21, 27));
		
		return sb.toString();
	}

	private String getCarteira(String linhaDigitavel) {
		
		return linhaDigitavel.substring(28, 31);
	}

	private String getCodigoCedentePadraoSantander(String linhaDigitavel) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(linhaDigitavel.substring(5, 9));
		sb.append(linhaDigitavel.substring(10, 13));
		
		return sb.toString();
	}

	private String getNove() {
		return "9";
	}

	@Override
	protected String getDigitoVerificadorCodigoDeBarras(String barcode) {

		int decrementador = 43;
		int multiplicador = 2;
		Integer acumulador = 0;

		for (int i = 0; i < barcode.length(); i++) {

			if (multiplicador > 9) {
				multiplicador = 2;
			}

			acumulador += Integer.parseInt(barcode.substring(decrementador - 1, decrementador)) * multiplicador;

			multiplicador += 1;
			decrementador -= 1;
		}

		Integer result = ((acumulador * 10) % 11);

		if (result == 0 || result == 1 || result == 10) {
			return "1";
		}

		if (TAMANHO_DV_CODIGO_DE_BARRAS > 1 || result.toString().length() > 1) {
			throw new IllegalArgumentException(
					"DV do códigoo de barras é maior que o determinado (1 dígito). - FEBRABAN");
		}

		return result.toString();
	}


}
