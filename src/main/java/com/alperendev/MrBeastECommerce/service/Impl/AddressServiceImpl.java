package com.alperendev.MrBeastECommerce.service.Impl;

import com.alperendev.MrBeastECommerce.dto.AddressDto;
import com.alperendev.MrBeastECommerce.dto.Response;
import com.alperendev.MrBeastECommerce.entity.Address;
import com.alperendev.MrBeastECommerce.entity.User;
import com.alperendev.MrBeastECommerce.repository.AddressRepository;
import com.alperendev.MrBeastECommerce.service.interfaces.AddressService;
import com.alperendev.MrBeastECommerce.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    @Override
    public Response saveAndupdateAddress(AddressDto addressDto) {
        User user = userService.getLoginUser();
        Address address = user.getAddress();
        if(address == null){
            address = new Address();
            address.setUser(user);
        }
        if(addressDto.getStreet() != null) address.setStreet(addressDto.getStreet());
        if(addressDto.getCity() != null) address.setCity(addressDto.getCity());
        if(addressDto.getState() != null) address.setState(addressDto.getState());
        if(addressDto.getZipCode()!= null) address.setZipCode(addressDto.getZipCode());
        if(addressDto.getCountry()!= null) address.setCountry(addressDto.getCountry());

        addressRepository.save(address);

        String message = (user.getAddress() == null) ? "Address successfully created" : "Address successfully updated";
        return Response.builder()
                .status(200)
                .message(message)
                .build();
    }
}
