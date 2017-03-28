package com.mgmtp.internship_vacation_booking.service.vacation_type;

import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;
import com.mgmtp.internship_vacation_booking.repository.VacationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacationTypeServiceImpl implements VacationTypeService {

    private final VacationTypeRepository vacationTypeRepository;

    @Autowired
    public VacationTypeServiceImpl(VacationTypeRepository vacationTypeRepository) {
        this.vacationTypeRepository = vacationTypeRepository;
    }

    @Override
    public VacationTypeEntity getVacationTypeEntity(int id) {
        return vacationTypeRepository.findOne(id);
    }

    @Override
    public List<VacationTypeEntity> getAllVacationType() {
        return vacationTypeRepository.findAllByOrderByDisplayOrderAsc();
    }
}
