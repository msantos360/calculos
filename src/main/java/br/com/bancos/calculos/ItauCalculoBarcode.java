package br.com.bancos.calculos;

import java.time.LocalDate;

import org.apache.commons.lang.StringUtils;

public class ItauCalculoBarcode extends BarcodeGenerics {

	private String barcode;

	public ItauCalculoBarcode(LocalDate dataVencimento, String linhaDigitavel, String nossoNumero, String agencia,
			String conta) {
		String linhaDigitavelSemFormatacao = new Convert().convertNumberFromString(linhaDigitavel);

		String identificacaoDoBanco = getIdentificacaoDoBanco(linhaDigitavelSemFormatacao);
		String codigoMoeda = getCodigoMoeda(linhaDigitavelSemFormatacao);
		String fatorDeVencimentoBoleto = getFatorDeVencimento(dataVencimento);
		String valorDoBoleto = getValorDoBoleto(linhaDigitavelSemFormatacao);
		String nn = getNossoNumero(nossoNumero, linhaDigitavelSemFormatacao, agencia, conta);
		String agenciaBeneficiaria = getAgenciaBeneficiariaSemDigito(agencia);
		String contaBeneficiarioSemDigito = getContaBeneficiarioComDigito(conta);
		String zeros = getZeros();

		StringBuilder barcodeCom43posicoes = new StringBuilder();

		barcodeCom43posicoes.append(identificacaoDoBanco);
		barcodeCom43posicoes.append(codigoMoeda);
		barcodeCom43posicoes.append(fatorDeVencimentoBoleto);
		barcodeCom43posicoes.append(valorDoBoleto);
		barcodeCom43posicoes.append(nn);

		barcodeCom43posicoes.append(agenciaBeneficiaria);
		barcodeCom43posicoes.append(contaBeneficiarioSemDigito);
		barcodeCom43posicoes.append(zeros);

		String digitoVerificadorCodigoDeBarras = getDigitoVerificadorCodigoDeBarras(barcodeCom43posicoes.toString());

		this.barcode = getBarcodeCompleto(digitoVerificadorCodigoDeBarras, barcodeCom43posicoes.toString());
	}

	public String getBarcode() {
		return barcode;
	}

	private String getCarteira(String linhaDigitavel) {
		String linhaDigitalSomenteNumeros = new Convert().convertNumberFromString(linhaDigitavel);

		return linhaDigitalSomenteNumeros.substring(4, 7);

	}
	
	private String getNossoNumero(String nossoNumero, String linhaDigitavel, String agencia, String conta) {
		String nossoNumeroSomenteNumeros = new Convert().convertNumberFromString(nossoNumero);
		String linhaDigitalSomenteNumeros = new Convert().convertNumberFromString(linhaDigitavel);
		String contaSomenteNumeros = new Convert().convertNumberFromString(conta);

		String carteira = getCarteira(linhaDigitalSomenteNumeros);

		StringBuilder carteiraNossoNumeroDigito = new StringBuilder();
		
		if (!carteira.equals(nossoNumeroSomenteNumeros.substring(0, 3))) {
			carteiraNossoNumeroDigito.append(carteira);
		}

		carteiraNossoNumeroDigito.append(nossoNumeroSomenteNumeros);

		String nossoNumeroAgenciaEConta = agencia + contaSomenteNumeros.subSequence(0, contaSomenteNumeros.length() - 1) + carteiraNossoNumeroDigito.toString();
		
		if (nossoNumeroAgenciaEConta.length() == 20) {
			carteiraNossoNumeroDigito.append(getDigitoNossoNumero(linhaDigitalSomenteNumeros));
		}

		return carteiraNossoNumeroDigito.toString();
	}

	private String getContaBeneficiarioComDigito(String contaBeneficiario) {
		String contaBeneficiarioSomenteNumeros = new Convert().convertNumberFromString(contaBeneficiario);

		return StringUtils.leftPad(contaBeneficiarioSomenteNumeros.substring(
				contaBeneficiarioSomenteNumeros.length() - 6, contaBeneficiarioSomenteNumeros.length()), 6, "0");
	}

	private String getZeros() {
		return "000";
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

		Integer result = (11 - (acumulador % 11));

		if (result == 0 || result == 10 || result == 11) {
			return "1";
		}

		if (TAMANHO_DV_CODIGO_DE_BARRAS > 1 || result.toString().length() > 1) {
			throw new IllegalArgumentException(
					"DV do códigoo de barras é maior que o determinado (1 dígito). - FEBRABAN");
		}

		return result.toString();
	}
	
	private String getDigitoNossoNumero(String linhaDigitavel) {

		return linhaDigitavel.substring(31,32);
		
		/*int decrementador = 20;
		int multiplicador = 2;
		Integer acumulador = 0;

		for (int i = 0; i < agenciaContaCarteiraNossoNumero.length(); i++) {

			if (multiplicador > 2) {
				multiplicador = 1;
			}

			if ((Integer.parseInt(agenciaContaCarteiraNossoNumero.substring(decrementador - 1, decrementador))
					* multiplicador) > 9) {

				acumulador += Integer.parseInt(String.valueOf(
						(Integer.parseInt(agenciaContaCarteiraNossoNumero.substring(decrementador - 1, decrementador))
								* multiplicador))
						.substring(0, 1))
						+ Integer
								.parseInt(String
										.valueOf((Integer.parseInt(agenciaContaCarteiraNossoNumero
												.substring(decrementador - 1, decrementador)) * multiplicador))
										.substring(1, 2));
			} else {
				acumulador += Integer.parseInt(
						agenciaContaCarteiraNossoNumero.substring(decrementador - 1, decrementador)) * multiplicador;
			}

			multiplicador += 1;
			decrementador -= 1;
		}
		Integer result = 10 - (acumulador % 10);

		if (result == 10) {
			return "0";
		}

		return result.toString();*/

	}


}
