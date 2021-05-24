import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Comanda;
import model.Medicament;
import model.validators.ComandaValidator;
import model.validators.MedicamentValidator;
import model.validators.Validator;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repository.*;
import service.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

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

        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.properties"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.properties "+ e);
        }

        FarmacistRepository farmacistRepository =new BDFarmacistRepository(sessionFactory);
        MedicRepository medicRepository =new BDMedicRepository(sessionFactory);
        MedicamentRepository medicamentRepository = new BDMedicamentRepository(sessionFactory);
        MedicamentComandaRepository medicamentComandaRepository = new BDMedicamentComandaRepository(props, medicamentRepository);
        ComandaRepository comandaRepository = new BDComandaRepositoryClasic(props, medicamentComandaRepository);


        Validator<Medicament> medicamentValidator = new MedicamentValidator();
        Validator<Comanda> comandaValidator = new ComandaValidator();

        Service service = new Service(farmacistRepository, medicRepository, comandaRepository, medicamentRepository, medicamentComandaRepository, medicamentValidator, comandaValidator);

        FXMLLoader LogInLoader = new FXMLLoader();
        LogInLoader.setLocation(getClass().getResource("/views/loginView.fxml"));
        AnchorPane LogInLayout = LogInLoader.load();
        LoginController loginController = LogInLoader.getController();
        Scene loginScene = new Scene(LogInLayout);
        primaryStage.setScene(loginScene);
        loginController.setService(service);


        primaryStage.show();

    }
}
