package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.exception.PositionChangeException;

@Getter
public class Vector {
    private Integer x;
    private Integer y;

    public Vector(Integer x, Integer y) {
        assertIsNotNull(x, y);

        this.x = x;
        this.y = y;
    }

    public void plus(Vector vector) {
        checkBoundsLimit(vector.getX(), vector.getY());

        x = x + vector.getX();
        y = y + vector.getY();
    }

    private void assertIsNotNull(Integer x, Integer y) throws IllegalArgumentException {
        if ((x == null || y == null)) {
            throw new IllegalArgumentException("Координата не может быть null");
        }
    }

    private void checkBoundsLimit(Integer x, Integer y) {
        final Integer halfMax = Integer.MAX_VALUE / 2;
        final Integer halfMin = Integer.MIN_VALUE / 2;

        if ((x > halfMax && y > halfMax) || (x < halfMin && y < halfMin)) {

            throw new PositionChangeException();
        }
        if ((x > halfMax && y < halfMax) || (x < halfMin && y > halfMin)) {
            Integer distXFromMax = Integer.MAX_VALUE - x;
            Integer distXFromMin = Integer.MIN_VALUE - x;

            if (distXFromMax < y || distXFromMin > y) {
                throw new PositionChangeException();
            }
        }
    }
}
