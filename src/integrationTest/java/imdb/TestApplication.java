package imdb;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.imdb.dao.impl", "com.imdb.dao"})
@EntityScan("com.imdb.dao.impl")
@ComponentScan("com.imdb.dao.impl")
public class TestApplication {
}
