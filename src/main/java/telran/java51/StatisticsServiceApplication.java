package telran.java51;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.internal.VisibleForTesting;
import com.mongodb.internal.VisibleForTesting.AccessModifier;

@SpringBootApplication
//@VisibleForTesting(otherwise = AccessModifier.PACKAGE)
public class StatisticsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatisticsServiceApplication.class, args);
	}

}
