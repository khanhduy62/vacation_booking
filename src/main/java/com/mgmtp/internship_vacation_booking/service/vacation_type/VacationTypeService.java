package com.mgmtp.internship_vacation_booking.service.vacation_type;

import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;

import java.util.List;

public interface VacationTypeService {

    VacationTypeEntity getVacationTypeEntity(int id);

    List<VacationTypeEntity> getAllVacationType();
}
