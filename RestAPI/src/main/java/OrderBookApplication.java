import com.interview.processor.IRestAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.interview"})
public class OrderBookApplication {

    @RequestMapping("/health")
    String health() {
        return "OrderBookApplication is up and running";
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(OrderBookApplication.class, args);
    }

}
