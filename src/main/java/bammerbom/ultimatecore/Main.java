/*
 * This file is part of UltimateCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) Bammerbom
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bammerbom.ultimatecore;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class Main extends Application {

    Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stag) throws Exception {
        stage = stag;
        stage.setTitle("UltimateCore");
        stage.getIcons().add(new Image("assets/ultimatecore/icon.png"));

        stage.setWidth(450);
        stage.setResizable(false);

        //Text
        Label text = new Label();
        text.setText("Welcome to the UltimateCore hub! Please select the action you want to take.");
        GridPane.setConstraints(text, 0, 0, 4, 1);

        //Bukkit
        Button bukkit = new Button("Bukkit page");
        bukkit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://dev.bukkit.org/bukkit-plugins/ultimate_core"));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Failed to open web page.", "UltimateCore", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    showErrorMessage("Opening web pages is not supported on your OS.");
                }
            }
        });
        bukkit.getStyleClass().add("button-2");
        bukkit.setPrefWidth(90.0);
        GridPane.setConstraints(bukkit, 0, 3);

        //UC web
        Button ucweb = new Button("UltimateCore website");
        ucweb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://ultimatecore.org"));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Failed to open web page.", "UltimateCore", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    showErrorMessage("Opening web pages is not supported on your OS.");
                }
            }
        });
        ucweb.getStyleClass().add("button-2");
        GridPane.setConstraints(ucweb, 1, 3);

        //Sponge page
        Button sponge = new Button("Sponge page");
        sponge.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                showErrorMessage("This page is not available yet.");
            }
        });
        sponge.setCancelButton(true);
        sponge.getStyleClass().add("button-2");
        GridPane.setConstraints(sponge, 2, 3);

        //Close
        Button close = new Button("Close");
        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stage.close();
            }
        });
        close.getStyleClass().add("button-close");
        close.setPrefWidth(67);
        GridPane.setConstraints(close, 3, 3);

        //Report issue
        Button reportissue = new Button("Report issue");
        reportissue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://dev.bukkit.org/bukkit-plugins/ultimate_core/tickets/"));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Failed to open web page.", "UltimateCore", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    showErrorMessage("Opening web pages is not supported on your OS.");
                }
            }
        });
        reportissue.getStyleClass().add("button-1");
        GridPane.setConstraints(reportissue, 0, 2);

        //Features list
        Button features = new Button("Features list");
        features.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://ultimatecore.org/features"));
                    } catch (Exception ex) {
                        showErrorMessage("Failed to open web page.");
                    }
                } else {
                    showErrorMessage("Opening web pages is not supported on your OS.");
                }
            }
        });
        features.getStyleClass().add("button-1");
        features.setPrefWidth(142.0);
        GridPane.setConstraints(features, 1, 2);

        //Source
        Button source = new Button("Source");
        source.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://github.com/Bammerbom/UltimateCore"));
                    } catch (Exception ex) {
                        showErrorMessage("Failed to open web page.");
                    }
                } else {
                    showErrorMessage("Opening web pages is not supported on your OS.");
                }
            }
        });
        source.getStyleClass().add("button-1");
        source.setPrefWidth(94.0);
        GridPane.setConstraints(source, 2, 2);

        //MCStats
        Button mcstats = new Button("MCStats");
        mcstats.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://mcstats.org/plugin/UltimateCore"));
                    } catch (Exception ex) {
                        showErrorMessage("Failed to open web page.");
                    }
                } else {
                    showErrorMessage("Opening web pages is not supported on your OS.");
                }
            }
        });
        mcstats.getStyleClass().add("button-1");
        GridPane.setConstraints(mcstats, 3, 2);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(8);

        grid.getChildren().addAll(text, reportissue, features, mcstats, source, bukkit, ucweb, sponge, close);

        Scene scene = new Scene(grid);

        scene.getStylesheets().add("assets/ultimatecore/style.css");

        stage.setScene(scene);
        stage.show();
    }

    public void showErrorMessage(String message) {
        final Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("UltimateCore");
        window.setWidth(250);
        window.setResizable(false);

        Label label = new Label();
        label.setText(message);
        Button close = new Button("Close");
        close.getStyleClass().add("button-close");
        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                window.close();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, close);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("assets/ultimatecore/style.css");
        window.setScene(scene);
        window.showAndWait();
    }

}
