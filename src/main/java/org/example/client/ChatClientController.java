package org.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.PrintWriter;

public class ChatClientController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageField;

    private PrintWriter out;

    // Инициализация для отправки сообщений на сервер
    public void initialize(PrintWriter out) {
        this.out = out;
    }

    // Метод для отображения сообщений в чате
    public void receiveMessage(String message) {
        chatArea.appendText(message + "\n"); // Добавляет новое сообщение в текстовое поле
    }

    // Метод для отправки сообщения
    @FXML
    public void sendMessage() {
        String message = messageField.getText();
        if (message.isEmpty()) return;

        out.println(message); // Отправляем сообщение на сервер
        messageField.clear(); // Очищаем поле ввода
    }

    // Метод для отображения информационных окон
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Метод для обработки регистрации
    @FXML
    public void registerUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Пожалуйста, введите имя пользователя и пароль.");
        } else {
            // Здесь будет логика регистрации
            System.out.println("Регистрация пользователя: " + username);
            showAlert("Успех", "Пользователь зарегистрирован!");
        }
    }

    // Метод для обработки входа
    @FXML
    public void loginUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Пожалуйста, введите имя пользователя и пароль.");
        } else {
            // Здесь будет логика входа
            System.out.println("Вход пользователя: " + username);
            // После входа можно скрыть поле ввода и кнопку
            messageField.setVisible(true);
        }
    }
}