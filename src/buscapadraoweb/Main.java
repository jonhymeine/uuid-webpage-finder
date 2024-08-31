/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package buscapadraoweb;

import buscaweb.CapturaRecursosWeb;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Santiago
 */
public class Main {

  // busca char em vetor e retorna indice
  public static int get_char_ref(char[] vet, char ref) {
    for (int i = 0; i < vet.length; i++) {
      if (vet[i] == ref) {
        return i;
      }
    }
    return -1;
  }

  // busca string em vetor e retorna indice
  public static int get_string_ref(String[] vet, String ref) {
    for (int i = 0; i < vet.length; i++) {
      if (vet[i].equals(ref)) {
        return i;
      }
    }
    return -1;
  }

  // retorna o próximo estado, dado o estado atual e o símbolo lido
  public static int proximo_estado(char[] alfabeto, int[][] matriz, int estado_atual, char simbolo) {
    int simbol_indice = get_char_ref(alfabeto, simbolo);
    if (simbol_indice != -1) {
      return matriz[estado_atual][simbol_indice];
    } else {
      return -1;
    }
  }

  /*
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // instancia e usa objeto que captura código-fonte de páginas Web
    CapturaRecursosWeb crw = new CapturaRecursosWeb();
    crw.getListaRecursos().add("https://www.uuidgenerator.net/version4");
    crw.getListaRecursos().add("https://www.npmjs.com/package/uuid");
    crw.getListaRecursos().add("https://pub.dev/packages/uuid");
    // URLS:
    // - https://www.uuidgenerator.net/version4 - 1 UUID
    // - https://www.npmjs.com/package/uuid - 13 UUIDs
    // - https://pub.dev/packages/uuid -1 UUID
    ArrayList<String> listaCodigos = crw.carregarRecursos();

    String[] codigosHTML = {
        listaCodigos.get(0),
        listaCodigos.get(1),
        listaCodigos.get(2),
    };

    // mapa do alfabeto
    char[] alfabeto = {
        '0', '1', '2', '3',
        '4', '5', '6', '7',
        '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f',
        '-'
    };

    // mapa de estados
    String[] estados = {
        "q0", "q1", "q2", "q3", "q4",
        "q5", "q6", "q7", "q8", "q9",
        "q10", "q11", "q12", "q13", "q14",
        "q15", "q16", "q17", "q18", "q19",
        "q20", "q21", "q22", "q23", "q24",
        "q25", "q26", "q27", "q28", "q29",
        "q30", "q31", "q32", "q33", "q34",
        "q35", "q36"
    };

    String estado_inicial = "q0";

    // estados finais
    String[] estados_finais = new String[1];
    estados_finais[0] = "q36";

    // tabela de transição de AFD para reconhecimento números de dois dígitos
    int[][] matriz = new int[37][17];

    ///// REFATORADO
    // Preenchendo a matriz com transições de q0 a q7
    int zeroIndex = get_char_ref(alfabeto, '0');
    int fIndex = get_char_ref(alfabeto, 'f');
    int hyphenIndex = get_char_ref(alfabeto, '-');
    int q8Index = get_string_ref(estados, "q8");
    int q9Index = get_string_ref(estados, "q9");
    int q13Index = get_string_ref(estados, "q13");
    int q14Index = get_string_ref(estados, "q14");
    int q15Index = get_string_ref(estados, "q15");
    int q17Index = get_string_ref(estados, "q17");
    int q18Index = get_string_ref(estados, "q18");
    int q19Index = get_string_ref(estados, "q19");
    int q20Index = get_string_ref(estados, "q20");
    int q23Index = get_string_ref(estados, "q23");
    int q24Index = get_string_ref(estados, "q24");

    for (int i = get_string_ref(estados, "q0"); i <= get_string_ref(estados, "q7"); i++) {
      for (int j = zeroIndex; j <= fIndex; j++) {
        matriz[i][j] = i + 1; // Transições para o próximo estado (q0 -> q1, q1 -> q2, ...)
      }
      matriz[i][hyphenIndex] = -1; // Transição para o '-' é inválida
    }

    // Transição q8
    for (int i = zeroIndex; i <= fIndex; i++) {
      matriz[q8Index][i] = -1;
    }
    matriz[q8Index][hyphenIndex] = q9Index; // Transição de q8 com '-' para q9

    // Preenchendo a matriz com transições de q9 a q12
    for (int i = q9Index; i <= get_string_ref(estados, "q12"); i++) {
      for (int j = zeroIndex; j <= fIndex; j++) {
        matriz[i][j] = i + 1; // Transições para o próximo estado (q9 -> q10, q10 -> q11, ...)
      }
      matriz[i][hyphenIndex] = -1; // Transição para o '-' é inválida
    }

    // Transição q13
    // Percore a linha 13 e define os valores até o f para -1
    for (int i = zeroIndex; i <= fIndex; i++) {
      matriz[q13Index][i] = -1;
    }
    matriz[q13Index][hyphenIndex] = q14Index; // Transição de q13 com '-' para q14

    // Transição q14
    // Percore a linha 14 e define os valores até o - para -1
    for (int i = zeroIndex; i <= hyphenIndex; i++) {
      matriz[q14Index][i] = -1;
    }
    matriz[q14Index][4] = q15Index; // Transição de q14 com '4' para q15

    // Preenchendo a matriz com transições de q15 a q17
    for (int i = q15Index; i <= q17Index; i++) {
      for (int j = zeroIndex; j <= fIndex; j++) {
        matriz[i][j] = i + 1;
      }
      matriz[i][hyphenIndex] = -1; // Transição para o '-' é inválida
    }

    // Transiçao q18
    for (int i = zeroIndex; i <= fIndex; i++) {
      matriz[q18Index][i] = -1;
    }
    matriz[q18Index][hyphenIndex] = q19Index; // Transição de q18 com '-' para q19

    // Transições de q19 com dígitos 8 e 9 e letras 'a' e 'b' para q20
    // Percore a linha 19 e define os valores até o - para -1
    for (int i = zeroIndex; i <= hyphenIndex; i++) {
      matriz[q19Index][i] = -1;
    }

    matriz[q19Index][get_char_ref(alfabeto, '8')] = q20Index;
    matriz[q19Index][get_char_ref(alfabeto, '9')] = q20Index;
    matriz[q19Index][get_char_ref(alfabeto, 'a')] = q20Index;
    matriz[q19Index][get_char_ref(alfabeto, 'b')] = q20Index;

    // Preenchendo a matriz com transições de q20 a q22
    for (int i = q20Index; i <= get_string_ref(estados, "q22"); i++) {
      for (int j = zeroIndex; j <= fIndex; j++) {
        matriz[i][j] = i + 1; // Transições para o próximo estado (q20 -> q21, q21 -> q22)
      }
      matriz[i][hyphenIndex] = -1; // Transição para o '-' é inválida
    }

    // Trasição do q23 para q24
    // Percore a linha 23 e define os valores até o f para -1
    for (int i = zeroIndex; i <= fIndex; i++) {
      matriz[q23Index][i] = -1;
    }
    matriz[q23Index][hyphenIndex] = q24Index; // Transição de q23 com '-' para q24

    // Preenchendo a matriz com transições de q24 a q35
    for (int i = q24Index; i <= 35; i++) {
      for (int j = zeroIndex; j <= fIndex; j++) {
        matriz[i][j] = i + 1; // Transições para o próximo estado (q24 -> q25, q25 -> q26, ...)
      }
      matriz[i][hyphenIndex] = -1; // Transição para o '-' é inválida
    }

    // Preenchendo as transições de q36 com -1
    for (int j = zeroIndex; j <= hyphenIndex; j++) {
      matriz[get_string_ref(estados, "q36")][j] = -1; // q36 é um estado final e não tem transições
    }

    int estado = get_string_ref(estados, estado_inicial);
    int estado_anterior = -1;
    List<String> palavras_reconhecidas = new ArrayList<String>();

    String palavra;

    int indexHtml = 1;
    for (String html : codigosHTML) {
      palavras_reconhecidas.clear();
      palavra = "";

      System.out.println("Analisando site " + indexHtml);

      // varre o código-fonte de um código
      for (int i = 0; i < html.length(); i++) {

        estado_anterior = estado;
        estado = proximo_estado(alfabeto, matriz, estado, html.charAt(i));
        // se o não há transição
        if (estado == -1) {
          // pega estado inicial
          estado = get_string_ref(estados, estado_inicial);
          // se o estado anterior foi um estado final
          if (get_string_ref(estados_finais, estados[estado_anterior]) != -1) {
            // se a palavra não é vazia adiciona palavra reconhecida
            if (!palavra.equals("")) {
              palavras_reconhecidas.add(palavra);
            }
            // se ao analisar este caracter não houve transição
            // teste-o novamente, considerando que o estado seja inicial
            i--;
          }
          // zera palavra
          palavra = "";

        } else {
          // se houver transição válida, adiciona caracter a palavra
          palavra += html.charAt(i);
        }
      }

      // foreach no Java para exibir todas as palavras reconhecidas
      for (String p : palavras_reconhecidas) {
        System.out.println(p);
      }
      System.out.println("\n\n");

      indexHtml += 1;
    }
  }
}
