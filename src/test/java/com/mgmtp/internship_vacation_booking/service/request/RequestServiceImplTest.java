package com.mgmtp.internship_vacation_booking.service.request;

import com.mgmtp.internship_vacation_booking.model.RequestEntity;
import com.mgmtp.internship_vacation_booking.model.VacationTypeEntity;
import com.mgmtp.internship_vacation_booking.repository.RequestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RequestServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getVacationTypeByRequestId() throws Exception {
        VacationTypeEntity vacationTypeEntity = new VacationTypeEntity();
        vacationTypeEntity.setName("Holiday");

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setComment("duy");
        requestEntity.setVacationType(vacationTypeEntity);

        Mockito.when(requestRepository.findOne(2)).thenReturn(requestEntity);
        assertEquals(requestService.getVacationTypeByRequestId(2), vacationTypeEntity);
    }
}