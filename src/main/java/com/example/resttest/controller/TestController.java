package com.example.resttest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Finn on 08-09-2017.
 */
@Controller
public class TestController
{
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public ResponseEntity<String> start()
    {
        return new ResponseEntity("Success", HttpStatus.OK);
    }

    @RequestMapping(value = "/welcome/{name}", method = RequestMethod.GET)
    public ResponseEntity<String> welcome (@PathVariable("name") String name)
    {
        if (name.equalsIgnoreCase("charles"))
            return new ResponseEntity("Welcome " + name, HttpStatus.OK);
        else
            return new ResponseEntity("Welcome " + name, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
