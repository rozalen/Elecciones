/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elecciones;

import electionresults.model.ElectionResults;
import electionresults.model.Party;
import electionresults.model.PartyResults;
import electionresults.model.RegionResults;
import electionresults.persistence.io.DataAccessLayer;
import java.io.File;
import java.io.IOException;
import static java.lang.Integer.max;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.DragEvent;
import static javafx.scene.input.KeyCode.X;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author SRozalen
 */
public class FXMLDocumentController implements Initializable {

    private Label label;
    private ToggleGroup grupo;
    @FXML
    private ImageView mapa;
    @FXML
    private ComboBox<String> comarcas;
    private Image comunitat;
    private Stage stage;
    private Scene scene;
    private ElectionResults electionresults;
    private List<ElectionResults> evolucion;
    @FXML
    private ComboBox<Integer> years;
    private Label error_mapa;
    @FXML
    private PieChart piechart;
    @FXML
    private BarChart<String, Number> barchart;
    private String provincia = null;
    @FXML
    private BarChart<?, ?> evo_histo;
    @FXML
    private ProgressIndicator progressindicator;
    private Boolean first_histo = true;
    private Boolean first_evo = true;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Text cargando;
    private ToggleButton toggle1;
    private ToggleButton toggle2;
    private ToggleButton toggle3;
    private ToggleButton toggle4;
    private ToggleButton toggle5;
    private ToggleButton toggle6;
    private ToggleButton toggle7;
    private ToggleButton toggle8;
    @FXML
    private StackedBarChart<?, ?> stackedbarchart;
    @FXML
    private LineChart<?, ?> linechart;
    @FXML
    private ProgressBar progressbar2;
    @FXML
    private Text cargando2;
    private List<Button> buttons;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private NumberAxis yAxis;
    String STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5; ";
    String STYLE_PRESSED = "-fx-background-color: transparent; -fx-padding: 6 4 4 6; "
            + "-fx-font-size: 16px; -fx-font-weight:bold;";
    @FXML
    private Slider slider;
    @FXML
    private Text slider_text;
    @FXML
    private CategoryAxis ejeX_histo;
    private CategoryAxis ejeX_evo;
    @FXML
    private CategoryAxis ejeX_evoLine;
    @FXML
    private CategoryAxis ejeX_evoSta;

    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        File file = new File("src/Imagenes/principal.png");
        comunitat = new Image(file.toURI().toString());

        mapa.setImage(comunitat);

        years.getItems()
                .addAll(1995, 1999, 2003, 2007, 2011, 2015);
        comarcas.setDisable(true);
        progressindicator.setVisible(false);

        years.getSelectionModel().selectFirst();

