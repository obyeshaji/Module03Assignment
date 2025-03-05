module edu.farmingdale.module3assignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.scripting;

    opens edu.farmingdale.module3assignment to javafx.fxml;
    exports edu.farmingdale.module3assignment;
}