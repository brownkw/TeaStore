package tools.descartes.teastore.orderprocessing.orderprocessor;

import java.util.TimerTask;
import java.lang.ref.SoftReference;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;

import tools.descartes.teastore.registryclient.Service;
import tools.descartes.teastore.registryclient.rest.LoadBalancedCRUDOperations;
import tools.descartes.teastore.entities.Product;

import tools.descartes.teastore.orderprocessing.orderprocessor.service.OrderprocessorService;

public class OrderprocessorTask extends TimerTask {

  @Override
  public void run() {
    System.out.println("Starting task at: "
        + LocalDateTime.ofInstant(Instant.ofEpochMilli(scheduledExecutionTime()), ZoneId.systemDefault()));

    Random r = new Random();
    int numItems = r.nextInt(50);
    int[] categoryIds = { 2, 3, 4, 5, 6 };

    int categoryId = categoryIds[r.nextInt(categoryIds.length)];

    List<Product> productlist = LoadBalancedCRUDOperations.getEntities(Service.PERSISTENCE,
        "products", Product.class, "category", categoryId, 0, numItems);

    if (productlist != null) {
      System.out.println("Found " + productlist.size() + " items.");
      productlist.stream().forEach((product) -> {
        System.out.println(product.toString());

        int mem_increment_mb = 10;
        if (System.getenv("MEM_INCREMENT_MB") != null) {
          try {
            mem_increment_mb = Integer.parseInt(System.getenv("MEM_INCREMENT_MB"));
          } catch (Exception e) {
            mem_increment_mb = 10;
          }
        }
        
        int memory_size = mem_increment_mb * 1024 * 1024;
        byte[] result = new byte[memory_size];
        OrderprocessorService.Stuff.add(new SoftReference<byte[]>(result));

      });
    }
  }
}