        try {
            years_clicked(null);
        } catch (InterruptedException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void cargar(ElectionResults aux) {

        this.electionresults = aux;
        System.out.println("metodo cargar " + aux.getGlobalResults().toString());

    }

    @FXML
    private void mapa_clicked(MouseEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        Image img = mapa.getImage();
        int ready = (int) ((y * img.getHeight()) / mapa.getLayoutBounds().getMaxY());
        int readx = (int) ((x * img.getWidth()) / mapa.getLayoutBounds().getMaxX());

        PixelReader reader = img.getPixelReader();

        Color color = reader.getColor(readx, ready);
        int blue = (int) (color.getBlue() * 255);
        System.out.println("X: " + readx + " Y: " + ready);
        System.out.println("Blue: " + blue);

        switch (blue) {
            case 170:
                provincia = "Castellón";

                File castellon = new File("src/imagenes/castellon.png");
                Image img_cas = new Image(castellon.toURI().toString());
                mapa.setImage(img_cas);
                comarcas.getItems().clear();
                comarcas.getItems().add("");
                comarcas.getItems().addAll(electionresults.getProvinces().get("Castellón").getRegions());
                selected_year(true, false, provincia, null);
                comarcas.setDisable(false);
                break;
            case 175:
                provincia = "Valencia";

                File valencia = new File("src/imagenes/valencia.png");
                Image img_vlc = new Image(valencia.toURI().toString());
                mapa.setImage(img_vlc);
                comarcas.getItems().clear();
                comarcas.getItems().add("");
                comarcas.getItems().addAll(electionresults.getProvinces().get("Valencia").getRegions());
                selected_year(true, false, provincia, null);

                comarcas.setDisable(false);

                break;
            case 180:
                provincia = "Alicante";
                File alicante = new File("src/imagenes/alicante.png");
                Image img_ali = new Image(alicante.toURI().toString());
                mapa.setImage(img_ali);
                comarcas.getItems().clear();
                comarcas.getItems().add("");
                comarcas.getItems().addAll(electionresults.getProvinces().get("Alicante").getRegions());
                selected_year(true, false, provincia, null);

                comarcas.setDisable(false);

                break;
            default:
                mapa.setImage(comunitat);
                selected_year(false, false, null, null);
                comarcas.setDisable(true);

        }

    }

    private void selected_year(Boolean provincia, Boolean primero, String provincia_name, String comarca) {
        try {

            ObservableList<Series<String, Number>> allSeries = null;
            if (!primero) {

                allSeries = barchart.getData();

                for (XYChart.Series<String, Number> series : allSeries) {
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        Node node = data.getNode();
                        Parent parent = node.parentProperty().get();
                        if (parent != null && parent instanceof Group) {
                            Group group = (Group) parent;
                            group.getChildren().clear();
                        }
                    }
                }
                allSeries.clear();

                piechart.getData().clear();
                barchart.getData().clear();

            }
            List<PartyResults> party_results_bar;
            List<PartyResults> party_results_pie;

            if (provincia) {
                party_results_pie = electionresults.getProvinceResults(provincia_name).getPartyResultsSorted();
                if (comarca != null) {
                    party_results_bar = electionresults.getRegionResults(comarca).getPartyResultsSorted();
                } else {
                    party_results_bar = party_results_pie;
                }

            } else {
                party_results_bar = electionresults.getGlobalResults().getPartyResultsSorted();
                party_results_pie = party_results_bar;

            }
            ObservableList<PieChart.Data> datos_pie = FXCollections.observableArrayList();

            for (int i = 0; i < party_results_pie.size(); i++) {

                if (party_results_bar.get(i).getPercentage() >= slider.getValue()) {
                    XYChart.Series partido = new XYChart.Series();
                    partido.setName(party_results_bar.get(i).getParty());
                    XYChart.Data dato = new XYChart.Data(Integer.toString(years.getValue()), party_results_bar.get(i).getVotes());

                    dato.nodeProperty().addListener(new ChangeListener<Node>() {
                        @Override
                        public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                            if (node != null) {
                                displayLabelForData(dato);
                            }
                        }
                    });

                    partido.getData().add(dato);
                    barchart.getData().add(partido);

                }

                if (party_results_pie.get(i).getSeats() > 0) {
                    String label = party_results_pie.get(i).getParty() + "(" + party_results_pie.get(i).getSeats() + ")";
                    PieChart.Data dato = new PieChart.Data(label, party_results_pie.get(i).getSeats());
                    datos_pie.add(dato);
                }
            }

            piechart.setData(datos_pie);
            int i = 0;
            for (PieChart.Data data : datos_pie) {

                String nom_partido = data.getName();
                String[] nom_separado = nom_partido.split("\\(");
                nom_partido = nom_separado[0];
                System.out.println(nom_partido);
                switch (nom_partido) {
                    case "PP":

                        data.getNode().setStyle(
                                "-fx-pie-color: " + Party.PP.getHexColor() + ";"
                        );
                        break;
                    case "PSOE":

                        data.getNode().setStyle(
                                "-fx-pie-color: " + Party.PSOE.getHexColor() + ";"
                        );
                        break;
                    case "C's":

                        data.getNode().setStyle(
                                "-fx-pie-color: " + Party.CS.getHexColor() + ";"
                        );
                        break;
                    case "PODEMOS":

                        data.getNode().setStyle(
                                "-fx-pie-color: " + Party.PODEMOS.getHexColor() + ";"
                        );
                        break;
                    case "COMPROMÍS":

                        data.getNode().setStyle(
                                "-fx-pie-color: " + Party.COMPROMIS.getHexColor() + ";"
                        );
                        break;
                    case "UPYD":

                        data.getNode().setStyle(
                                "-fx-pie-color: " + Party.UPYD.getHexColor() + ";"
                        );
                        break;
                    case "EU-EV":

                        data.getNode().setStyle(
                                "-fx-pie-color: " + Party.EU.getHexColor() + ";"
                        );
                        break;
                    case "UV-FICVA-CCV":

                        data.getNode().setStyle(
                                "-fx-pie-color: " + Party.UV.getHexColor() + ";"
                        );
                        break;
                    default:

                }
                i++;
            }

            piechart.setLabelsVisible(true);
            piechart.setLegendVisible(false);

        } catch (Exception e) {

        }
    }

    @FXML
    private void years_clicked(ActionEvent event) throws InterruptedException {

        Task<ElectionResults> task = new Task<ElectionResults>() {
            @Override
            protected ElectionResults call() throws Exception {
                ElectionResults aux = DataAccessLayer.getElectionResults(years.getValue());
                System.out.println("in task");

                return aux;
            }
        };

        task.setOnSucceeded((e) -> {
            cargar(task.getValue());

            barchart.setVisible(true);
            piechart.setVisible(true);
            progressindicator.setVisible(false);

            try {
                if (comarcas.getValue() != null) {
                    System.out.println("comarca");
                    selected_year(true, false, provincia, comarcas.getValue());
                } else if (provincia != null) {
                    System.out.println("provincia");

                    selected_year(true, false, provincia, null);

                } else {
                    System.out.println("comunidad");

                    selected_year(false, false, null, null);
                }
            } catch (Exception except) {

            }
        });

        task.setOnRunning((e) -> {
            progressindicator.setVisible(true);

            progressindicator.progressProperty().bind(task.progressProperty());

        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        System.out.println("despues del task");

    }

    private void displayLabelForData(XYChart.Data<String, Number> data) {
        final Node node = data.getNode();
        final Text dataText = new Text(data.getYValue() + "");

        node.parentProperty().addListener((ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) -> {
            Group parentGroup = (Group) parent;

            try {
                parentGroup.getChildren().add(dataText);
            } catch (Exception exp) {
            }
        });

        node.boundsInParentProperty().addListener((ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) -> {
            dataText.setLayoutX(Math.round(bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2));
            dataText.setLayoutY(Math.round(bounds.getMinY() + bounds.getHeight() / 2 + dataText.prefHeight(-1) / 2) - 2.5);

        });
    }

    @FXML
    private void comarcas_select(ActionEvent event) {
        selected_year(true, false, provincia, comarcas.getValue());
    }

    private void participacion(List<ElectionResults> historico) {
        final int aux_years = historico.size();
        ejeX_histo.setCategories(FXCollections.<String>observableArrayList(Arrays.asList("1995", "1999", "2003", "2007", "2011", "2015")));
        List<String> zonas = Arrays.asList("Comunidad Valenciana", "Castellón", "Valencia", "Alicante");

        for (int i = 0; i < zonas.size(); i++) {
            XYChart.Series serie = new XYChart.Series();
            serie.setName(zonas.get(i));
            for (int j = 0; j < aux_years; j++) {
                if (!zonas.get(i).equals("Comunidad Valenciana")) {
                    double votos = historico.get(j).getProvinceResults(zonas.get(i)).getPollData().getVotes();
                    double censo = historico.get(j).getProvinceResults(zonas.get(i)).getPollData().getCensus();
                    double porcentaje = (votos / censo) * 100;
                    serie.getData().add(new XYChart.Data(Integer.toString(historico.get(j).getYear()), porcentaje));
                } else {
                    double votos = historico.get(j).getGlobalResults().getPollData().getVotes();
                    double censo = historico.get(j).getGlobalResults().getPollData().getCensus();
                    double porcentaje = (votos / censo) * 100;

                    serie.getData().add(new XYChart.Data(Integer.toString(historico.get(j).getYear()), porcentaje));
                }
            }
            evo_histo.getData().add(serie);
        }

    }

    @FXML
    private void historico_tab(Event event) {

        if (first_histo) {

            Task<List<ElectionResults>> task = new Task<List<ElectionResults>>() {
                @Override
                protected List<ElectionResults> call() throws Exception {
                    List<ElectionResults> aux = DataAccessLayer.getAllElectionResults();
                    System.out.println("in task 2");

                    return aux;
                }
            };

            task.setOnSucceeded((e) -> {
                participacion(task.getValue());
                evo_histo.setVisible(true);
                progressBar.setVisible(false);
                cargando.setVisible(false);
            });

            task.setOnRunning((e) -> {
                evo_histo.setVisible(false);
                progressBar.setVisible(true);
                cargando.setVisible(true);

                progressBar.progressProperty().bind(task.progressProperty());

            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

            first_histo = false;
        }

    }
    //historico partidos

    private void Evolucion() {
        final int aux_years = evolucion.size();

        Object[] arrayPartidos = Party.PARTIES_BY_NAME.keySet().toArray();
        ArrayList<String> listaPartidos = new ArrayList();
        ArrayList<Integer> yearlist = new ArrayList();
        ejeX_evoLine.setCategories(FXCollections.<String>observableArrayList(Arrays.asList("1995", "1999", "2003", "2007", "2011", "2015")));
        ejeX_evoSta.setCategories(FXCollections.<String>observableArrayList(Arrays.asList("1995", "1999", "2003", "2007", "2011", "2015")));

        for (Object arrayPartido : arrayPartidos) {
            String partido_aux = (String) arrayPartido;
            if (!listaPartidos.contains(Party.getPartyByName(partido_aux).getName())) {
                listaPartidos.add(Party.getPartyByName(partido_aux).getName());
            }
        }

        for (int l = 0; l < evolucion.size(); l++) {
            yearlist.add(evolucion.get(l).getYear());
        }

        Collections.sort(yearlist);

        for (int i = 0; i < listaPartidos.size(); i++) {
            String partido = listaPartidos.get(i);
            XYChart.Series asientos_series = new XYChart.Series();
            XYChart.Series votos_series = new XYChart.Series();

            asientos_series.setName(partido);
            votos_series.setName(partido);

            for (int k = 0; k < aux_years; k++) {
                int asientosPartido = 0;
                int votos = 0;
                String año = Integer.toString(yearlist.get(k));

                for (int j = 0; j < arrayPartidos.length; j++) {
                    String partido_aux = (String) arrayPartidos[j];
                    if (Party.getPartyByName(partido).getAcronyms().contains(partido_aux)) {
                        PartyResults partyResults = evolucion.get(k).getGlobalResults().getPartyResults(partido_aux);
                        if (partyResults != null) {
                            asientosPartido = asientosPartido + partyResults.getSeats();
                            votos = votos + partyResults.getVotes();
                        }
                    }
                }
                if (asientosPartido > 0) {
                    asientos_series.getData().add(new XYChart.Data(año, asientosPartido));
                }

                if (votos > 0) {
                    votos_series.getData().add(new XYChart.Data(año, votos));

                }
            }
            linechart.getData().add(votos_series);
            stackedbarchart.getData().add(asientos_series);
            linechart.setLegendSide(Side.RIGHT);
            stackedbarchart.setLegendSide(Side.RIGHT);
        }
        int i = 0;

        for (Button aux : buttons) {
            String partido_nom = listaPartidos.get(i);
            Party partido = Party.getPartyByName(partido_nom);
            Image logo = partido.getLogo();
            ImageView imageview = new ImageView(logo);
            imageview.setFitWidth(50);

            imageview.setFitHeight(50);
            aux.setText(partido_nom);
            aux.setGraphic(imageview);
            aux.setStyle(STYLE_PRESSED);
            aux.setTextFill(Color.PURPLE);
            
            i++;
        }

    }

    @FXML
    private void evolucion_tab(Event event) {

        if (first_evo) {

            buttons = new ArrayList<>();

            buttons.add(button1);
            buttons.add(button2);
            buttons.add(button3);
            buttons.add(button4);
            buttons.add(button5);
            buttons.add(button6);
            buttons.add(button7);
            buttons.add(button8);

            Task<List<ElectionResults>> task = new Task<List<ElectionResults>>() {
                @Override
                protected List<ElectionResults> call() throws Exception {
                    List<ElectionResults> aux = DataAccessLayer.getAllElectionResults();
                    System.out.println("in task 2");

                    return aux;
                }
            };

            task.setOnSucceeded((e) -> {
                evolucion = task.getValue();
                Evolucion();
                stackedbarchart.setVisible(true);
                linechart.setVisible(true);
                progressbar2.setVisible(false);
                cargando2.setVisible(false);
                for (Button aux : buttons) {

                    aux.setVisible(true);

                }

            });

            task.setOnRunning((e) -> {
                stackedbarchart.setVisible(false);
                linechart.setVisible(false);
                progressbar2.setVisible(true);
                cargando2.setVisible(true);
                for (Button aux : buttons) {

                    aux.setVisible(false);

                }

                progressbar2.progressProperty().bind(task.progressProperty());

            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

            first_evo = false;
        }
    }

    @FXML
    private void activar_partido(ActionEvent event) {
        String partido = ((Button) event.getSource()).getText();

        boolean encontrado = false;
        for (int i = 0; i < linechart.getData().size(); i++) {
            String buscar = linechart.getData().get(i).getName();
            if (buscar.equals(partido)) {
                encontrado = true;
                linechart.getData().remove(i);
                stackedbarchart.getData().remove(i);
                ((Button) event.getSource()).setStyle(STYLE_NORMAL);
                ((Button) event.getSource()).setTextFill(Color.BLACK);
                yAxis.setAutoRanging(false);
            }
        }
        if (encontrado == false) {
            ((Button) event.getSource()).setStyle(STYLE_PRESSED);
            ((Button) event.getSource()).setTextFill(Color.PURPLE);

            final int aux_years = evolucion.size();
            Object[] arrayPartidos = Party.PARTIES_BY_NAME.keySet().toArray();
            ArrayList<String> listaPartidos = new ArrayList();

            for (Object arrayPartido : arrayPartidos) {
                String partido_aux = (String) arrayPartido;
                if (!listaPartidos.contains(Party.getPartyByName(partido_aux).getName())) {
                    listaPartidos.add(Party.getPartyByName(partido_aux).getName());
                }
            }

            XYChart.Series asientos_series = new XYChart.Series();
            XYChart.Series votos_series = new XYChart.Series();

            asientos_series.setName(partido);
            votos_series.setName(partido);

            for (int k = 0; k < aux_years; k++) {
                int asientosPartido = 0;
                int votos = 0;
                String año = Integer.toString(evolucion.get(k).getYear());
                for (int j = 0; j < arrayPartidos.length; j++) {
                    String partido2 = (String) arrayPartidos[j];
                    if (Party.getPartyByName(partido).getAcronyms().contains(partido2)) {
                        PartyResults partyResults = evolucion.get(k).getGlobalResults().getPartyResults(partido2);
                        if (partyResults != null) {
                            asientosPartido = asientosPartido + partyResults.getSeats();
                            votos = votos + partyResults.getVotes();
                        }
                    }
                }
                if (asientosPartido > 0) {
                    asientos_series.getData().add(new XYChart.Data(año, asientosPartido));
                }

                if (votos > 0) {
                    votos_series.getData().add(new XYChart.Data(año, votos));

                }
            }
            linechart.getData().add(votos_series);
            stackedbarchart.getData().add(asientos_series);
        }
    }

    @FXML
    private void slider_change(MouseEvent event) {
        Double aux = Math.floor(slider.getValue() * 100) / 100;

        slider_text.setText(Double.toString(aux));

    }

    @FXML
    private void slider_released(MouseEvent event) {

        if (comarcas.getValue() != null) {
            System.out.println("comarca");
            selected_year(true, false, provincia, comarcas.getValue());
        } else if (provincia != null) {
            System.out.println("provincia");

            selected_year(true, false, provincia, null);

        } else {
            System.out.println("comunidad");

            selected_year(false, false, null, null);
        }
    }

}
