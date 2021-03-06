package com.ionut.ciuta.posd1.controller;

import com.ionut.ciuta.posd1.dto.Message;
import com.ionut.ciuta.posd1.exception.ExceptionWithStatusCode;
import com.ionut.ciuta.posd1.service.ManagementService;
import com.ionut.ciuta.posd1.service.ResourceAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * ionutciuta24@gmail.com on 26.10.2017.
 */
@Controller
public class ResourceController {
    private static final Logger log = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ResourceAccessService resourceAccessService;

    @Autowired
    private ManagementService managementService;

    @GetMapping("/app")
    public String home() {
        return "home.html";
    }

    @PostMapping("sci/hw/resource/{user}/create")
    @ResponseBody
    public ResponseEntity<Message> createResource(@PathVariable String user,
                                                  @RequestBody String pass,
                                                  @RequestParam String name,
                                                  @RequestParam(required = false) String value,
                                                  @RequestParam Integer type) {
        try {
            resourceAccessService.create(user, pass, name, value);
            return ResponseEntity.ok().build();
        } catch (ExceptionWithStatusCode e) {
            log.error(
                    "writeResource failed with {} for {} {} {} {}",
                    e.getClass().getSimpleName(), user, pass, name, value);
            return new ResponseEntity<>(new Message(e.text), e.status);
        }
    }

    @PostMapping("sci/hw/resource/{user}/read")
    @ResponseBody
    public ResponseEntity<Message> readResource(@PathVariable String user,
                                                @RequestBody String pass,
                                                @RequestParam String name) {
        try {
            return ResponseEntity.ok(new Message(resourceAccessService.read(user, pass, name)));
        } catch (ExceptionWithStatusCode e) {
            log.error(
                    "readResource failed with {} for {} {} {}",
                    e.getClass().getSimpleName(), user, pass, name);
            return new ResponseEntity<>(new Message(e.text), e.status);
        }
    }

    @PostMapping("sci/hw/resource/{user}/write")
    @ResponseBody
    public ResponseEntity<Message> writeResource(@PathVariable String user,
                                                 @RequestBody String pass,
                                                 @RequestParam String name,
                                                 @RequestParam String value) {
        try {
            resourceAccessService.write(user, pass, name, value);
            return ResponseEntity.ok().build();
        } catch (ExceptionWithStatusCode e) {
            log.error(
                    "writeResource failed with {} for {} {} {} {}",
                    e.getClass().getSimpleName(), user, pass, name, value);
            return new ResponseEntity<>(new Message(e.text), e.status);
        }
    }

    @PostMapping("sci/hw/resource/{user}/rights")
    @ResponseBody
    public ResponseEntity<Message> changeRights(@PathVariable String user,
                                                @RequestBody String pass,
                                                @RequestParam String name,
                                                @RequestParam String rights) {
        try {
            resourceAccessService.changeRights(user, pass, name, rights);
            return ResponseEntity.ok().build();
        } catch (ExceptionWithStatusCode e) {
            log.error(
                    "changeRights failed with {} for {} {} {} {} {}",
                    e.getClass().getSimpleName(), user, pass, name, rights);
            return new ResponseEntity<>(new Message(e.text), e.status);
        }
    }

    @PostMapping("sci/hw/resource/{user}/create_role")
    @ResponseBody
    public ResponseEntity<Message> createRole(@PathVariable String user,
                                              @RequestBody String pass,
                                              @RequestParam String role) {
        try {
            managementService.createRole(user, pass, role);
            return ResponseEntity.ok().build();
        } catch (ExceptionWithStatusCode e) {
            log.error("createRole: {}, {}, {}", user, pass, role);
            return new ResponseEntity<>(new Message(e.text), e.status);
        }
    }

    @PostMapping("sci/hw/resource/{user}/role_rights")
    @ResponseBody
    public ResponseEntity<Message> changeRoleRights(@PathVariable String user,
                                                    @RequestBody String pass,
                                                    @RequestParam String role,
                                                    @RequestParam String rights) {
        try {
            managementService.changeRights(user, pass, role, rights);
            return ResponseEntity.ok().build();
        } catch (ExceptionWithStatusCode e) {
            log.error("changeRoleRights: {}, {}, {}, {}", user, pass, role, rights);
            return new ResponseEntity<>(new Message(e.text), e.status);
        }
    }

    @PostMapping("sci/hw/resource/{user}/assign_role")
    @ResponseBody
    public ResponseEntity<Message> assignRole(@PathVariable String user,
                                              @RequestBody String pass,
                                              @RequestParam String role,
                                              @RequestParam String assignee) {
        try {
            managementService.assignRole(user, pass, assignee, role);
            return ResponseEntity.ok().build();
        } catch (ExceptionWithStatusCode e) {
            log.error("assignRole: {}, {}, {}, {}", user, pass, role, assignee);
            return new ResponseEntity<>(new Message(e.text), e.status);
        }
    }

    @PostMapping("sci/hw/resource/{user}/add_role")
    @ResponseBody
    public ResponseEntity<Message> addRole(@PathVariable String user,
                                           @RequestBody String pass,
                                           @RequestParam String resource,
                                           @RequestParam String role) {
        try {
            resourceAccessService.addRights(user, pass, resource, role);
            return ResponseEntity.ok().build();
        } catch (ExceptionWithStatusCode e) {
            log.error("addRole: {}, {}, {}, {}", user, pass, resource, role);
            return new ResponseEntity<>(new Message(e.text), e.status);
        }
    }
}
