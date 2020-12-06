package br.com.bancos.calculos;

import java.time.Duration;
import java.time.LocalDate;

import org.apache.commons.lang.StringUtils;

public abstract class BarcodeGenerics {

	private static final LocalDate DATA_BASE = LocalDate.of(1997, 10, 07);
	private static final LocalDate DATA_VENCIMENTO_LIMITE = LocalDate.of(2025, 02, 22);

	private static final Integer TAMANHO_CODIGO_DO_BANCO = 3;
	private static final Integer TAMANHO_CODIGO_MOEDA = 1;
	private static final Integer TAMANHO_FATOR_DE_VENCIMENTO = 4;
	private static final Integer TAMANHO_VALOR_DO_BOLETO = 10;
	protected static final Integer TAMANHO_BARCODE_BOLETO = 44;
	protected static final Integer TAMANHO_DV_CODIGO_DE_BARRAS = 1;
	
	protected String getAgenciaBeneficiariaSemDigito(String agencia) {
		String agenciaSomenteNumeros = new Convert().convertNumberFromString(agencia);

		if (agenciaSomenteNumeros.length() >= 4) {
			return agenciaSomenteNumeros.substring(0, 4);
		}

		return StringUtils.leftPad(agenciaSomenteNumeros, 4, "0");

	}
	
	protected String getFatorDeVencimento(LocalDate dataVencimentoBoleto) {

		Long days = null;

		if (DATA_VENCIMENTO_LIMITE.isBefore(dataVencimentoBoleto)
				|| DATA_VENCIMENTO_LIMITE.isEqual(dataVencimentoBoleto)) {

			Long daysBefore22022025 = Duration.between(DATA_BASE.atStartOfDay(), dataVencimentoBoleto.atStartOfDay())
					.dividedBy(10).toDays();

			Long count = Duration.between(DATA_VENCIMENTO_LIMITE.atStartOfDay(), dataVencimentoBoleto.atStartOfDay())
					.toDays();

			Long result = daysBefore22022025 + count;

			if (result.toString().length() > 4 || TAMANHO_FATOR_DE_VENCIMENTO > 4) {
				throw new IllegalArgumentException("Fator de vencimento é maior que o determinado (4 dígitos). - FEBRABAN");
			}
			
			return result.toString();
		}

		days = Duration.between(DATA_BASE.atStartOfDay(), dataVencimentoBoleto.atStartOfDay()).toDays();
		
		if (days.toString().length() > 4) {
			throw new IllegalArgumentException("Fator de vencimento é maior que o determinado (4 dígitos). - FEBRABAN");
		}
		
		return days.toString();
	}

	protected String getValorDoBoleto(String linhaDigitavel) {
		String linha = linhaDigitavel.substring(linhaDigitavel.length() - TAMANHO_VALOR_DO_BOLETO, linhaDigitavel.length());
		
		if (TAMANHO_VALOR_DO_BOLETO > 10 || linha.length() > 10) {
			throw new IllegalArgumentException("Valor do boleto é maior que o determinado (10 dígitos). - FEBRABAN");
		}
		
		return linha;
	}

	protected String getIdentificacaoDoBanco(String linhaDigitavel) {

		String linha = linhaDigitavel.substring(0, TAMANHO_CODIGO_DO_BANCO);

		if (TAMANHO_CODIGO_DO_BANCO > 3 || linha.length() > 3) {
			throw new IllegalArgumentException("Identificação do banco é maior que o determinado (3 dígitos). - FEBRABAN");
		}

		return linha;
	}

	protected String getCodigoMoeda(String linhaDigitavel) {

		String linha = linhaDigitavel.substring(3, 4);

		if (TAMANHO_CODIGO_MOEDA > 1 || linha.length() > 1) {
			throw new IllegalArgumentException("Código moeda é maior que o determinado (1 dígito). - FEBRABAN");
		}

		return linha;
	}
	
	protected String getBarcodeCompleto(String digitoVerificadorCodigoDeBarras, String barcode) {

		String inicio = barcode.substring(0, 4);
		String fim = barcode.substring(4, barcode.length());
		
		if ((inicio + digitoVerificadorCodigoDeBarras + fim).length() > TAMANHO_BARCODE_BOLETO) {
			throw new IllegalArgumentException("Tamanho do barcode é maior que o determinado (44 dígitos). - FEBRABAN");
		}

		return inicio + digitoVerificadorCodigoDeBarras + fim;
	}
	
	protected abstract String getDigitoVerificadorCodigoDeBarras(String barcode);
	
	
}
