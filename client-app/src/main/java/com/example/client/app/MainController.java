package com.example.client.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class MainController {

    JSONPlaceHolderClient client;

    @Autowired
    public MainController(JSONPlaceHolderClient client) {
        this.client = client;
    }

    @GetMapping
    List<Post> getPosts() {
        return client.getPosts();
    }


}
