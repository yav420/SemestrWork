package org.example.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient extends Application {
    private ChatClientController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Chat Client");
        primaryStage.show();

        controller = loader.getController();

        // Подключаемся к серверу в отдельном потоке
        new Thread(() -> connectToServer("localhost", 12345)).start();
    }

    private void connectToServer(String serverAddress, int port) {
        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Передаём PrintWriter в контроллер
            Platform.runLater(() -> controller.initialize(out));

            // Читаем сообщения с сервера
            String message;
            while ((message = in.readLine()) != null) {
                String finalMessage = message;
                Platform.runLater(() -> controller.receiveMessage(finalMessage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}