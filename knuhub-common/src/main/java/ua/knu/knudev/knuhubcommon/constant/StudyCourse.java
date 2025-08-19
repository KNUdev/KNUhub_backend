package ua.knu.knudev.knuhubcommon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StudyCourse {

    FIRST_BACHELOR(0),
    SECOND_BACHELOR(1),
    THIRD_BACHELOR(2),
    FOURTH_BACHELOR(3),
    FIRST_MASTER(4),
    SECOND_MASTER(5),
    FIRST_PHD(6),
    SECOND_PHD(7),
    THIRD_PHD(8),
    FOURTH_PHD(9);

    @Getter
    private final int index;

}
