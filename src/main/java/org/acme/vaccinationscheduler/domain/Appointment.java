package org.acme.vaccinationscheduler.domain;

import java.time.LocalDateTime;

public class Appointment {

	private Long appointmentId;
    private VaccineType vaccineType;
    private String vaccinationCenterName;
    private LocalDateTime timeslotDateTime;
    private Long personId;
    private String personName;
    private Boolean isFirstDoseAdministered;
    private AppointmentProviderStatus appointmentProviderStatus;
    private AppointmentRecipientStatus appointmentRecipientStatus;
    
	public AppointmentProviderStatus getAppointmentProviderStatus() {
		return appointmentProviderStatus;
	}
	public void setAppointmentProviderStatus(AppointmentProviderStatus appointmentProviderStatus) {
		this.appointmentProviderStatus = appointmentProviderStatus;
	}
	public AppointmentRecipientStatus getAppointmentRecipientStatus() {
		return appointmentRecipientStatus;
	}
	public void setAppointmentRecipientStatus(AppointmentRecipientStatus appointmentRecipientStatus) {
		this.appointmentRecipientStatus = appointmentRecipientStatus;
	}
	public VaccineType getVaccineType() {
		return vaccineType;
	}
	public void setVaccineType(VaccineType vaccineType) {
		this.vaccineType = vaccineType;
	}
	public Boolean getIsFirstDoseAdministered() {
		return isFirstDoseAdministered;
	}
	public void setIsFirstDoseAdministered(Boolean isFirstDoseAdministered) {
		this.isFirstDoseAdministered = isFirstDoseAdministered;
	}
	public Long getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}
	public String getVaccinationCenterName() {
		return vaccinationCenterName;
	}
	public void setVaccinationCenterName(String vaccinationCenterName) {
		this.vaccinationCenterName = vaccinationCenterName;
	}
	public LocalDateTime getTimeslotDateTime() {
		return timeslotDateTime;
	}
	public void setTimeslotDateTime(LocalDateTime timeslotDateTime) {
		this.timeslotDateTime = timeslotDateTime;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}

}
