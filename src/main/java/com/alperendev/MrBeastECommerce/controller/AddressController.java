package com.alperendev.MrBeastECommerce.controller;

import com.alperendev.MrBeastECommerce.dto.AddressDto;
import com.alperendev.MrBeastECommerce.dto.Response;
import com.alperendev.MrBeastECommerce.service.interfaces.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/save")
    public ResponseEntity<Response> saveAndUpdateAddress(@RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(addressService.saveAndupdateAddress(addressDto));
    }
}
