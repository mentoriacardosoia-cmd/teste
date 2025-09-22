package com.example.project.model;

import com.example.project.dto.FuncionarioDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    private String cargo;

    @Column(nullable = false)
    private BigDecimal salario;

    private LocalDate dataAdmissao;

    public Funcionario() {}

    public Funcionario(FuncionarioDTO dto) {
        this.nome = dto.getNome();
        this.cpf = dto.getCpf();
        this.cargo = dto.getCargo();
        this.salario = dto.getSalario();
        this.dataAdmissao = dto.getDataAdmissao();
    }

    public void updateFromDTO(FuncionarioDTO dto) {
        this.nome = dto.getNome();
        this.cargo = dto.getCargo();
        this.salario = dto.getSalario();
        this.dataAdmissao = dto.getDataAdmissao();
    }

    // Getters and setters omitted for brevity
}
