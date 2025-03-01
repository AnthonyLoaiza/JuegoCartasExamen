import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Carta {
    private int indice;
    private String nombreCarta;

    public Carta(String nombreCarta) {
        this.nombreCarta = nombreCarta;
    }

    public String getNombreArchivo() {
        return nombreCarta;
    }

    public int getValor() {
        String[] partes = nombreCarta.split("_");
        String numeroCarta = partes[0];
        return Integer.parseInt(numeroCarta);
    }

    public String getPinta() {
        String[] partes = nombreCarta.split("_");
        return partes[1].replace(".JPG", "");
    }

    // public NombreCarta getNombre() {
    //     int posicion = indice % 13;
    //     if (posicion == 0) {
    //         posicion = 13;
    //     }
    //     return NombreCarta.values()[posicion - 1];
    // }

    public void mostrar(JPanel pnl, int x, int y) {
        String rutaImagen = "imagenes/" + nombreCarta;
        ImageIcon imagen = new ImageIcon(getClass().getResource(rutaImagen));

        JLabel lbl = new JLabel(imagen);
        lbl.setBounds(x, y, imagen.getIconWidth(), imagen.getIconHeight());
        pnl.add(lbl);
    }

}
