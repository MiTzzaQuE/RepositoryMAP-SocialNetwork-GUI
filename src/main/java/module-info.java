module com.example.social_network_gui_v2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.pdfbox;

    exports com.example.social_network_gui_v2;
    exports com.example.social_network_gui_v2.controller;
    opens com.example.social_network_gui_v2.controller to javafx.fxml;

    opens com.example.social_network_gui_v2.domain to javafx.base;
}