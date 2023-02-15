package carteira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CarteiraApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarteiraApplication.class, args);

        System.out.println(new BCryptPasswordEncoder().encode("teste"));
    }
}
