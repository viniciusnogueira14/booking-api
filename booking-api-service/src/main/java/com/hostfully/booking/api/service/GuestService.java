package com.hostfully.booking.api.service;

import com.hostfully.booking.api.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository repository;

    public void deleteGuestsById(List<Long> ids) {
        repository.deleteAllById(ids);
    }
}
