package com.example.project.dto;

import com.example.project.model.Funcionario;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FuncionarioDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String cargo;
    private BigDecimal salario;
    private LocalDate dataAdmissao;

    public FuncionarioDTO() {}

    public FuncionarioDTO(Funcionario funcionario) {
        this.id = funcionario.getId();
        this.nome = funcionario.getNome();
        this.cpf = funcionario.getCpf();
        this.cargo = funcionario.getCargo();
        this.salario = funcionario.getSalario();
        this.dataAdmissao = funcionario.getDataAdmissao();
    }

    // Getters and setters omitted for brevity
}
