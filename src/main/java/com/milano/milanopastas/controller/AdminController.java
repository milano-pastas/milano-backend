package com.milano.milanopastas.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @GetMapping("/status")
    public String status() {
        return "Panel admin operativo ✅";
    }
}
