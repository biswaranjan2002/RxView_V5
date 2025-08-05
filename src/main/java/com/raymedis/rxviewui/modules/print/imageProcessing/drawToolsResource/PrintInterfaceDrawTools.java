package com.raymedis.rxviewui.modules.print.imageProcessing.drawToolsResource;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public interface PrintInterfaceDrawTools {

    void removeAll();

    Boolean containsElement(Node element);

    void handleMousePressed(MouseEvent event);

    void handleMouseDragged(MouseEvent event) ;

    void handleCanvasReleased(MouseEvent mouseEvent);

    void hideAll(PrintCanvasMainChange printCanvasMainChange);

    void unHideAll();

}
