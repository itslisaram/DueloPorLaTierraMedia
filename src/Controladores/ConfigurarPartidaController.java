package Controladores;

import Clases.Database;
import Clases.Jugador;
import Clases.JugadorPartida;
import Clases.Partida;
import Clases.Tablero;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurarPartidaController {
    private static final Logger LOGGER = Logger.getLogger(ConfigurarPartidaController.class.getName());

	@FXML
	private TextField nombreComunidad;

	@FXML
	private TextField nombreMordor;

	@FXML
	private void onComenzarPartida() {
		try {
			//Validar los nombres ingresados
			if (nombreComunidad.getText().isEmpty() || nombreMordor.getText().isEmpty()) {
				final Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Datos faltantes");
				alert.setHeaderText("Faltan datos para configurar la partida");
				alert.setContentText("Por favor, ingrese nombres válidos para ambos jugadores.");
				alert.showAndWait();
				return;
			}
			else {
				final String jugadorComunidad = nombreComunidad.getText();
				final String jugadorMordor = nombreMordor.getText();
				Database.insertarJugador(jugadorMordor);
				Database.insertarJugador(jugadorComunidad);
			}

			//Generar datos random para los jugadores
			final int edadJugador1 = (int) (Math.random() * (50 - 18 + 1) + 18); // Edad entre 18 y 50
			final int victoriasJugador1 = (int) (Math.random() * 101); // Número de victorias entre 0 y 100

			final int edadJugador2 = (int) (Math.random() * (50 - 18 + 1) + 18); // Edad entre 18 y 50
			final int victoriasJugador2 = (int) (Math.random() * 101); // Número de victorias entre 0 y 100

			//Crear los jugadores con los datos ingresados y aleatorios
			final Jugador jugador1 = new Jugador(1, nombreMordor.getText(), edadJugador1, victoriasJugador1);
			final Jugador jugador2 = new Jugador(2, nombreComunidad.getText(), edadJugador2, victoriasJugador2);

			// Generar el archivo HTML con los datos de ambos jugadores y guardarlo en la ruta
			final String nombreArchivo = new File("datos_partida.html").getAbsolutePath();
			guardarArchivoHTML(nombreArchivo, jugador1, jugador2);

			// Mostrar mensaje de éxito
			final Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Éxito");
			alert.setHeaderText("Archivo generado");
			alert.setContentText("El archivo se ha guardado correctamente tu carpeta.");
			alert.showAndWait();

			// Cargar la vista del tablero del juego
			cargarVistaTablero(jugador1, jugador2);

		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ocurrió un error al configurar la partida");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	private void guardarArchivoHTML(final String nombreFichero, final Jugador jugador1, final Jugador jugador2) {
		try (BufferedWriter buffWrit = new BufferedWriter(new FileWriter(nombreFichero))) {
			// Inicio del HTML
			buffWrit.write("<!DOCTYPE html>");
			buffWrit.newLine();
			buffWrit.write("<html lang=\"es\">");
			buffWrit.newLine();
			buffWrit.write("<head>");
			buffWrit.newLine();
			buffWrit.write("<meta charset=\"UTF-8\">");
			buffWrit.newLine();
			buffWrit.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
			buffWrit.newLine();
			buffWrit.write("<title>Datos de los Jugadores</title>");
			buffWrit.newLine();
			buffWrit.write("<style>");
			buffWrit.newLine();

			//Estilos CSS
			buffWrit.write(
					"body { font-family: Arial, sans-serif; background-color: #f4f4f9; color: #333; margin: 0; padding: 20px; }");
			buffWrit.newLine();
			buffWrit.write("table { width: 80%; margin: auto; border-collapse: collapse; }");
			buffWrit.newLine();
			buffWrit.write("th, td { padding: 10px; border: 1px solid #ddd; text-align: center; }");
			buffWrit.newLine();
			buffWrit.write("th { background-color: #4CAF50; color: white; }");
			buffWrit.newLine();

			buffWrit.write("</style>");
			buffWrit.newLine();
			buffWrit.write("</head>");
			buffWrit.newLine();
			buffWrit.write("<body>");
			buffWrit.newLine();
			buffWrit.write("<h1 style=\"text-align: center;\">Datos de los Jugadores</h1>");
			buffWrit.newLine();

			//Tabla con los datos de los jugadores
			buffWrit.write("<table>");
			buffWrit.newLine();
			buffWrit.write("<tr><th>ID</th><th>Nombre</th><th>Edad</th><th>Victorias</th></tr>");
			buffWrit.newLine();

			// Datos del Jugador 1
			buffWrit.write("<tr>");
			buffWrit.write("<td>" + jugador1.getId() + "</td>");
			buffWrit.write("<td>" + jugador1.getNombre() + "</td>");
			buffWrit.write("<td>" + jugador1.getEdad() + "</td>");
			buffWrit.write("<td>" + jugador1.getNumeroVictorias() + "</td>");
			buffWrit.write("</tr>");
			buffWrit.newLine();

			// datos del Jugador 2
			buffWrit.write("<tr>");
			buffWrit.write("<td>" + jugador2.getId() + "</td>");
			buffWrit.write("<td>" + jugador2.getNombre() + "</td>");
			buffWrit.write("<td>" + jugador2.getEdad() + "</td>");
			buffWrit.write("<td>" + jugador2.getNumeroVictorias() + "</td>");
			buffWrit.write("</tr>");
			buffWrit.newLine();

			// Fin de la tabla
			buffWrit.write("</table>");
			buffWrit.newLine();
			buffWrit.write("</body>");
			buffWrit.newLine();
			buffWrit.write("</html>");
		} catch (IOException e) {
			 LOGGER.info("Ocurrió un error al escribir el archivo HTML: " + e.getMessage());
		}
	}

	private void cargarVistaTablero(final Jugador jugador1, final Jugador jugador2) {
		try {
			//Cargar la vista del tablero
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/Partida.fxml"));
			final Parent root = loader.load();

			// pbtener el controlador del tablero
			final TableroController tableroController = loader.getController();

			//Crear la partida y configurar los jugadores
			final Tablero tablero = new Tablero(1, "Disposición inicial", "fase1");
			final Partida partida = new Partida(1, "12:00", "2025-01-01", tablero);
			final JugadorPartida jugadorPartida1 = new JugadorPartida(jugador1, partida, 0, "Sauron", null, null, null);
			final JugadorPartida jugadorPartida2 = new JugadorPartida(jugador2, partida, 0, "Comunidad", null, null, null);

			// Configurar los jugadores y el tablero en el controlador del tablero
			tableroController.inicializarTablero(jugadorPartida1, jugadorPartida2, tablero);

			//Cambiar a la nueva escena
			final Stage stage = (Stage) nombreComunidad.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Tablero de Juego");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error al cargar la vista del tablero", e);
			final Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No se pudo cargar la vista del tablero");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}
}
