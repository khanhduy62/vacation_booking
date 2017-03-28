package com.mgmtp.internship_vacation_booking.service.vacationtype;

import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;

import java.util.List;

/**
 * Created by ASUS on 12/13/2016.
 */
public interface VacationTypeService {

    VacationTypeEntity getVacationTypeEntity(int id);

    List<VacationTypeEntity> getAllVacationType();
}
