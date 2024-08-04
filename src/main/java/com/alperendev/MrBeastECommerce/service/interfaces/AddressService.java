package com.alperendev.MrBeastECommerce.service.interfaces;

import com.alperendev.MrBeastECommerce.dto.AddressDto;
import com.alperendev.MrBeastECommerce.dto.Response;

public interface AddressService {
    Response saveAndupdateAddress(AddressDto addressDto);
}
