package br.com.bancos.calculos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;

public class BancoDoBrasilCodigoDeBarrasTest extends BarcodeGenerics {

	private static final String LINHA_DIGITAVEL = "00191.60373 53813.632212 00105.178172 4 84270000032445";
	private static final String BARCODE_ESPERADO = "00194842700000324451603753813632210010517817";
	private static final String EXEMPLO_BARCODE = "0019373700000001000500940144816060680935031";
	private static final String EXEMPLO_DIGITO_ESPERADO = "3";

	private static final String BANCO_ESPERADO = "001";
	private static final String MOEDA_ESPERADA = "9";
	private static final String VALOR_DO_BOLETO_ESPERADO = "0000032445";
	private static final LocalDate DATA_VENCIMENTO_BOLETO = LocalDate.of(2020, 11, 02);
	private static final String DIGITO_VERIF_COD_BARRAS_ESPERADO = "4";
	private static final String AGENCIA_ESPERADA = "3221";
	private static final String CONTA_ESPERADA = "00105178";
	private static final String NUMERO_DO_CONVENIO_ESPERADO = "160375";
	private static final String COMPLEMENTO_NOSSO_NUMERO_ESPERADO = "38136";
	private static final String TIPO_MODALIDADE_DE_COBRANCA_ESPERADO = "17";

	@Test
	public void testeDigitoExemploBancoDoBrasil() {
		String digitoVerificadorCodigoDeBarras = getDigitoVerificadorCodigoDeBarras(EXEMPLO_BARCODE);

		assertEquals(EXEMPLO_DIGITO_ESPERADO, digitoVerificadorCodigoDeBarras);
	}

	@Test
	public void montaCodigoDeBarras() {
		String linhaDigitavelSemFormatacao = new Convert().convertNumberFromString(LINHA_DIGITAVEL);

		String identificacaoDoBanco = getIdentificacaoDoBanco(linhaDigitavelSemFormatacao);
		String codigoMoeda = getCodigoMoeda(linhaDigitavelSemFormatacao);
		String fatorDeVencimentoBoleto = getFatorDeVencimento(DATA_VENCIMENTO_BOLETO);
		String valorDoBoleto = getValorDoBoleto(linhaDigitavelSemFormatacao);
		String numeroDoConvenio = getNumeroDoConvenio("00000016037538136");
		String complementoNossoNumero = getComplementoNossoNumero("00000016037538136");
		String agenciaBeneficiaria = getAgenciaBeneficiariaSemDigito("3221-2");
		String contaBeneficiario = getContaBeneficiarioSemDigito("105178-4");
		String tipoModalidadeDeCobranca = getTipoModalidadeDeCobranca("00000016037538136");

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

		String barcodeCom44posicoes = getBarcodeCompleto(digitoVerificadorCodigoDeBarras,
				barcodeCom43posicoes.toString());

		assertEquals(BANCO_ESPERADO, identificacaoDoBanco);
		assertEquals(MOEDA_ESPERADA, codigoMoeda);
		assertEquals(DIGITO_VERIF_COD_BARRAS_ESPERADO, digitoVerificadorCodigoDeBarras);
		assertEquals(VALOR_DO_BOLETO_ESPERADO, valorDoBoleto);
		assertEquals("8427", fatorDeVencimentoBoleto);
		assertEquals(NUMERO_DO_CONVENIO_ESPERADO, numeroDoConvenio);
		assertEquals(COMPLEMENTO_NOSSO_NUMERO_ESPERADO, complementoNossoNumero);
		assertEquals(AGENCIA_ESPERADA, agenciaBeneficiaria);
		assertEquals(CONTA_ESPERADA, contaBeneficiario);
		assertEquals(TIPO_MODALIDADE_DE_COBRANCA_ESPERADO, tipoModalidadeDeCobranca);
		assertEquals(BARCODE_ESPERADO, barcodeCom44posicoes);
		assertEquals(TAMANHO_BARCODE_BOLETO, barcodeCom44posicoes.length());
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
