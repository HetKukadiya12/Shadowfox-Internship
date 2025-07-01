package com.example.inventoryfx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Inventory Manager built with JavaFX.
 * Generated on 2025-06-26.
 */
public class InventoryManagerFX extends Application {

    private final ObservableList<InventoryItem> data = FXCollections.observableArrayList();
    private int nextId = 1;
    private Label totalLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        TableView<InventoryItem> table = new TableView<>();
        TableColumn<InventoryItem, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<InventoryItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<InventoryItem, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        table.getColumns().addAll(idCol, nameCol, qtyCol);
        table.setItems(data);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField qtyField = new TextField();
        qtyField.setPromptText("Qty");

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");

        HBox input = new HBox(5, nameField, qtyField, addBtn, updateBtn, deleteBtn);
        input.setPadding(new Insets(10));

        totalLabel = new Label("Total Qty: 0");
        BorderPane root = new BorderPane(table, input, null, totalLabel, null);

        addBtn.setOnAction(e -> {
            try {
                int qty = Integer.parseInt(qtyField.getText().trim());
                String name = nameField.getText().trim();
                if (!name.isEmpty()) {
                    data.add(new InventoryItem(nextId++, name, qty));
                    nameField.clear();
                    qtyField.clear();
                    updateTotal();
                }
            } catch (NumberFormatException ex) {
                showAlert("Quantity must be a number");
            }
        });

        updateBtn.setOnAction(e -> {
            InventoryItem selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Select a row first");
                return;
            }
            try {
                int qty = Integer.parseInt(qtyField.getText().trim());
                String name = nameField.getText().trim();
                selected.setName(name);
                selected.setQuantity(qty);
                table.refresh();
                updateTotal();
            } catch (NumberFormatException ex) {
                showAlert("Quantity must be a number");
            }
        });

        deleteBtn.setOnAction(e -> {
            InventoryItem selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                data.remove(selected);
                updateTotal();
            } else {
                showAlert("Select a row first");
            }
        });

        stage.setTitle("Inventory Manager - JavaFX v2");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    private void updateTotal() {
        int total = data.stream().mapToInt(InventoryItem::getQuantity).sum();
        totalLabel.setText("Total Qty: " + total);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
