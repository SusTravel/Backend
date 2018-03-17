package org.sustrav.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.sustrav.demo.services.AdminService;
import org.sustrav.demo.services.ImagesDomainService;

@RestController
public class AdminController {

    @Autowired
    private ImagesDomainService imagesDomainService;

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/api/place/{id}/qrcode", method = RequestMethod.POST)
    public Result generateQRCodeImage(@PathVariable("id") Long id) {
        return new Result(imagesDomainService.generateQRCodeForPlace(id));
    }

    @RequestMapping(value = "/api/user/{id}/remove", method = RequestMethod.POST)
    public Result removeUserInfo(@PathVariable("id") Long id) {
        adminService.removeUserInfo(id);
        return new Result("success");
    }

    public static class Result {
        private String result;

        public Result(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
