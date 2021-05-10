import controller.FarmacistController;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Medicament;
import model.validators.MedicamentValidator;
import model.validators.Validator;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repository.*;
import service.IService;
import service.Service;

public class Start extends Application {



    static SessionFactory sessionFactory;
    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception "+e);
            e.printStackTrace();
            System.out.println("ERROR!!");
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initialize();
        FarmacistRepository farmacistRepository =new BDFarmacistRepository(sessionFactory);
        MedicRepository medicRepository =new BDMedicRepository(sessionFactory);
        ComandaRepository comandaRepository = new BDComandaRepository(sessionFactory);
        MedicamentRepository medicamentRepository = new BDMedicamentRepository(sessionFactory);
        MedicamentValidator medicamentValidator = new MedicamentValidator();
        IService service = new Service(farmacistRepository, medicRepository, comandaRepository, medicamentRepository, medicamentValidator);
        FXMLLoader LogInLoader = new FXMLLoader();
        LogInLoader.setLocation(getClass().getResource("/views/loginView.fxml"));
        AnchorPane LogInLayout = LogInLoader.load();
        LoginController loginController = LogInLoader.getController();
        Scene loginScene = new Scene(LogInLayout);
        primaryStage.setScene(loginScene);
        loginController.setService(service);

        primaryStage.show();
        primaryStage.setWidth(600);

    }
}
