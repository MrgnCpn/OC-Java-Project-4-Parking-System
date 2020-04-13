package com.parkit.parkingsystem.unit.model;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ParkingSpotTest {
    private ParkingSpot parkingSpot;

    @ParameterizedTest(name = "Id : {0}, ParkingType : {1}, isAvailable : {2}")
    @CsvSource({
            "1, CAR, false",
            "1, BIKE, false",
            "2, CAR, true",
            "2, BIKE, true"
    })
    void testGetterNoSetter(int id, String s_parkingType, Boolean isAvailable){
        parkingSpot = new ParkingSpot(id, ParkingType.valueOf(s_parkingType), isAvailable);

        assertThat(parkingSpot.getId()).isEqualTo(id);
        assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.valueOf(s_parkingType));
        assertThat(parkingSpot.isAvailable()).isEqualTo(isAvailable);

    }

    @Test
    void testGetterAfterSetter(){
        parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        parkingSpot.setId(2);
        parkingSpot.setParkingType(ParkingType.CAR);
        parkingSpot.setAvailable(true);

        assertThat(parkingSpot.getId()).isEqualTo(2);
        assertThat(parkingSpot.getParkingType()).isEqualTo(ParkingType.CAR);
        assertThat(parkingSpot.isAvailable()).isEqualTo(true);
    }

    @Test
    void testEquals() {
        assertThat(
                new ParkingSpot(1, ParkingType.BIKE, false)
                        .equals(new ParkingSpot(1, ParkingType.CAR, false))
        ).isTrue();
    }

    @AfterEach
    void undefTest() {
        parkingSpot = null;
    }
}