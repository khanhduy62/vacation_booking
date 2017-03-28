--liquibase formatted sql

--changeset hcao:2
INSERT INTO time_off_request VALUES (1, '2016-05-01', '2016-05-05', 'Vacation', 'Summer holiday');
INSERT INTO time_off_request VALUES (2, '2016-05-07', '2016-05-15', 'Sick', 'Stomachache');