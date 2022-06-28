package org.example.domain;

import lombok.Getter;

@Getter
public class Direction {
    private final Integer directionNumber;
    private Integer direction;

    public Direction(Integer direction, Integer directionNumber) {
        assertIsNotNull(direction, directionNumber);

        this.direction = direction % directionNumber;
        this.directionNumber = directionNumber;
    }

    public void next(Integer velocity) {
         direction = (direction + velocity) % directionNumber;
    }

    private void assertIsNotNull(Integer direction, Integer directionNumber) throws IllegalArgumentException {
        if ((direction == null || directionNumber == null)) {
            throw new IllegalArgumentException("Напраление не может быть null");
        }
    }

}
