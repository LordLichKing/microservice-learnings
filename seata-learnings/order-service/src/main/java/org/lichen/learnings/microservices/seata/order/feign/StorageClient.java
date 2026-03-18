package org.lichen.learnings.microservices.seata.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "storage-service")
public interface StorageClient {

    @GetMapping("/storage/decrease")
    String decrease(@RequestParam("productId") String productId, @RequestParam("count") Integer count);
}
