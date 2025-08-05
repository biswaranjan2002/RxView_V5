package com.raymedis.rxviewui.modules.ImageProcessing.DrawToolsResource;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public interface IDrawTools {

    void removeAll();

    Boolean containsElement(Node element);

    void handleMousePressed(MouseEvent event);

    void handleMouseDragged(MouseEvent event) ;

    void handleCanvasReleased(MouseEvent mouseEvent);

    void hideAll();

    void unHideAll();
}
