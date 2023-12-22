package com.hostfully.booking.api.utils;

import com.hostfully.booking.api.entity.Booking;
import com.hostfully.booking.api.entity.BookingStatus;
import com.hostfully.booking.api.entity.Guest;
import com.hostfully.booking.api.entity.Property;
import com.hostfully.booking.api.resource.BookingRequestResource;
import com.hostfully.booking.api.resource.BookingResponseResource;
import com.hostfully.booking.api.resource.GuestResource;
import com.hostfully.booking.api.resource.PropertyResource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ObjectMockUtils {

    /*****************************************************
     ******************  BOOKING MOCKS  ******************
     *****************************************************/

    public static BookingRequestResource getBookingRequestResourceMockHappyPath() {
        var resource = new BookingRequestResource();
        resource.setPropertyId("a50df57f-8554-4268-97c4-a0777f77317a");
        resource.setBeginAt(LocalDate.of(2023, 12, 10));
        resource.setEndAt(LocalDate.of(2023, 12, 20));
        resource.setGuests(getGuestResourcesMockHappyPath());
        return resource;
    }

    public static List<GuestResource> getGuestResourcesMockHappyPath() {
        var guest1 = new GuestResource();
        guest1.setName("Name Test");
        guest1.setEmail("email@teste.com");
        guest1.setAge(50);
        guest1.setDocumentNumber("PASSPORT");
        guest1.setDocumentNumber("XX999999999");

        var guest2 = new GuestResource();
        guest2.setName("Second Test");
        guest2.setEmail("email@teste.com");
        guest2.setAge(20);

        return Arrays.asList(guest1, guest2);
    }

    public static PropertyResource getBookingPropertyResourceMockHappyPath() {
        var resource = new PropertyResource();
        resource.setId(10L);
        resource.setUuid(UUID.fromString("a50df57f-8554-4268-97c4-a0777f77317a"));
        resource.setName("Property 10");
        resource.setDescription("This is a simple example of description of the first property");
        return resource;
    }

    public static Booking getSavedBookingMockHappyPath() {
        var booking = new Booking();
        booking.setId(1L);
        booking.setUuid(UUID.fromString("55a6b2ea-7d44-40d3-8eb1-8967110d3df8"));
        booking.setProperty(getBookingPropertyMockHappyPath());
        booking.setStatus(BookingStatus.BOOKED);
        booking.setBeginAt(LocalDate.of(2023, 12, 10));
        booking.setEndAt(LocalDate.of(2023, 12, 20));
        booking.setGuests(getGuestsMockHappyPath());
        return booking;
    }

    public static Booking getBookingMockHappyPath() {
        var booking = new Booking();
        booking.setProperty(getBookingPropertyMockHappyPath());
        booking.setBeginAt(LocalDate.of(2023, 12, 10));
        booking.setEndAt(LocalDate.of(2023, 12, 20));
        booking.setGuests(getGuestsMockHappyPath());
        return booking;
    }

    public static Property getBookingPropertyMockHappyPath() {
        var property = new Property();
        property.setId(10L);
        property.setUuid(UUID.fromString("a50df57f-8554-4268-97c4-a0777f77317a"));
        property.setName("Property 10");
        property.setDescription("This is a simple example of description of the first property");
        return property;
    }

    public static List<Guest> getGuestsMockHappyPath() {
        var guest1 = new Guest();
        guest1.setId(100L);
        guest1.setUuid(UUID.fromString("da26c68e-b6c6-47a7-a546-3f47621f0abc"));
        guest1.setName("Name Test");
        guest1.setEmail("email@teste.com");
        guest1.setAge(50);
        guest1.setDocumentNumber("PASSPORT");
        guest1.setDocumentNumber("XX999999999");

        var guest2 = new Guest();
        guest2.setId(200L);
        guest2.setUuid(UUID.fromString("834a61e8-57f4-46e4-a324-41598247921d"));
        guest2.setName("Second Test");
        guest2.setEmail("email@teste.com");
        guest2.setAge(20);

        return Arrays.asList(guest1, guest2);
    }

    public static BookingResponseResource getBookingResponseResourceMockHappyPath() {
        var resource = new BookingResponseResource();
        resource.setUuid("55a6b2ea-7d44-40d3-8eb1-8967110d3df8");
        resource.setProperty(getBookingPropertyResourceMockHappyPath());
        resource.setStatus(BookingStatus.BOOKED.name());
        resource.setBeginAt(LocalDate.of(2023, 12, 10));
        resource.setEndAt(LocalDate.of(2023, 12, 20));
        resource.setGuests(getGuestResourcesMockHappyPath());
        return resource;
    }


    /*****************************************************
     ******************  BOOKING MOCKS  ******************
     *****************************************************/

    public static PropertyResource getBlockPropertyResourceMockHappyPath() {
        var resource = new PropertyResource();
        resource.setId(999L);
        resource.setUuid(UUID.fromString("c0f926fa-5d63-4d33-8476-74ce938d6bff"));
        resource.setName("Block Property MOCK");
        resource.setDescription("This is a simple example of description of MOCK PROPERTY");
        return resource;
    }

    public static Property getBlockPropertyMockHappyPath() {
        var property = new Property();
        property.setId(999L);
        property.setUuid(UUID.fromString("c0f926fa-5d63-4d33-8476-74ce938d6bff"));
        property.setName("Block Property MOCK");
        property.setDescription("This is a simple example of description of MOCK PROPERTY");
        return property;
    }

    public static BookingRequestResource getBlockRequestResourceMockHappyPath() {
        var resource = new BookingRequestResource();
        resource.setPropertyId("c0f926fa-5d63-4d33-8476-74ce938d6bff");
        resource.setBeginAt(LocalDate.of(2024, 6, 20));
        resource.setEndAt(LocalDate.of(2024, 6, 30));
        return resource;
    }

    public static Booking getBlockMockHappyPath() {
        var booking = new Booking();
        booking.setProperty(getBlockPropertyMockHappyPath());
        booking.setBeginAt(LocalDate.of(2024, 6, 20));
        booking.setEndAt(LocalDate.of(2024, 6, 30));
        booking.setGuests(new ArrayList<>());
        return booking;
    }

    public static Booking getSavedBlockMockHappyPath() {
        var booking = new Booking();
        booking.setId(9L);
        booking.setUuid(UUID.fromString("77497a01-57e0-41ca-b880-02963f9cc6a0"));
        booking.setProperty(getBlockPropertyMockHappyPath());
        booking.setStatus(BookingStatus.BLOCKED);
        booking.setBeginAt(LocalDate.of(2024, 6, 20));
        booking.setEndAt(LocalDate.of(2024, 6, 30));
        booking.setGuests(new ArrayList<>());
        return booking;
    }

    public static BookingResponseResource getBlockResponseResourceMockHappyPath() {
        var resource = new BookingResponseResource();
        resource.setUuid("77497a01-57e0-41ca-b880-02963f9cc6a0");
        resource.setProperty(getBlockPropertyResourceMockHappyPath());
        resource.setStatus(BookingStatus.BLOCKED.name());
        resource.setBeginAt(LocalDate.of(2024, 6, 20));
        resource.setEndAt(LocalDate.of(2024, 6, 30));
        return resource;
    }
}
