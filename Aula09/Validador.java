package com.example.exemplocrud;

import java.util.regex.Pattern;

public class Validador {

    // Padrão para validar telefone: (XX) 9XXXX-XXXX
    private static final Pattern TELEFONE_PATTERN = Pattern.compile("^\\(?([1-9]{2})\\)?\\s?9[0-9]{4}-?[0-9]{4}$");

    /**
     * Valida se o CPF é matematicamente válido e não possui dígitos repetidos.
     */
    public static boolean validaCpf(String CPF) {
        if (CPF == null) return false;

        // Remove caracteres não numéricos
        CPF = CPF.replaceAll("[^0-9]", "");

        if (CPF.length() != 11 || CPF.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma, num, peso, resto;
            char dig10, dig11;

            // Cálculo do 1º dígito verificador
            soma = 0; peso = 10;
            for (int i = 0; i < 9; i++) {
                num = CPF.charAt(i) - '0';
                soma += (num * peso--);
            }
            resto = soma % 11;
            dig10 = (resto < 2) ? '0' : (char) ((11 - resto) + '0');

            // Cálculo do 2º dígito verificador
            soma = 0; peso = 11;
            for (int i = 0; i < 10; i++) {
                num = CPF.charAt(i) - '0';
                soma += (num * peso--);
            }
            resto = soma % 11;
            dig11 = (resto < 2) ? '0' : (char) ((11 - resto) + '0');

            return (dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10));

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida se o telefone possui 11 dígitos e segue o padrão brasileiro.
     */
    public static boolean validaTelefone(String telefone) {
        if (telefone == null) return false;

        // Para a regex funcionar corretamente, comparamos com o formato esperado
        // ou limpamos antes. Aqui usamos a regex definida acima.
        return TELEFONE_PATTERN.matcher(telefone).matches();
    }
}