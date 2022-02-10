package tools.descartes.teastore.orderprocessing.orderprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tools.descartes.teastore.orderprocessing.orderprocessor.service.OrderprocessorService;

// import tools.descartes.teastore.registryclient.RegistryClient;
import tools.descartes.teastore.registryclient.Service;
import tools.descartes.teastore.registryclient.loadbalancers.ServiceLoadBalancer;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class OrderprocessorApplication implements CommandLineRunner {

	@Autowired
    private OrderprocessorService pService;

	public static void main(String[] args) {
		ServiceLoadBalancer.preInitializeServiceLoadBalancers(Service.PERSISTENCE);
		SpringApplication.run(OrderprocessorApplication.class, args);
	}

	@Override
	public void run(String... args) {
		pService.StartService();
		
		while (pService.IsServiceRunning()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				//TODO: handle exception
			}
		}
	}
}
