package com.raymedis.rxviewui.modules.print.Layout;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LayoutIconCreator {

    private static final LayoutIconCreator instance = new LayoutIconCreator();

    public static LayoutIconCreator getInstance() {
        return instance;
    }

    private static final Logger logger = LoggerFactory.getLogger(LayoutIconCreator.class);

    private static final double FIXED_SPACING = 2.0;

    public Node createLayout(String layoutCode, double width, double height) {
        //logger.debug("Creating layout with code: {}, width: {}, height: {}", layoutCode, width, height);

        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(width, height);
        gridPane.setMinSize(width, height);
        gridPane.setMaxSize(width, height);
        gridPane.setAlignment(Pos.CENTER);

        Pattern pattern = Pattern.compile("^(STANDARD|ROW|COL)\\\\(\\d+),(\\d+)(?:,(\\d+))?(?:,(\\d+))?$");
        Matcher matcher = pattern.matcher(layoutCode);

        if (matcher.find()) {
            String type = matcher.group(1);
            int value1 = Integer.parseInt(matcher.group(2));
            int value2 = Integer.parseInt(matcher.group(3));
            Integer value3 = matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : null;
            Integer value4 = matcher.group(5) != null ? Integer.parseInt(matcher.group(5)) : null;

            // Validate that STANDARD layout only has exactly two numbers
            if ("STANDARD".equals(type) && (value3 != null || value4 != null)) {
                logger.warn("Invalid layout format: STANDARD layout must have exactly 2 values.");
                return null;
            }

            // Process based on type
            Node layoutNode = null;
            switch (type) {
                case "STANDARD":
                    layoutNode = createStandardLayout(value1, value2, width, height);
                    break;
                case "ROW":
                    // ROW layout with variable number of parameters
                    if (value3 != null && value4 != null) {
                        layoutNode = createRowLayout(width, height, value1, value2, value3, value4);
                    } else if (value3 != null) {
                        layoutNode = createRowLayout(width, height, value1, value2, value3);
                    } else {
                        layoutNode = createRowLayout(width, height, value1, value2);
                    }
                    break;
                case "COL":
                    // COL layout with variable number of parameters
                    if (value3 != null && value4 != null) {
                        layoutNode = createColumnLayout(width, height, value1, value2, value3, value4);
                    } else if (value3 != null) {
                        layoutNode = createColumnLayout(width, height, value1, value2, value3);
                    } else {
                        layoutNode = createColumnLayout(width, height, value1, value2);
                    }
                    break;
                default:
                    logger.warn("Unknown layout type: {}", type);
                    return null;
            }

            gridPane.getChildren().add(layoutNode);
        }
        else {
            logger.warn("Invalid layout format: {}", layoutCode);
            return null;
        }

        return gridPane;
    }


    private VBox createStandardLayout(int columns, int rows, double totalWidth, double totalHeight) {
        // Calculate available space after accounting for spacing
        double availableHeight = totalHeight - (FIXED_SPACING * (rows - 1));
        double rowHeight = availableHeight / rows;

        VBox container = new VBox();
        container.setPrefSize(totalWidth, totalHeight);
        container.setMaxSize(totalWidth, totalHeight);
        container.setMinSize(totalWidth, totalHeight);
        container.setAlignment(Pos.CENTER);
        container.setSpacing(FIXED_SPACING);

        // All rows have the same number of columns (left-right symmetry)
        for (int i = 0; i < rows; i++) {
            HBox rowContainer = createUniformRow(totalWidth, rowHeight, columns);
            container.getChildren().add(rowContainer);
        }

        return container;
    }


    private VBox createRowLayout(double totalWidth, double totalHeight, int... rowsConfig) {
        // Calculate available height after accounting for spacing
        double availableHeight = totalHeight - (FIXED_SPACING * (rowsConfig.length - 1));
        double rowHeight = availableHeight / rowsConfig.length;

        VBox container = new VBox();
        container.setPrefSize(totalWidth, totalHeight);
        container.setMaxSize(totalWidth, totalHeight);
        container.setMinSize(totalWidth, totalHeight);
        container.setAlignment(Pos.CENTER);
        container.setSpacing(FIXED_SPACING);

        // For logging and debugging
        for (int i = 0; i < rowsConfig.length; i++) {
            //logger.info("ROW: index {}, cells {}, rowHeight {}, totalWidth {}", i, rowsConfig[i], rowHeight, totalWidth);

            HBox rowContainer = createUniformRow(totalWidth, rowHeight, rowsConfig[i]);
            container.getChildren().add(rowContainer);
        }

        return container;
    }


    private HBox createColumnLayout(double totalWidth, double totalHeight, int... columnsConfig) {
        // Calculate available width after accounting for spacing
        double availableWidth = totalWidth - (FIXED_SPACING * (columnsConfig.length - 1));
        double columnWidth = availableWidth / columnsConfig.length;

        HBox container = new HBox();
        container.setPrefSize(totalWidth, totalHeight);
        container.setMaxSize(totalWidth, totalHeight);
        container.setMinSize(totalWidth, totalHeight);
        container.setAlignment(Pos.CENTER);
        container.setSpacing(FIXED_SPACING);
        container.setId("subGridlayout");

        // For logging and debugging
        for (int i = 0; i < columnsConfig.length; i++) {
           //logger.info("COL: index {}, cells {}, columnWidth {}, totalHeight {}",i, columnsConfig[i], columnWidth, totalHeight);

            VBox columnContainer = createUniformColumn(columnWidth, totalHeight, columnsConfig[i]);
            container.getChildren().add(columnContainer);
        }

        container.setMouseTransparent(true);
        return container;
    }


    private HBox createUniformRow(double totalWidth, double height, int cells) {
        // Calculate available width after spacing
        double availableWidth = totalWidth - (FIXED_SPACING * (cells - 1));
        double cellWidth = availableWidth / cells;

        HBox row = new HBox();
        row.setPrefSize(totalWidth, height);
        row.setMaxSize(totalWidth, height);
        row.setMinSize(totalWidth, height);
        row.setAlignment(Pos.CENTER);
        row.setSpacing(FIXED_SPACING);

        for (int i = 0; i < cells; i++) {
            GridPane cell = createCell(cellWidth, height);
            row.getChildren().add(cell);
        }

        return row;
    }


    private VBox createUniformColumn(double width, double totalHeight, int cells) {
        // Calculate available height after spacing
        double availableHeight = totalHeight - (FIXED_SPACING * (cells - 1));
        double cellHeight = availableHeight / cells;

        VBox column = new VBox();
        column.setPrefSize(width, totalHeight);
        column.setMaxSize(width, totalHeight);
        column.setMinSize(width, totalHeight);
        column.setAlignment(Pos.CENTER);
        column.setSpacing(FIXED_SPACING);

        for (int i = 0; i < cells; i++) {
            GridPane cell = createCell(width, cellHeight);
            column.getChildren().add(cell);
        }

        return column;
    }


    private GridPane createCell(double width, double height) {
        GridPane cell = new GridPane();
        cell.setPrefSize(width, height);
        cell.setMaxSize(width, height);
        cell.setMinSize(width, height);
        cell.setId("layout");
        return cell;
    }
}