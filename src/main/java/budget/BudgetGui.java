package budget;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BudgetGui extends Application {

    /**
     * A general way of spending money in a budget.
     * 
     * @author dbgod
     *
     */
    interface Category {
        /**
         * Name of this category's type of spending.
         */
        String getName();

        /**
         * How much of this category is allotted in budget.
         */
        int getPercentage();

        double getAmount();

        final class Default implements Category {
            private final String mName;
            private final int mPercentage;
            private final int mTotalAmount;

            public Default(final String name, final int percentage, final int totalAmount) {
                mName = name;
                mPercentage = percentage;
                mTotalAmount = totalAmount;
            }

            @Override
            public String getName() {
                return mName;
            }

            /**
             * 
             */
            @Override
            public int getPercentage() {
                return mPercentage;
            }

            @Override
            public double getAmount() {
                return (mPercentage / 100.0) * mTotalAmount;
            }
        }

        static Category gifts(final int totalAmount) {
            return new Category.Default("Gifts", 10, totalAmount);
        }

        static Category saving(final int totalAmount) {
            return new Category.Default("Saving", 5, totalAmount);
        }

        static Category housing(final int totalAmount) {
            return new Category.Default("Housing", 25, totalAmount);
        }

        static Category utilities(final int totalAmount) {
            return new Category.Default("Utilities", 5, totalAmount);
        }

        static Category food(final int totalAmount) {
            return new Category.Default("Food", 5, totalAmount);
        }

        static Category transportation(final int totalAmount) {
            return new Category.Default("Transportation", 10, totalAmount);
        }

        static Category clothing(final int totalAmount) {
            return new Category.Default("Clothing", 10, totalAmount);
        }

        static Category medical(final int totalAmount) {
            return new Category.Default("Medical/Health", 5, totalAmount);
        }

        static Category personal(final int totalAmount) {
            return new Category.Default("Personal", 5, totalAmount);
        }

        static Category recreation(final int totalAmount) {
            return new Category.Default("Recreation", 5, totalAmount);
        }

        static Category debts(final int totalAmount) {
            return new Category.Default("Debts", 5, totalAmount);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        final GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        final Label amountLabel = new Label("Income:");
        grid.add(amountLabel, 0, 0);
        final TextField amountField = new TextField();
        final int totalAmount = 1;
        amountField.setText(Integer.toString(totalAmount));
        grid.add(amountField, 1, 0);

        final Label percentLeftLabel = new Label("Percent Left:");
        grid.add(percentLeftLabel, 0, 1);
        final TextField percentLeftField = new TextField();
        percentLeftField.setEditable(false);
        Integer percentLeft = 100;

        ObservableList<Category> categories = FXCollections.observableArrayList(Category.gifts(totalAmount),
                Category.saving(totalAmount), Category.housing(totalAmount), Category.utilities(totalAmount),
                Category.food(totalAmount), Category.transportation(totalAmount), Category.clothing(totalAmount),
                Category.medical(totalAmount), Category.personal(totalAmount), Category.recreation(totalAmount),
                Category.debts(totalAmount));
        for (final Category category : categories) {
            percentLeft = percentLeft - category.getPercentage();
        }
        percentLeftField.setText(Integer.toString(percentLeft));
        grid.add(percentLeftField, 1, 1);

        final TableView<Category> table = new TableView<>();
        table.setItems(categories);
        table.setEditable(true);

        final TableColumn<Category, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));

        final TableColumn<Category, Integer> percentageColumn = new TableColumn<>("Percentage");
        percentageColumn.setCellValueFactory(new PropertyValueFactory<Category, Integer>("percentage"));

        final TableColumn<Category, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<Category, Double>("amount"));

        final ObservableList columns = table.getColumns();
        columns.addAll(categoryColumn, percentageColumn, amountColumn);
        grid.add(table, 0, 2, 2, 1);

        final BorderPane border = new BorderPane();
        final MenuBar menuBar = new MenuBar();
        final Menu fileMenu = new Menu("File");
        final ObservableList<MenuItem> fileMenuItems = fileMenu.getItems();
        fileMenuItems.add(new MenuItem("Open"));
        final MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(a -> {
            final FileChooser fileChooser = new FileChooser();
            final File file = fileChooser.showSaveDialog(stage);
            if (file == null) {
                throw new IllegalArgumentException("No file chosen");
            }
            try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (final Category category : categories) {
                    writer.write(category.getName());
                    writer.write(",");
                    writer.write(Integer.toString(category.getPercentage()));
                    writer.write(",");
                    writer.write(Double.toString(category.getAmount()));
                    writer.write(System.getProperty("line.separator"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileMenuItems.add(saveItem);
        menuBar.getMenus().add(fileMenu);
        border.setTop(menuBar);
        border.setCenter(grid);

        final Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
