package com.rich.controller;

import com.rich.entity.CatTable;
import com.rich.entity.Config;
import com.rich.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @ClassName: DataBaseController
 * @Date: 2021/12/12 20:29
 * @Author: Clown B
 * @Version: 1.0
 */


@RestController
@CrossOrigin
@RequestMapping("/back")
public class DataBaseController {

    @Autowired
    private DataBaseService dataBaseService;


    @PostMapping("queryList")
    public List<CatTable> generateTableColShow(@RequestBody Config config) {
        return dataBaseService.generateTableColShow(config);
    }

    @PostMapping("generate")
    public void generateDbCode(@RequestBody Config config, HttpServletResponse response) {
        dataBaseService.generateDbCode(config, response);
    }

}
