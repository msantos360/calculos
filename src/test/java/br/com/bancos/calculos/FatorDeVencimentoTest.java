package br.com.bancos.calculos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class FatorDeVencimentoTest extends BarcodeGenerics {

	private static final LocalDate DATA_VENCIMENTO_TESTE1 = LocalDate.of(2000, 07, 03);
	private static final LocalDate DATA_VENCIMENTO_TESTE2 = LocalDate.of(2000, 07, 04);
	private static final LocalDate DATA_VENCIMENTO_TESTE3 = LocalDate.of(2000, 07, 05);
	private static final LocalDate DATA_VENCIMENTO_TESTE4 = LocalDate.of(2002, 05, 01);
	private static final LocalDate DATA_VENCIMENTO_TESTE5 = LocalDate.of(2010, 11, 17);
	private static final LocalDate DATA_VENCIMENTO_TESTE6 = LocalDate.of(2025, 02, 21);
	private static final LocalDate DATA_VENCIMENTO_TESTE7 = LocalDate.of(2025, 02, 22);
	private static final LocalDate DATA_VENCIMENTO_TESTE8 = LocalDate.of(2025, 02, 23);
	
	
	@Test
	public void calculaFatorDeVencimento() {
		String fatorDeVencimento = getFatorDeVencimento(DATA_VENCIMENTO_TESTE1);
		String fatorDeVencimento2 = getFatorDeVencimento(DATA_VENCIMENTO_TESTE2);
		String fatorDeVencimento3 = getFatorDeVencimento(DATA_VENCIMENTO_TESTE3);
		String fatorDeVencimento4 = getFatorDeVencimento(DATA_VENCIMENTO_TESTE4);
		String fatorDeVencimento5 = getFatorDeVencimento(DATA_VENCIMENTO_TESTE5);
		String fatorDeVencimento6 = getFatorDeVencimento(DATA_VENCIMENTO_TESTE6);
		String fatorDeVencimento7 = getFatorDeVencimento(DATA_VENCIMENTO_TESTE7);
		String fatorDeVencimento8 = getFatorDeVencimento(DATA_VENCIMENTO_TESTE8);

		assertEquals("1000", fatorDeVencimento);
		assertEquals("1001", fatorDeVencimento2);
		assertEquals("1002", fatorDeVencimento3);
		assertEquals("1667", fatorDeVencimento4);
		assertEquals("4789", fatorDeVencimento5);
		assertEquals("9999", fatorDeVencimento6);
		assertEquals("1000", fatorDeVencimento7);
		assertEquals("1001", fatorDeVencimento8);
	}


	@Override
	protected String getDigitoVerificadorCodigoDeBarras(String barcode) {
		// TODO Auto-generated method stub
		return null;
	}

}
