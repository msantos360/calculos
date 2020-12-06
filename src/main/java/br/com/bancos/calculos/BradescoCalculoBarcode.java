package br.com.bancos.calculos;

import java.time.LocalDate;

import org.apache.commons.lang.StringUtils;

public class BradescoCalculoBarcode extends BarcodeGenerics {

	private String barcodeCom44posicoes;

	public BradescoCalculoBarcode(String linhaDigitavel, LocalDate dataVencimentoBoleto, String agenciaBeneficiaria,
			String nossoNumero, String conta) {
		String linhaDigitavelSemFormatacao = new Convert().convertNumberFromString(linhaDigitavel);

		String identificacaoDoBanco = getIdentificacaoDoBanco(linhaDigitavelSemFormatacao);
		String codigoMoeda = getCodigoMoeda(linhaDigitavelSemFormatacao);

		String fatorDeVencimentoBoleto = getFatorDeVencimento(dataVencimentoBoleto);

		String valorDoBoleto = getValorDoBoleto(linhaDigitavelSemFormatacao);
		String agencia = getAgenciaBeneficiariaSemDigito(agenciaBeneficiaria);
		String carteira = getCarteira(linhaDigitavelSemFormatacao);
		String ns = getNossoNumero(nossoNumero);
		String contaBeneficiario = getContaBeneficiarioSemDigito(conta);
		String zero = getZero();

		StringBuilder barcodeCom43posicoes = new StringBuilder();

		barcodeCom43posicoes.append(identificacaoDoBanco);
		barcodeCom43posicoes.append(codigoMoeda);
		barcodeCom43posicoes.append(fatorDeVencimentoBoleto);
		barcodeCom43posicoes.append(valorDoBoleto);
		barcodeCom43posicoes.append(agencia);
		barcodeCom43posicoes.append(carteira);
		barcodeCom43posicoes.append(ns);
		barcodeCom43posicoes.append(contaBeneficiario);
		barcodeCom43posicoes.append(zero);

		String digitoVerificadorCodigoDeBarras = getDigitoVerificadorCodigoDeBarras(barcodeCom43posicoes.toString());

		this.barcodeCom44posicoes = getBarcodeCompleto(digitoVerificadorCodigoDeBarras,
				barcodeCom43posicoes.toString());
	}

	public String getBarcodeCom44posicoes() {
		return barcodeCom44posicoes;
	}

	@Override
	protected String getDigitoVerificadorCodigoDeBarras(String barcode) {

		int decrementador = 43;
		int multiplicador = 2;
		int acumulador = 0;

		for (int i = 0; i < barcode.length(); i++) {
			acumulador += Integer.parseInt(barcode.substring(decrementador - 1, decrementador)) * multiplicador;

			multiplicador += 1;
			decrementador -= 1;
		}

		Integer result = (acumulador / 11);

		if (result == 0 || result == 1 || result > 9) {
			return "1";
		}

		return result.toString();
	}


	private String getCarteira(String linhaDigitavel) {
		return StringUtils.leftPad(linhaDigitavel.substring(7, 8), 2, "0");
	}

	private String getNossoNumero(String nossoNumero) {
		String nossoNumeroSomenteNumeros = new Convert().convertNumberFromString(nossoNumero);

		return StringUtils.leftPad(nossoNumeroSomenteNumeros.substring(0, nossoNumeroSomenteNumeros.length() - 1), 11,
				"0");
	}

	private String getZero() {
		return "0";
	}
	
	private String getContaBeneficiarioSemDigito(String contaBeneficiario) {
		String contaBeneficiarioSomenteNumeros = new Convert().convertNumberFromString(contaBeneficiario);

		return StringUtils.leftPad(
				contaBeneficiarioSomenteNumeros.substring(0, contaBeneficiarioSomenteNumeros.length() - 1), 7, "0");
	}

}
