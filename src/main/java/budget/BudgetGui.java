package budget;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class BudgetGui extends Application {

    interface Budget {
        int totalAmount();

        int percentUnallocated();

        Set<String> categories();

        int percentAllocatedTo(String category);

        final class Default implements Budget {
            private final int mTotalAmount;
            private final List<Category> mCategories;

            public Default(final int totalAmount, final List<Category> categories) {
                mTotalAmount = totalAmount;
                mCategories = categories;
            }

            @Override
            public int totalAmount() {
                return mTotalAmount;
            }

            @Override
            public int percentUnallocated() {
                int percentLeft = 100;
                for (final Category category : mCategories) {
                    percentLeft = percentLeft - category.percentageProperty().getValue();
                }
                return percentLeft;
            }

            @Override
            public Set<String> categories() {
                final Set<String> names = new HashSet<>();
                for (final Category category : mCategories) {
                    final String name = category.getName();
                    names.add(name);
                }
                return names;
            }

            @Override
            public int percentAllocatedTo(final String category) {

                return 0;
            }
        }
    }

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

        int getPercentage();

        void setPercentage(int percentage);

        /**
         * What portion of budget this category has.
         */
        SimpleIntegerProperty percentageProperty();

        /**
         * Dollar amount of portion of budget this category has.
         */
        double getAmount();

        SimpleDoubleProperty amountProperty();

        void setAmount(double amount);

        void setTotalAmount(int totalAmount);

        final class Default implements Category {
            private final String mName;
            private final SimpleIntegerProperty mPercentage;
            private final SimpleIntegerProperty mTotalAmount;
            private final SimpleDoubleProperty mAmount;

            public Default(final String name, final int percentage, final int totalAmount) {
                mName = name;
                mPercentage = new SimpleIntegerProperty(percentage);
                mTotalAmount = new SimpleIntegerProperty(totalAmount);
                mAmount = new SimpleDoubleProperty((percentage / 100.0) * totalAmount);
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
                return mPercentage.getValue();
            }

            @Override
            public void setPercentage(int percentage) {
                mPercentage.setValue(percentage);
            }

            /**
             * 
             */
            @Override
            public SimpleIntegerProperty percentageProperty() {
                return mPercentage;
            }

            @Override
            public double getAmount() {
                return mAmount.getValue();
            }

            @Override
            public void setAmount(double amount) {
                mAmount.set(amount);
            }

            @Override
            public SimpleDoubleProperty amountProperty() {
                mAmount.setValue((mPercentage.getValue() / 100.0) * mTotalAmount.getValue());
                return mAmount;
            }

            @Override
            public void setTotalAmount(int totalAmount) {
                mTotalAmount.setValue(totalAmount);
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

    interface Categories {
        Category category(final String name);

        void setPercentage(String category, int percentage);

        void updateTotal(int amount);

        int percentageLeft();

        TableView<Category> table();

        final class Default implements Categories {
            private final ObservableList<Category> mCategories;

            public Default(final ObservableList<Category> categories) {
                mCategories = categories;
            }

            @Override
            public Category category(final String name) {
                for (final Category category : mCategories) {
                    if (name.equals(category.getName())) {
                        return category;
                    }
                }
                throw new IllegalArgumentException("Unknown category '" + name + "'");
            }

            @Override
            public void setPercentage(final String name, final int percentage) {
                final Category category = category(name);
                category.setPercentage(percentage);
            }

            @Override
            public void updateTotal(final int newTotal) {
                for (final Category category : mCategories) {
                    category.setTotalAmount(newTotal);
                    System.out.println(category.getAmount());
                }
            }

            @Override
            public int percentageLeft() {
                int total = 100;
                for (final Category category : mCategories) {
                    final int percentage = category.percentageProperty().getValue();
                    total -= percentage;
                }
                return total;
            }

            @Override
            public TableView<Category> table() {
                final TableView<Category> table = new TableView<>();

                table.setItems(mCategories);
                table.setEditable(true);

                final TableColumn<Category, String> categoryColumn = new TableColumn<>("Category");
                categoryColumn.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));

                final TableColumn<Category, Integer> percentageColumn = new TableColumn<>("Percentage");
                percentageColumn.setCellValueFactory(new PropertyValueFactory<Category, Integer>("percentage"));
                percentageColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
                    @Override
                    public String toString(Integer i) {
                        return Integer.toString(i);
                    }

                    @Override
                    public Integer fromString(String string) {
                        return Integer.parseInt(string);
                    }
                }));
                percentageColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Category, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Category, Integer> t) {
                        t.getRowValue().setPercentage(t.getNewValue());
                    }
                });
                final TableColumn<Category, Double> amountColumn = new TableColumn<>("Amount");
                amountColumn.setCellValueFactory(new PropertyValueFactory<Category, Double>("amount"));

                final ObservableList columns = table.getColumns();
                columns.addAll(categoryColumn, percentageColumn, amountColumn);
                return table;
            }
        }

        static Categories standard(final int totalAmount) {
            ObservableList<Category> categories = FXCollections.observableArrayList(Category.gifts(totalAmount),
                    Category.saving(totalAmount), Category.housing(totalAmount), Category.utilities(totalAmount),
                    Category.food(totalAmount), Category.transportation(totalAmount), Category.clothing(totalAmount),
                    Category.medical(totalAmount), Category.personal(totalAmount), Category.recreation(totalAmount),
                    Category.debts(totalAmount));
            return new Categories.Default(categories);
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

        final Categories categories = Categories.standard(totalAmount);
        percentLeftField.setText(Integer.toString(categories.percentageLeft()));
        grid.add(percentLeftField, 1, 1);

        final TableView<Category> table = categories.table();
        amountField.setOnAction(a -> {
            final String string = amountField.getText();
            final int total = Integer.parseInt(string);
            categories.updateTotal(total);
            table.refresh(); // FIXME Shouldn't need to call refresh manually
        });

        grid.add(table, 0, 2, 2, 1);

        final BorderPane border = new BorderPane();
        final MenuBar menuBar = new MenuBar();
        final Menu fileMenu = new Menu("File");
        final ObservableList<MenuItem> fileMenuItems = fileMenu.getItems();
        fileMenuItems.add(new MenuItem("Open"));
        final MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(a -> {
            final FileChooser chooser = new FileChooser();
            final ExtensionFilter textFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            final ObservableList<ExtensionFilter> filters = chooser.getExtensionFilters();
            filters.add(textFilter);
            final File file = chooser.showSaveDialog(stage);
            if (file == null) {
                throw new IllegalArgumentException("No file chosen");
            }
            try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(String.format("%s%n", amountField.getText()));
                for (final Category category : table.getItems()) {
                    writer.write(category.getName());
                    writer.write(",");
                    writer.write(Integer.toString(category.percentageProperty().getValue()));
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
