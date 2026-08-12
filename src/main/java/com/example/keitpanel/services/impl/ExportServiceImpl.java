package com.example.keitpanel.services.impl;

import com.example.keitpanel.entities.branch.Branch;
import com.example.keitpanel.entities.employee.Employee;
import com.example.keitpanel.entities.equipment.Equipment;
import com.example.keitpanel.entities.equipment.EquipmentAssignment;
import com.example.keitpanel.entities.position.Position;
import com.example.keitpanel.repositories.BranchRepository;
import com.example.keitpanel.repositories.EmployeeRepository;
import com.example.keitpanel.repositories.EquipmentAssignmentRepository;
import com.example.keitpanel.repositories.EquipmentRepository;
import com.example.keitpanel.repositories.PositionRepository;
import com.example.keitpanel.services.ExcelExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl {

    private final ExcelExportService excelExportService;
    private final EquipmentRepository equipmentRepository;
    private final BranchRepository branchRepository;
    private final EquipmentAssignmentRepository assignmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    @Transactional(readOnly = true)
    public byte[] exportEquipments() {
        List<String> headers = List.of("İnventar №", "Seriya №", "Brend", "Model", "Növ", "Status", "Filial", "Şöbə", "Otaq", "Qiymət");
        List<Equipment> list = equipmentRepository.findAll();

        List<List<Object>> rows = new ArrayList<>();
        for (Equipment eq : list) {
            rows.add(List.of(
                    eq.getInventoryNo() != null ? eq.getInventoryNo() : "",
                    eq.getSerialNo() != null ? eq.getSerialNo() : "",
                    eq.getBrand() != null ? eq.getBrand() : "",
                    eq.getModel() != null ? eq.getModel() : "",
                    eq.getType() != null ? eq.getType().name() : "",
                    eq.getStatus() != null ? eq.getStatus().name() : "",
                    eq.getBranch() != null ? eq.getBranch().getName() : "",
                    eq.getDepartment() != null ? eq.getDepartment().getName() : "",
                    eq.getRoom() != null ? eq.getRoom() : "",
                    eq.getPrice() != null ? eq.getPrice().toString() : ""
            ));
        }

        return excelExportService.generateExcel("Avadanlıqlar", headers, rows);
    }

    @Transactional(readOnly = true)
    public byte[] exportEmployees() {
        List<String> headers = List.of("Tabel №", "Ad", "Soyad", "Ata Adı", "E-poçt", "Telefon", "AnyDesk ID", "Filial", "Şöbə", "Vəzifə");
        List<Employee> list = employeeRepository.findAll();

        List<List<Object>> rows = new ArrayList<>();
        for (Employee emp : list) {
            rows.add(List.of(
                    emp.getPersonnelNo() != null ? emp.getPersonnelNo() : "",
                    emp.getFirstName() != null ? emp.getFirstName() : "",
                    emp.getLastName() != null ? emp.getLastName() : "",
                    emp.getPatronymic() != null ? emp.getPatronymic() : "",
                    emp.getEmail() != null ? emp.getEmail() : "",
                    emp.getPhoneMobile() != null ? emp.getPhoneMobile() : "",
                    emp.getAnydeskId() != null ? emp.getAnydeskId() : "",
                    emp.getBranch() != null ? emp.getBranch().getName() : "",
                    emp.getDepartment() != null ? emp.getDepartment().getName() : "",
                    emp.getPosition() != null ? emp.getPosition().getTitle() : ""
            ));
        }

        return excelExportService.generateExcel("İşçilər", headers, rows);
    }

    @Transactional(readOnly = true)
    public byte[] exportBranches() {
        List<String> headers = List.of("ID", "Filial Adı", "Üst Filial", "Status");
        List<Branch> list = branchRepository.findAll();

        List<List<Object>> rows = new ArrayList<>();
        for (Branch b : list) {
            rows.add(List.of(
                    b.getId(),
                    b.getName(),
                    b.getParent() != null ? b.getParent().getName() : "-",
                    b.isActive() ? "Aktiv" : "Deaktiv"
            ));
        }

        return excelExportService.generateExcel("Filiallar", headers, rows);
    }

    @Transactional(readOnly = true)
    public byte[] exportPositions() {
        List<String> headers = List.of("ID", "Vəzifə Adı", "Şöbə", "Ştat Sayı");
        List<Position> list = positionRepository.findAll();

        List<List<Object>> rows = new ArrayList<>();
        for (Position p : list) {
            rows.add(List.of(
                    p.getId(),
                    p.getTitle() != null ? p.getTitle() : "",
                    p.getDepartment() != null ? p.getDepartment().getName() : "",
                    p.getHeadcount() != null ? p.getHeadcount() : 0
            ));
        }

        return excelExportService.generateExcel("Vəzifələr", headers, rows);
    }

    @Transactional(readOnly = true)
    public byte[] exportAssignments() {
        List<String> headers = List.of("Avadanlıq (İnventar №)", "Təhvil Alan İşçi", "AnyDesk ID", "Filial", "Təhvil Tarixi", "Qaytarılma Tarixi", "Akt №");
        List<EquipmentAssignment> list = assignmentRepository.findAll();

        List<List<Object>> rows = new ArrayList<>();
        for (EquipmentAssignment a : list) {
            String employeeName = a.getEmployee() != null ? a.getEmployee().getFirstName() + " " + a.getEmployee().getLastName() : "-";
            String anydesk = (a.getEmployee() != null && a.getEmployee().getAnydeskId() != null) ? a.getEmployee().getAnydeskId() : "-";

            rows.add(List.of(
                    a.getEquipment() != null ? a.getEquipment().getInventoryNo() : "-",
                    employeeName,
                    anydesk,
                    a.getBranch() != null ? a.getBranch().getName() : "-",
                    a.getAssignedAt() != null ? a.getAssignedAt().toString() : "-",
                    a.getReturnedAt() != null ? a.getReturnedAt().toString() : "Aktivdir",
                    a.getHandoverDocNo() != null ? a.getHandoverDocNo() : "-"
            ));
        }

        return excelExportService.generateExcel("Təhvil-Təslim", headers, rows);
    }
}