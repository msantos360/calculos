package br.com.bancos.calculos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class SantanderCodigoDeBarrasTest extends BarcodeGenerics {

	private static final String LINHA_DIGITAVEL = "03399.16918 02310.002882 34883.001017 8 58750000004190";
	private static final String BARCODE_ESPERADO = "03398587500000041909169102310002883488300101";
	private static final String EXEMPLO_BARCODE = "0339204600000273719028203356661245780020102";
	private static final String EXEMPLO_DIGITO_ESPERADO = "6";

	private static final String BANCO_ESPERADO = "033";
	private static final String MOEDA_ESPERADA = "9";
	private static final String DIGITO_VERIF_COD_BARRAS_ESPERADO = "8";
	private static final String VALOR_DO_BOLETO_ESPERADO = "0000004190";
	private static final LocalDate DATA_VENCIMENTO_BOLETO = LocalDate.of(2013, 11, 07);
	private static final String CODIGO_CEDENTE_ESPERADO = "1691023";
	private static final String NOSSO_NUMERO_ESPERADO = "1000288348830";
	private static final String ZERO_ESPERADO = "0";
	private static final String CARTEIRA_ESPERADA = "101";
	
	@Test
	public void testeBoletosAcotuboSantander() {
		SantanderCalculoBarcode inox = new SantanderCalculoBarcode(LocalDate.of(2020, 12, 29), "03399.65394 21100.000005 01395.101049 3 84840000687317");
		SantanderCalculoBarcode inox2 = new SantanderCalculoBarcode(LocalDate.of(2020, 12, 16), "03399.65394 21100.000005 01974.701045 1 84710000203058");
		SantanderCalculoBarcode tubosEacos = new SantanderCalculoBarcode(LocalDate.of(2015, 11, 01), "03399.10648 11800.000009 23714.001049 1 65990000083477");
		
		String barcodeInox = inox.getBarcode();
		String barcodeInox2 = inox2.getBarcode();
		String barcodeTubosEacos = tubosEacos.getBarcode();
		
		assertEquals("03393848400006873179653921100000000139510104", barcodeInox);
		assertEquals("03391847100002030589653921100000000197470104", barcodeInox2);
		assertEquals("03391659900000834779106411800000002371400104", barcodeTubosEacos);
		
	}

	@Test
	public void testeDigitoExemploSantander() {
		String digitoVerificadorCodigoDeBarras = getDigitoVerificadorCodigoDeBarras(EXEMPLO_BARCODE);

		assertEquals(EXEMPLO_DIGITO_ESPERADO, digitoVerificadorCodigoDeBarras);
	}
	
	@Test
	public void testeGetCarteira() {
		String carteira = getCarteira("03399169180231000288234883001017858750000004190");
		
		assertEquals(CARTEIRA_ESPERADA, carteira);
	}
	
	@Test
	public void testeNossoNumero() {
		String nossoNumero = getNossoNumero("03399169180231000288234883001017858750000004190");
		
		assertEquals("1000288348830", nossoNumero);
	}

	@Test
	public void testeCodigoCedentePadraoSantander() {
		String codigoCedente = getCodigoCedentePadraoSantander("03399169180231000288234883001017858750000004190");
		
		assertEquals(CODIGO_CEDENTE_ESPERADO, codigoCedente);
	}
	
	
	@Test
	public void montaCodigoDeBarras() {
		String linhaDigitavelSemFormatacao = new Convert().convertNumberFromString(LINHA_DIGITAVEL);

		String identificacaoDoBanco = getIdentificacaoDoBanco(linhaDigitavelSemFormatacao);
		String codigoMoeda = getCodigoMoeda(linhaDigitavelSemFormatacao);
		String fatorDeVencimentoBoleto = getFatorDeVencimento(DATA_VENCIMENTO_BOLETO);
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

		String barcodeCom44posicoes = getBarcodeCompleto(digitoVerificadorCodigoDeBarras,
				barcodeCom43posicoes.toString());

		assertEquals(BANCO_ESPERADO, identificacaoDoBanco);
		assertEquals(MOEDA_ESPERADA, codigoMoeda);
		assertEquals(DIGITO_VERIF_COD_BARRAS_ESPERADO, digitoVerificadorCodigoDeBarras);
		assertEquals(VALOR_DO_BOLETO_ESPERADO, valorDoBoleto);
		assertEquals("5875", fatorDeVencimentoBoleto);
		assertEquals(CARTEIRA_ESPERADA, carteira);
		assertEquals(CODIGO_CEDENTE_ESPERADO, codigoCedentePadraoSantander);
		assertEquals(NOSSO_NUMERO_ESPERADO, nossoNumero);
		assertEquals(ZERO_ESPERADO, zero);
		assertEquals(CARTEIRA_ESPERADA, carteira);
		assertEquals(BARCODE_ESPERADO, barcodeCom44posicoes);
		assertEquals(TAMANHO_BARCODE_BOLETO, barcodeCom44posicoes.length());
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
