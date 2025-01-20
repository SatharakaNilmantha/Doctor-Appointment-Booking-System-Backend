package com.example.Doctor_Appointment_Booking_System_Backend.repository;

import com.example.Doctor_Appointment_Booking_System_Backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // Custom query to check if a doctor with the same details exists
    boolean existsByFullNameAndPhoneNumberAndDepartmentAndTitle(String fullName, String phoneNumber, String department, String title);

    @Query(value="SELECT* FROM doctor WHERE doctor_id=?1 " ,nativeQuery = true)
    Doctor getDoctorById(long doctorId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE doctor SET full_name = ?2, address = ?3, gender = ?4, image = ?5, phone_number = ?6, degree = ?7, department = ?8, title = ?9, description = ?10, fees = ?11 WHERE doctor_id = ?1", nativeQuery = true)
    int updateDoctorById(long doctorId, String fullName, String address, String gender, String image, String phoneNumber, String degree, String department, String title, String description, double fees);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM doctor WHERE doctor_id=?1", nativeQuery = true)
    int deleteDoctorById(long doctorId);


}
