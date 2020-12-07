package br.com.bancos.calculos;

import java.time.LocalDate;

import org.apache.commons.lang.StringUtils;

public class BancoDoBrasilCalculoBarcode extends BarcodeGenerics{
	
	private String barcode;

	public BancoDoBrasilCalculoBarcode(LocalDate dataVencimento, String linhaDigitavel, String nossoNumero, String agencia, String conta) {
		String linhaDigitavelSemFormatacao = new Convert().convertNumberFromString(linhaDigitavel);

		String identificacaoDoBanco = getIdentificacaoDoBanco(linhaDigitavelSemFormatacao);
		String codigoMoeda = getCodigoMoeda(linhaDigitavelSemFormatacao);
		String fatorDeVencimentoBoleto = getFatorDeVencimento(dataVencimento);
		String valorDoBoleto = getValorDoBoleto(linhaDigitavelSemFormatacao);
		String numeroDoConvenio = getNumeroDoConvenio(nossoNumero);
		String complementoNossoNumero = getComplementoNossoNumero(nossoNumero);
		String agenciaBeneficiaria = getAgenciaBeneficiariaSemDigito(agencia);
		String contaBeneficiario = getContaBeneficiarioSemDigito(conta);
		String tipoModalidadeDeCobranca = getTipoModalidadeDeCobranca(nossoNumero);

		StringBuilder barcodeCom43posicoes = new StringBuilder();

		barcodeCom43posicoes.append(identificacaoDoBanco);
		barcodeCom43posicoes.append(codigoMoeda);
		barcodeCom43posicoes.append(fatorDeVencimentoBoleto);
		barcodeCom43posicoes.append(valorDoBoleto);
		barcodeCom43posicoes.append(numeroDoConvenio);
		barcodeCom43posicoes.append(complementoNossoNumero);
		barcodeCom43posicoes.append(agenciaBeneficiaria);
		barcodeCom43posicoes.append(contaBeneficiario);
		barcodeCom43posicoes.append(tipoModalidadeDeCobranca);

		String digitoVerificadorCodigoDeBarras = getDigitoVerificadorCodigoDeBarras(barcodeCom43posicoes.toString());

		this.barcode = getBarcodeCompleto(digitoVerificadorCodigoDeBarras,
				barcodeCom43posicoes.toString());

	}
	
	public String getBarcode() {
		return barcode;
	}

	private String getContaBeneficiarioSemDigito(String contaBeneficiario) {
		String contaBeneficiarioSomenteNumeros = new Convert().convertNumberFromString(contaBeneficiario);

		return StringUtils.leftPad(
				contaBeneficiarioSomenteNumeros.substring(0, contaBeneficiarioSomenteNumeros.length() - 1), 8, "0");
	}

	private String getTipoModalidadeDeCobranca(String nossoNumero) {

		String nossoNumeroSomenteNumeros = new Convert().convertNumberFromString(nossoNumero);

		Long nn = Long.parseLong(nossoNumeroSomenteNumeros);

		if (nn.toString().length() == 17) {
			return "21";
		}

		return "17";
	}

	private String getComplementoNossoNumero(String nossoNumero) {

		String nossoNumeroSomenteNumeros = new Convert().convertNumberFromString(nossoNumero);

		Long nn = Long.parseLong(nossoNumeroSomenteNumeros);

		if (nn.toString().length() == 17) {
			return StringUtils.leftPad(nn.toString().substring(0, nn.toString().length() - 1), 17, "0");

		} else if (nn.toString().length() == 11) {
			return StringUtils.leftPad(nn.toString().substring(nn.toString().length() - 5, nn.toString().length()), 5,
					"0");
		}

		return "";
	}

	private String getNumeroDoConvenio(String nossoNumero) {
		String nossoNumeroSomenteNumeros = new Convert().convertNumberFromString(nossoNumero);

		Long nn = Long.parseLong(nossoNumeroSomenteNumeros);

		if (nn.toString().length() == 11) {
			return StringUtils.leftPad(nn.toString().substring(0, 6), 6, "0");
		}

		return "";

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

}
