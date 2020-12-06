package br.com.bancos.calculos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;

public class BradescoCodigoDeBarrasTest extends BarcodeGenerics {

	private static final String LINHA_DIGITAVEL = "23790.33901 00287.000020 00701.236309 9 84830001932506";
	private static final String BARCODE_ESPERADO = "23791848300019325063397090287000020000014020";

	private static final String BANCO_ESPERADO = "237";
	private static final String MOEDA_ESPERADA = "9";
	private static final String DIGITO_VERIF_COD_BARRAS_ESPERADO = "1";
	private static final String VALOR_DO_BOLETO_ESPERADO = "0001932506";
	private static final String AGENCIA_ESPERADA = "3397";
	private static final String AGENCIA_ESPERADA2 = "0039";
	private static final String CARTEIRA_ESPERADA = "09";
	private static final String NOSSO_NUMERO_ESPERADO = "02870000200";
	private static final String CONTA_BENEFICIARIO_ESPERADO = "0001402";
	private static final String ZERO_ESPERADO = "0";
	private static final LocalDate DATA_VENCIMENTO_BOLETO_ESPERADA = LocalDate.of(2020, 12, 28);

	@Test
	public void montaCodigoDeBarrasUtilizandoAclassBradesco() {
		BradescoCalculoBarcode bradescoCalculoBarcode = new BradescoCalculoBarcode(LINHA_DIGITAVEL,
				DATA_VENCIMENTO_BOLETO_ESPERADA, "3397-9", "028700002009", "001402-8");

		assertEquals(BARCODE_ESPERADO, bradescoCalculoBarcode.getBarcodeCom44posicoes());
	}

	@Test
	public void montaCodigoDeBarras() {

		String linhaDigitavelSemFormatacao = new Convert().convertNumberFromString(LINHA_DIGITAVEL);

		String identificacaoDoBanco = getIdentificacaoDoBanco(linhaDigitavelSemFormatacao);
		String codigoMoeda = getCodigoMoeda(linhaDigitavelSemFormatacao);
		String fatorDeVencimentoBoleto = getFatorDeVencimento(DATA_VENCIMENTO_BOLETO_ESPERADA);
		String valorDoBoleto = getValorDoBoleto(linhaDigitavelSemFormatacao);
		String agenciaBeneficiaria = getAgenciaBeneficiariaSemDigito("3397-9");
		String agenciaBeneficiaria2 = getAgenciaBeneficiariaSemDigito("39");
		String carteira = getCarteira(linhaDigitavelSemFormatacao);
		String nossoNumero = getNossoNumero("028700002009");
		String contaBeneficiario = getContaBeneficiarioSemDigito("001402-8");
		String zero = getZero();

		StringBuilder barcodeCom43posicoes = new StringBuilder();

		barcodeCom43posicoes.append(identificacaoDoBanco);
		barcodeCom43posicoes.append(codigoMoeda);
		barcodeCom43posicoes.append(fatorDeVencimentoBoleto);
		barcodeCom43posicoes.append(valorDoBoleto);
		barcodeCom43posicoes.append(agenciaBeneficiaria);
		barcodeCom43posicoes.append(carteira);
		barcodeCom43posicoes.append(nossoNumero);
		barcodeCom43posicoes.append(contaBeneficiario);
		barcodeCom43posicoes.append(zero);

		String digitoVerificadorCodigoDeBarras = getDigitoVerificadorCodigoDeBarras(barcodeCom43posicoes.toString());

		String barcodeCom44posicoes = getBarcodeCompleto(digitoVerificadorCodigoDeBarras,
				barcodeCom43posicoes.toString());

		assertEquals(BANCO_ESPERADO, identificacaoDoBanco);
		assertEquals(MOEDA_ESPERADA, codigoMoeda);
		assertEquals(DIGITO_VERIF_COD_BARRAS_ESPERADO, digitoVerificadorCodigoDeBarras);
		assertEquals("8483", fatorDeVencimentoBoleto);
		assertEquals(VALOR_DO_BOLETO_ESPERADO, valorDoBoleto);
		assertEquals(AGENCIA_ESPERADA, agenciaBeneficiaria);
		assertEquals(AGENCIA_ESPERADA2, agenciaBeneficiaria2);
		assertEquals(CARTEIRA_ESPERADA, carteira);
		assertEquals(NOSSO_NUMERO_ESPERADO, nossoNumero);
		assertEquals(CONTA_BENEFICIARIO_ESPERADO, contaBeneficiario);
		assertEquals(ZERO_ESPERADO, zero);
		assertEquals(TAMANHO_BARCODE_BOLETO, barcodeCom44posicoes.length());
		assertEquals(BARCODE_ESPERADO, barcodeCom44posicoes);

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
	
	private String getContaBeneficiarioSemDigito(String contaBeneficiario) {
		String contaBeneficiarioSomenteNumeros = new Convert().convertNumberFromString(contaBeneficiario);

		return StringUtils.leftPad(
				contaBeneficiarioSomenteNumeros.substring(0, contaBeneficiarioSomenteNumeros.length() - 1), 7, "0");
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

}
