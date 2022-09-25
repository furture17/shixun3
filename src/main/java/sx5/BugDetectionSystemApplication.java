package sx5;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("sx5/mapper")
public class BugDetectionSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(BugDetectionSystemApplication.class, args);
    }

}
