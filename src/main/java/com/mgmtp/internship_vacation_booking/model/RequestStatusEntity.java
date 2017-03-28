package com.mgmtp.internship_vacation_booking.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "request_status")
public class RequestStatusEntity {

    @EmbeddedId
    private RequestStatusEntityPK requestStatusEntityPK;

    @Column(name = "approved")
    private Boolean approved;

}
