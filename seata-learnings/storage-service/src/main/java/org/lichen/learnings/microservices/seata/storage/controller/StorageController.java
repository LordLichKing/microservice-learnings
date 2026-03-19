package org.lichen.learnings.microservices.seata.storage.controller;

import org.lichen.learnings.microservices.seata.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/decrease")
    public String decrease(@RequestParam("productId") String productId, @RequestParam("count") Integer count) {
        storageService.decrease(productId, count);
        return "库存扣减成功";
    }
}
