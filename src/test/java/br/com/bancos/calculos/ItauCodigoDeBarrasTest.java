package br.com.bancos.calculos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;

public class ItauCodigoDeBarrasTest extends BarcodeGenerics {

	private static final String LINHA_DIGITAVEL = "34191.76007 01711.780096 40348.990009 8 58180000006999";
	private static final String BARCODE_ESPERADO = "34198581800000069991760001711780094034899000";
	private static final String EXEMPLO_BARCODE_ITAU = "3419166700000123451101234567880057123457000";
	private static final String EXEMPLO_DV_ITAU_ESPERADO = "6";

	private static final String BANCO_ESPERADO = "341";
	private static final String MOEDA_ESPERADA = "9";
	private static final String DIGITO_VERIF_COD_BARRAS_ESPERADO = "8";
	private static final String VALOR_DO_BOLETO_ESPERADO = "0000006999";
	private static final String AGENCIA_ESPERADA = "0094";
	private static final String CONTA_BENEFICIARIO_ESPERADO = "034899";
	private static final String CARTEIRA_ESPERADA = "176";
	private static final String NOSSO_NUMERO_ESPERADO = "176000171178";
	private static final String ZEROS_ESPERADOS = "000";
	private static final LocalDate DATA_VENCIMENTO_BOLETO_ESPERADA = LocalDate.of(2013, 9, 11);

	@Test
	public void calculDVItau() {

		String digitoVerificadorCodigoDeBarras = getDigitoVerificadorCodigoDeBarras(EXEMPLO_BARCODE_ITAU);

		assertEquals(EXEMPLO_DV_ITAU_ESPERADO, digitoVerificadorCodigoDeBarras);
	}
	
	@Test
	public void testeDigitoNossoNumero() {
		String digitoNossoNumero = getDigitoNossoNumero("34191123903955458293081054140009784770003055248");
		String digitoNossoNumero2 = getDigitoNossoNumero("34191123903132165293081054140009184830000332661");
		String digitoNossoNumero3 = getDigitoNossoNumero("34191819812395896293081092670009384380005316393");
		String digitoNossoNumero4 = getDigitoNossoNumero("34191124659716123293581092670009784800000625060");
		String digitoNossoNumero5 = getDigitoNossoNumero("34191123900680299293080617570009384850000785490");
		String digitoNossoNumero6 = getDigitoNossoNumero("34191123900682956293080617570009884670001061500");
		
		assertEquals("9", digitoNossoNumero);
		assertEquals("9", digitoNossoNumero2);
		assertEquals("9", digitoNossoNumero3);
		assertEquals("9", digitoNossoNumero4);
		assertEquals("9", digitoNossoNumero5);
		assertEquals("9", digitoNossoNumero6);
	}
	
	@Test
	public void montaCodigoDeBarrasUtilizandoAclassItau() {
		ItauCalculoBarcode incotep = new ItauCalculoBarcode(LocalDate.of(2020, 12, 22),
				"34191.12390 39554.582930 81054.140009 7 84770003055248", "11239395545-8", "2938", "10541-4");
		ItauCalculoBarcode incotep2 = new ItauCalculoBarcode(LocalDate.of(2020, 12, 28),
				"34191.12390 31321.652930 81054.140009 1 84830000332661", "393132165", "2938", "10541-4");

		ItauCalculoBarcode inox = new ItauCalculoBarcode(LocalDate.of(2020, 11, 13),
				"34191.81981 23958.962930 81092.670009 3 84380005316393", "181/98239589-6", "2938", "10926-7");
		ItauCalculoBarcode inox2 = new ItauCalculoBarcode(LocalDate.of(2020, 12, 25),
				"34191.12465 97161.232935 81092.670009 7 84800000625060", "469716123", "2938", "10926-7");

		ItauCalculoBarcode acotubo = new ItauCalculoBarcode(LocalDate.of(2020, 12, 30),
				"34191.12390 06802.992930 80617.570009 3 84850000785490", "39068029", "2938", "06175-7");

		String barcodeIncotep = incotep.getBarcode();
		String barcodeIncotep2 = incotep2.getBarcode();
		
		String barcodeInox = inox.getBarcode();
		String barcodeInox2 = inox2.getBarcode();
		
		String barcodeAcotubo = acotubo.getBarcode();

		assertEquals("34197847700030552481123939554582938105414000", barcodeIncotep);
		assertEquals("34191848300003326611123931321652938105414000", barcodeIncotep2);
		
		assertEquals("34193843800053163931819823958962938109267000", barcodeInox);
		assertEquals("34197848000006250601124697161232938109267000", barcodeInox2);
		
		assertEquals("34193848500007854901123906802992938061757000", barcodeAcotubo);
		
	}

