package com.example.project.service;

import com.example.project.dto.FuncionarioDTO;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.model.Funcionario;
import com.example.project.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public FuncionarioDTO createFuncionario(FuncionarioDTO funcionarioDTO) {
        Funcionario funcionario = new Funcionario(funcionarioDTO);
        return new FuncionarioDTO(funcionarioRepository.save(funcionario));
    }

    public List<FuncionarioDTO> getAllFuncionarios() {
        return funcionarioRepository.findAll().stream().map(FuncionarioDTO::new).collect(Collectors.toList());
    }

    public FuncionarioDTO getFuncionarioById(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Funcionario not found"));
        return new FuncionarioDTO(funcionario);
    }

    public FuncionarioDTO updateFuncionario(Long id, FuncionarioDTO funcionarioDTO) {
        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Funcionario not found"));
        funcionario.updateFromDTO(funcionarioDTO);
        return new FuncionarioDTO(funcionarioRepository.save(funcionario));
    }

    public void deleteFuncionario(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Funcionario not found"));
        funcionarioRepository.delete(funcionario);
    }
}
