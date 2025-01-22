package com.example.Doctor_Appointment_Booking_System_Backend.service.serviceIMPL;

import com.example.Doctor_Appointment_Booking_System_Backend.Exception.DuplicateException;
import com.example.Doctor_Appointment_Booking_System_Backend.Exception.NotFoundException;
import com.example.Doctor_Appointment_Booking_System_Backend.dto.AdminDto;
import com.example.Doctor_Appointment_Booking_System_Backend.entity.Admin;
import com.example.Doctor_Appointment_Booking_System_Backend.repository.AdminRepository;
import com.example.Doctor_Appointment_Booking_System_Backend.service.AdminServices;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements AdminServices {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ModelMapper modelMapper;



    public String savedAdmin(AdminDto adminDto) {

        // Check for duplicate email
        if (adminRepository.existsByEmail(adminDto.getEmail())) {
            throw new DuplicateException("An Admin with this email already exists.");
        }

        // Save the patient if no duplicates found
        adminRepository.save(modelMapper.map(adminDto, Admin.class));
        return "Admin Details Saved Successfully";
    }


    public List<AdminDto> AllAdmin() {
        List adminList = adminRepository.findAll();
        if (adminList.isEmpty()) {
            throw new NotFoundException("No admins found in the database.");
        }
        return modelMapper.map(adminList, new TypeToken<List<AdminDto>>(){}.getType());
    }


    public String updateAdmin(long adminId, AdminDto adminDto) {

        if (adminDto.getFullName() == null || adminDto.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name is required.");
        }

        if (adminDto.getPhoneNumber() == null || adminDto.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required.");
        }


        // Check if the patient exists in the repository
        if (adminRepository.existsById(adminId)) {
            // Perform the update using the repository method
            int updatedRows = adminRepository.updateAdminById(
                    adminId,
                    adminDto.getFullName(),
                    adminDto.getPhoneNumber()

            );

            // Check if any rows were updated
            if (updatedRows > 0) {
                return "Admin updated successfully with ID " + adminId;
            } else {
                throw new RuntimeException("Failed to update Admin with ID " + adminId);
            }
        } else {
            // If the patient does not exist, throw an exception
            throw new RuntimeException("Admin not found with ID " + adminId);
        }
    }



    public String deleteAdminById(long adminId) {

        try {

            int deletedRows = adminRepository.deleteAdminById(adminId);

            if (deletedRows == 0) {
                // If no rows were deleted, throw custom exception
                throw new NotFoundException("Doctor with ID " + adminId + " not found or couldn't be deleted.");
            }

            return "Deleted successfully " + adminId;

        } catch (NotFoundException e) {
            throw e;
        }

    }

}
