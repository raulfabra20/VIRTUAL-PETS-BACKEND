package S5.T2.virtual_pet_api_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VirtualPetApiBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualPetApiBackApplication.class, args);
	}

}
