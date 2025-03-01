import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JPanel;

public class Jugador {

    private int DISTANCIA = 40;
    private int MARGEN = 10;
    private int TOTAL_CARTAS = 10;
    private String MENSAJE_PREDETERMINADO = "No se encontraron grupos";
    private String ENCABEZADO_MENSAJE = "Se encontraron los siguientes grupos:\n";
    private int MNIMA_CANTIDAD_GRUPO = 2;
    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random();

    public void repartir() {

        List<String> archivos = obtenerArchivosDeCarpeta("C:\\Users\\Anthony_\\Documents\\ITM JAVA\\JuegoCartasExamen\\JuegoCartas\\src\\imagenes"); 

        for (int i = 0; i < TOTAL_CARTAS; i++) {
            int indiceAleatorio = r.nextInt(archivos.size());
            String nombreCarta = archivos.get(indiceAleatorio);
            cartas[i] = new Carta(nombreCarta);
        }
    }

    private List<String> obtenerArchivosDeCarpeta(String rutaCarpeta) {
        List<String> archivos = new ArrayList<>();
        File carpeta = new File(rutaCarpeta);
            for (File archivo : carpeta.listFiles()) {
                if (archivo.isFile() && archivo.getName().toLowerCase().endsWith(".jpg")) {
                    archivos.add(archivo.getName());
                }
            }
        return archivos;
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int x = MARGEN + (TOTAL_CARTAS - 1) * DISTANCIA;
        for (Carta carta : cartas) {
            carta.mostrar(pnl, x, MARGEN);
            x -= DISTANCIA;
        }
        pnl.repaint();
    }

public String getGrupos() {
    String mensaje = MENSAJE_PREDETERMINADO;

    int[] contadores = new int[14];
    for (Carta carta : cartas) {
        contadores[carta.getValor()]++;
    }

    boolean hayGrupos = false;
    StringBuilder gruposBuilder = new StringBuilder(ENCABEZADO_MENSAJE);

    boolean[] cartasEnGrupo = new boolean[cartas.length];
    for (int valor = 1; valor <= 13; valor++) {
        if (contadores[valor] >= MNIMA_CANTIDAD_GRUPO) {
            hayGrupos = true;
            gruposBuilder.append(Grupo.values()[contadores[valor]])
                        .append(" de ")
                        .append(valor)
                        .append("\n");

            for (int i = 0; i < cartas.length; i++) {
                if (cartas[i].getValor() == valor) {
                    cartasEnGrupo[i] = true;
                }
            }
        }
    }

    String escaleras = verificarEscaleras(cartasEnGrupo);
    if (!escaleras.isEmpty()) {
        hayGrupos = true;
        gruposBuilder.append(escaleras);
    }

    int puntajeCartasSueltas = 0;
    StringBuilder cartasSueltasBuilder = new StringBuilder("\nCartas sueltas:\n");
    for (int i = 0; i < cartas.length; i++) {
        if (!cartasEnGrupo[i]) {
        puntajeCartasSueltas += calcularPuntajeCarta(cartas[i]);
        cartasSueltasBuilder.append(cartas[i].getValor())
        .append(" de ")
        .append(cartas[i].getPinta())
        .append("\n");
    }
}

    if (hayGrupos || puntajeCartasSueltas > 0) {
        mensaje = gruposBuilder.toString();
        if (puntajeCartasSueltas > 0) {
            mensaje += cartasSueltasBuilder.toString()
            + "Puntaje de cartas sueltas: " + puntajeCartasSueltas + "\n";
        }
    }

    return mensaje;
}

private String verificarEscaleras(boolean[] cartasEnGrupo) {
    Map<String, List<Carta>> cartasPorPinta = new HashMap<>();
    for (Carta carta : cartas) {
        String pinta = carta.getPinta();
        cartasPorPinta.computeIfAbsent(pinta, k -> new ArrayList<>()).add(carta);
    }

    StringBuilder escalerasBuilder = new StringBuilder();

    for (List<Carta> cartasDePinta : cartasPorPinta.values()) {

        cartasDePinta.sort(Comparator.comparingInt(Carta::getValor));

        int contadorEscalera = 1;

        for (int i = 1; i < cartasDePinta.size(); i++) {
            int valorActual = cartasDePinta.get(i).getValor();
            int valorAnterior = cartasDePinta.get(i - 1).getValor();

            if (valorActual == valorAnterior + 1) {
                contadorEscalera++;
            } else if (valorActual != valorAnterior) {

                if (contadorEscalera >= 3) {
                    escalerasBuilder.append("Escalera de ")
                    .append(contadorEscalera).append(" cartas de ")
                    .append(cartasDePinta.get(i - 1).getPinta()).append(": ")
                    .append(cartasDePinta.get(i - contadorEscalera).getValor()).append(" a ")
                    .append(cartasDePinta.get(i - 1).getValor())
                    .append("\n");

                    for (int j = i - contadorEscalera; j < i; j++) {
                        int indiceCarta = Arrays.asList(cartas).indexOf(cartasDePinta.get(j));
                        cartasEnGrupo[indiceCarta] = true;
                    }
                }
                contadorEscalera = 1;
            }
        }

        if (contadorEscalera >= 3) {
            escalerasBuilder.append("Escalera de ")
            .append(contadorEscalera).append(" cartas de ").append(cartasDePinta.get(cartasDePinta.size() - 1).getPinta())
            .append(": ").append(cartasDePinta.get(cartasDePinta.size() - contadorEscalera).getValor())
            .append(" a ").append(cartasDePinta.get(cartasDePinta.size() - 1).getValor()).append("\n");

            for (int j = cartasDePinta.size() - contadorEscalera; j < cartasDePinta.size(); j++) {
                int indiceCarta = Arrays.asList(cartas).indexOf(cartasDePinta.get(j));
                cartasEnGrupo[indiceCarta] = true;
            }
        }
    }

    return escalerasBuilder.toString();
}

private int calcularPuntajeCarta(Carta carta) {
    int valor = carta.getValor();

    if (valor == 1 || valor == 11 || valor == 12 || valor == 13) {
        return 10;
    }

    return valor;
}

}