	@Test
	public void montaCodigoDeBarrasParaCarteirasNaoEspeciais() {
		String linhaDigitavelSemFormatacao = new Convert().convertNumberFromString(LINHA_DIGITAVEL);

		String identificacaoDoBanco = getIdentificacaoDoBanco(linhaDigitavelSemFormatacao);
		String codigoMoeda = getCodigoMoeda(linhaDigitavelSemFormatacao);
		String fatorDeVencimentoBoleto = getFatorDeVencimento(DATA_VENCIMENTO_BOLETO_ESPERADA);
		String valorDoBoleto = getValorDoBoleto(linhaDigitavelSemFormatacao);
		String carteira = getCarteira(linhaDigitavelSemFormatacao);
		String nossoNumero = getNossoNumero("176/00017117-8", linhaDigitavelSemFormatacao);
		String agenciaBeneficiaria = getAgenciaBeneficiariaSemDigito("0094");
		String contaBeneficiarioSemDigito = getContaBeneficiarioComDigito("03489-9");
		String zeros = getZeros();

		StringBuilder barcodeCom43posicoes = new StringBuilder();

		barcodeCom43posicoes.append(identificacaoDoBanco);
		barcodeCom43posicoes.append(codigoMoeda);
		barcodeCom43posicoes.append(fatorDeVencimentoBoleto);
		barcodeCom43posicoes.append(valorDoBoleto);
		barcodeCom43posicoes.append(nossoNumero);

		barcodeCom43posicoes.append(agenciaBeneficiaria);
		barcodeCom43posicoes.append(contaBeneficiarioSemDigito);
		barcodeCom43posicoes.append(zeros);

		String digitoVerificadorCodigoDeBarras = getDigitoVerificadorCodigoDeBarras(barcodeCom43posicoes.toString());

		String barcodeCom44posicoes = getBarcodeCompleto(digitoVerificadorCodigoDeBarras,
				barcodeCom43posicoes.toString());

		assertEquals(BANCO_ESPERADO, identificacaoDoBanco);
		assertEquals(MOEDA_ESPERADA, codigoMoeda);
		assertEquals(DIGITO_VERIF_COD_BARRAS_ESPERADO, digitoVerificadorCodigoDeBarras);
		assertEquals("5818", fatorDeVencimentoBoleto);
		assertEquals(VALOR_DO_BOLETO_ESPERADO, valorDoBoleto);
		assertEquals(AGENCIA_ESPERADA, agenciaBeneficiaria);
		assertEquals(CARTEIRA_ESPERADA, carteira);
		assertEquals(NOSSO_NUMERO_ESPERADO, nossoNumero);
		assertEquals(CONTA_BENEFICIARIO_ESPERADO, contaBeneficiarioSemDigito);
		assertEquals(ZEROS_ESPERADOS, zeros);
		assertEquals(BARCODE_ESPERADO, barcodeCom44posicoes);
		assertEquals(TAMANHO_BARCODE_BOLETO, barcodeCom44posicoes.length());
	}

	private String getCarteira(String linhaDigitavel) {
		String linhaDigitalSomenteNumeros = new Convert().convertNumberFromString(linhaDigitavel);

		return linhaDigitalSomenteNumeros.substring(4, 7);

	}

	private String getNossoNumero(String nossoNumero, String linhaDigitavel) {
		String nossoNumeroSomenteNumeros = new Convert().convertNumberFromString(nossoNumero);
		String linhaDigitalSomenteNumeros = new Convert().convertNumberFromString(linhaDigitavel);

		String carteira = getCarteira(linhaDigitalSomenteNumeros);

		StringBuilder carteiraNossoNumeroDigito = new StringBuilder();
		
		if (!carteira.equals(nossoNumeroSomenteNumeros.substring(0, 3))) {
			carteiraNossoNumeroDigito.append(carteira);
		}

		carteiraNossoNumeroDigito.append(nossoNumeroSomenteNumeros);

		if (carteiraNossoNumeroDigito.length() == 11) {
			carteiraNossoNumeroDigito.append(getDigitoNossoNumero(carteiraNossoNumeroDigito.toString()));
		}
		
		//String nn = StringUtils.leftPad(nossoNumeroSomenteNumeros, 8, "0");


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
