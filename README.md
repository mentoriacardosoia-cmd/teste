CRIE ESSE PROJETO CRIANDO CADA ARQUIVO DO JAVA E DO FRONT 

/backend/pom.xml
xml<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.0.0</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.desafio</groupId>
	<artifactId>backend</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>backend</name>
	<description>Desafio Fullstack Backend</description>
	<properties>
		<java.version>17</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
/backend/src/main/java/com/desafio/backend/BackendApplication.java
javapackage com.desafio.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:4200")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}
/backend/src/main/java/com/desafio/backend/entity/Secretaria.java
javapackage com.desafio.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Secretaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Sigla é obrigatória")
    private String sigla;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
/backend/src/main/java/com/desafio/backend/entity/Servidor.java
javapackage com.desafio.backend.entity;

import com.desafio.backend.validator.Age;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

@Entity
public class Servidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Email(message = "E-mail deve ser válido")
    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    @Past(message = "Data de nascimento deve ser no passado")
    @NotNull(message = "Data de nascimento é obrigatória")
    @Age(min = 18, max = 75, message = "Idade deve ser entre 18 e 75 anos")
    private LocalDate dataNascimento;

    @ManyToOne
    @JoinColumn(name = "secretaria_id")
    @NotNull(message = "Secretaria é obrigatória")
    private Secretaria secretaria;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Secretaria getSecretaria() {
        return secretaria;
    }

    public void setSecretaria(Secretaria secretaria) {
        this.secretaria = secretaria;
    }
}
/backend/src/main/java/com/desafio/backend/validator/Age.java
javapackage com.desafio.backend.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Age {
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    String message() default "Idade inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
/backend/src/main/java/com/desafio/backend/validator/AgeValidator.java
javapackage com.desafio.backend.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {

    private int min;
    private int max;

    @Override
    public void initialize(Age constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true;
        }
        int age = Period.between(date, LocalDate.now()).getYears();
        return age >= min && age <= max;
    }
}
/backend/src/main/java/com/desafio/backend/repository/SecretariaRepository.java
javapackage com.desafio.backend.repository;

import com.desafio.backend.entity.Secretaria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecretariaRepository extends JpaRepository<Secretaria, Long> {
}
/backend/src/main/java/com/desafio/backend/repository/ServidorRepository.java
javapackage com.desafio.backend.repository;

import com.desafio.backend.entity.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServidorRepository extends JpaRepository<Servidor, Long> {
}
/backend/src/main/java/com/desafio/backend/service/SecretariaService.java
javapackage com.desafio.backend.service;

import com.desafio.backend.entity.Secretaria;
import com.desafio.backend.repository.SecretariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecretariaService {

    @Autowired
    private SecretariaRepository repository;

    public List<Secretaria> findAll() {
        return repository.findAll();
    }

    public Secretaria save(Secretaria secretaria) {
        return repository.save(secretaria);
    }

    public Optional<Secretaria> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
/backend/src/main/java/com/desafio/backend/service/ServidorService.java
javapackage com.desafio.backend.service;

import com.desafio.backend.entity.Servidor;
import com.desafio.backend.repository.ServidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServidorService {

    @Autowired
    private ServidorRepository repository;

    public List<Servidor> findAll() {
        return repository.findAll();
    }

    public Servidor save(Servidor servidor) {
        return repository.save(servidor);
    }

    public Optional<Servidor> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
/backend/src/main/java/com/desafio/backend/controller/SecretariaController.java
javapackage com.desafio.backend.controller;

import com.desafio.backend.entity.Secretaria;
import com.desafio.backend.service.SecretariaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secretarias")
public class SecretariaController {

    @Autowired
    private SecretariaService service;

    @GetMapping
    public List<Secretaria> list() {
        return service.findAll();
    }

    @PostMapping
    public Secretaria create(@Valid @RequestBody Secretaria secretaria) {
        return service.save(secretaria);
    }

    @PutMapping
    public ResponseEntity<Secretaria> update(@Valid @RequestBody Secretaria secretaria) {
        if (secretaria.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return service.findById(secretaria.getId())
                .map(existing -> {
                    existing.setNome(secretaria.getNome());
                    existing.setSigla(secretaria.getSigla());
                    return ResponseEntity.ok(service.save(existing));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
/backend/src/main/java/com/desafio/backend/controller/ServidorController.java
javapackage com.desafio.backend.controller;

import com.desafio.backend.entity.Servidor;
import com.desafio.backend.service.ServidorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servidores")
public class ServidorController {

    @Autowired
    private ServidorService service;

    @GetMapping
    public List<Servidor> list() {
        return service.findAll();
    }

    @PostMapping
    public Servidor create(@Valid @RequestBody Servidor servidor) {
        return service.save(servidor);
    }

    @PutMapping
    public ResponseEntity<Servidor> update(@Valid @RequestBody Servidor servidor) {
        if (servidor.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return service.findById(servidor.getId())
                .map(existing -> {
                    existing.setNome(servidor.getNome());
                    existing.setEmail(servidor.getEmail());
                    existing.setDataNascimento(servidor.getDataNascimento());
                    existing.setSecretaria(servidor.getSecretaria());
                    return ResponseEntity.ok(service.save(existing));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
/backend/src/main/java/com/desafio/backend/exception/GlobalExceptionHandler.java
javapackage com.desafio.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralExceptions(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
/backend/src/main/resources/application.properties
propertiesspring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
/frontend/package.json
json{
  "name": "frontend",
  "version": "0.0.0",
  "scripts": {
    "ng": "ng",
    "start": "ng serve",
    "build": "ng build",
    "watch": "ng build --watch --configuration development",
    "test": "ng test"
  },
  "private": true,
  "dependencies": {
    "@angular/animations": "^17.0.0",
    "@angular/common": "^17.0.0",
    "@angular/compiler": "^17.0.0",
    "@angular/core": "^17.0.0",
    "@angular/forms": "^17.0.0",
    "@angular/platform-browser": "^17.0.0",
    "@angular/platform-browser-dynamic": "^17.0.0",
    "@angular/router": "^17.0.0",
    "primeicons": "^7.0.0",
    "primeng": "^17.0.0",
    "rxjs": "~7.8.0",
    "tslib": "^2.3.0",
    "zone.js": "~0.14.3",
    "xlsx": "^0.18.5",
    "file-saver": "^2.0.5"
  },
  "devDependencies": {
    "@angular-devkit/build-angular": "^17.0.0",
    "@angular/cli": "^17.0.0",
    "@angular/compiler-cli": "^17.0.0",
    "@types/jasmine": "~5.1.0",
    "@types/jasminewd2": "~2.0.3",
    "jasmine-core": "~5.1.0",
    "karma": "~6.4.0",
    "karma-chrome-launcher": "~3.2.0",
    "karma-coverage": "~2.2.0",
    "karma-jasmine": "~5.1.0",
    "karma-jasmine-html-reporter": "~2.1.0",
    "typescript": "~5.2.2"
  }
}
/frontend/angular.json
json{
  "$schema": "./node_modules/@angular/cli/lib/config/schema.json",
  "version": 1,
  "newProjectRoot": "projects",
  "projects": {
    "frontend": {
      "projectType": "application",
      "schematics": {
        "@schematics/angular:component": {
          "style": "scss"
        }
      },
      "root": "",
      "sourceRoot": "src",
      "prefix": "app",
      "architect": {
        "build": {
          "builder": "@angular-devkit/build-angular:browser",
          "options": {
            "outputPath": "dist/frontend",
            "index": "src/index.html",
            "main": "src/main.ts",
            "polyfills": "src/polyfills.ts",
            "tsConfig": "tsconfig.app.json",
            "assets": [
              "src/favicon.ico",
              "src/assets"
            ],
            "styles": [
              "src/styles.scss",
              "node_modules/primeng/resources/themes/saga-blue/theme.css",
              "node_modules/primeng/resources/primeng.min.css",
              "node_modules/primeicons/primeicons.css"
            ],
            "scripts": [],
            "vendorChunk": true,
            "extractLicenses": false,
            "buildOptimizer": false,
            "sourceMap": true,
            "optimization": false,
            "namedChunks": true
          },
          "configurations": {
            "production": {
              "fileReplacements": [
                {
                  "replace": "src/environments/environment.ts",
                  "with": "src/environments/environment.prod.ts"
                }
              ],
              "optimization": true,
              "outputHashing": "all",
              "sourceMap": false,
              "namedChunks": false,
              "extractLicenses": true,
              "vendorChunk": false,
              "buildOptimizer": true
            }
          }
        },
        "serve": {
          "builder": "@angular-devkit/build-angular:dev-server",
          "configurations": {
            "production": {
              "browserTarget": "frontend:build:production"
            },
            "development": {
              "browserTarget": "frontend:build:development"
            }
          },
          "defaultConfiguration": "development"
        },
        "extract-i18n": {
          "builder": "@angular-devkit/build-angular:extract-i18n",
          "options": {
            "browserTarget": "frontend:build"
          }
        },
        "test": {
          "builder": "@angular-devkit/build-angular:karma",
          "options": {
            "main": "src/test.ts",
            "polyfills": "src/polyfills.ts",
            "tsConfig": "tsconfig.spec.json",
            "karmaConfig": "karma.conf.js",
            "assets": [
              "src/favicon.ico",
              "src/assets"
            ],
            "styles": [
              "src/styles.scss"
            ],
            "scripts": []
          }
        }
      }
    }
  },
  "cli": {
    "analytics": false
  }
}
/frontend/src/app/app.module.ts
typescriptimport { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ServidorListComponent } from './servidor-list/servidor-list.component';
import { ServidorFormComponent } from './servidor-form/servidor-form.component';
import { SecretariaListComponent } from './secretaria-list/secretaria-list.component';
import { SecretariaFormComponent } from './secretaria-form/secretaria-form.component';

import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { MessageModule } from 'primeng/message';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TabViewModule } from 'primeng/tabview';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@NgModule({
  declarations: [
    AppComponent,
    ServidorListComponent,
    ServidorFormComponent,
    SecretariaListComponent,
    SecretariaFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    CalendarModule,
    DropdownModule,
    MessageModule,
    ProgressSpinnerModule,
    TabViewModule,
    DialogModule,
    ToastModule,
    CardModule,
    ConfirmDialogModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
/frontend/src/app/app.component.html
html<div class="p-4">
  <h1>Gestão de Servidores e Secretarias</h1>
  <p-tabView>
    <p-tabPanel header="Servidores">
      <app-servidor-list></app-servidor-list>
    </p-tabPanel>
    <p-tabPanel header="Secretarias">
      <app-secretaria-list></app-secretaria-list>
    </p-tabPanel>
  </p-tabView>
</div>
<p-toast></p-toast>
/frontend/src/app/app.component.ts
typescriptimport { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'frontend';
}
/frontend/src/app/servidor-list/servidor-list.component.html
html<div class="card">
  <p-progressSpinner *ngIf="loading"></p-progressSpinner>
  <p-table [value]="servidores" [tableStyle]="{ 'min-width': '50rem' }" [exportFilename]="'servidores'">
    <ng-template pTemplate="caption">
      <div class="flex justify-content-between">
        <span>Listagem de Servidores</span>
        <button pButton icon="pi pi-plus" label="Novo" (click)="showForm = true; selectedServidor = null"></button>
      </div>
    </ng-template>
    <ng-template pTemplate="header">
      <tr>
        <th>Nome</th>
        <th>E-mail</th>
        <th>Data Nascimento</th>
        <th>Secretaria</th>
        <th>Sigla</th>
        <th>Ações</th>
      </tr>
    </ng-template>
    <ng-template pTemplate="body" let-servidor>
      <tr>
        <td>{{ servidor.nome }}</td>
        <td>{{ servidor.email }}</td>
        <td>{{ servidor.dataNascimento | date: 'dd/MM/yyyy' }}</td>
        <td>{{ servidor.secretaria?.nome }}</td>
        <td>{{ servidor.secretaria?.sigla }}</td>
        <td>
          <button pButton icon="pi pi-pencil" class="p-button-rounded p-button-success mr-2" (click)="edit(servidor)"></button>
          <button pButton icon="pi pi-trash" class="p-button-rounded p-button-danger" (click)="confirmDelete(servidor.id)"></button>
        </td>
      </tr>
    </ng-template>
    <ng-template pTemplate="summary">
      <div class="flex align-items-center justify-content-between">
        Total de servidores: {{servidores ? servidores.length : 0 }}
        <p-button icon="pi pi-file-excel" label="Exportar XLS" (click)="exportExcel()"></p-button>
        <p-button icon="pi pi-file" label="Exportar CSV" (click)="exportCsv()"></p-button>
      </div>
    </ng-template>
  </p-table>
</div>

<p-dialog header="{{ selectedServidor ? 'Editar Servidor' : 'Cadastrar Servidor' }}" [(visible)]="showForm" [modal]="true" [style]="{width: '50vw'}">
  <app-servidor-form [servidor]="selectedServidor" (onSave)="loadServidores()" (onCancel)="showForm = false"></app-servidor-form>
</p-dialog>

<p-confirmDialog [style]="{width: '50vw'}"></p-confirmDialog>
/frontend/src/app/servidor-list/servidor-list.component.ts
typescriptimport { Component, OnInit } from '@angular/core';
import { Servidor } from '../models/servidor';
import { ServidorService } from '../services/servidor.service';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-servidor-list',
  templateUrl: './servidor-list.component.html',
  styleUrls: ['./servidor-list.component.scss'],
  providers: [MessageService, ConfirmationService]
})
export class ServidorListComponent implements OnInit {
  servidores: Servidor[] = [];
  showForm = false;
  selectedServidor: Servidor | null = null;
  loading = false;

  constructor(
    private service: ServidorService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.loadServidores();
  }

  loadServidores() {
    this.loading = true;
    this.service.getAll().subscribe({
      next: (data) => {
        this.servidores = data;
        this.loading = false;
      },
      error: (err) => {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Erro ao carregar servidores: ' + err.message});
        this.loading = false;
      }
    });
  }

  edit(servidor: Servidor) {
    this.selectedServidor = { ...servidor };
    this.showForm = true;
  }

  confirmDelete(id: number) {
    this.confirmationService.confirm({
      message: 'Confirma a exclusão?',
      accept: () => {
        this.delete(id);
      }
    });
  }

  delete(id: number) {
    this.service.delete(id).subscribe({
      next: () => {
        this.messageService.add({severity: 'success', summary: 'Sucesso', detail: 'Servidor excluído com sucesso'});
        this.loadServidores();
      },
      error: (err) => this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Erro ao excluir: ' + err.message})
    });
  }

  exportCsv() {
    const table = document.querySelector('p-table') as any;
    table.exportCSV();
  }

  exportExcel() {
    import('xlsx').then(xlsx => {
      const worksheet = xlsx.utils.json_to_sheet(this.servidores.map(s => ({
        Nome: s.nome,
        Email: s.email,
        'Data Nascimento': s.dataNascimento,
        Secretaria: s.secretaria?.nome,
        Sigla: s.secretaria?.sigla
      })));
      const workbook = { Sheets: { 'data': worksheet }, SheetNames: ['data'] };
      const excelBuffer: any = xlsx.write(workbook, { bookType: 'xlsx', type: 'array' });
      this.saveAsExcelFile(excelBuffer, 'servidores');
    });
  }

  private saveAsExcelFile(buffer: any, fileName: string): void {
    import('file-saver').then(FileSaver => {
      let EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
      let EXCEL_EXTENSION = '.xlsx';
      const data: Blob = new Blob([buffer], {
        type: EXCEL_TYPE
      });
      FileSaver.saveAs(data, fileName + '_export_' + new Date().getTime() + EXCEL_EXTENSION);
    });
  }
}
/frontend/src/app/servidor-form/servidor-form.component.html
html<form [formGroup]="form" (ngSubmit)="save()">
  <div class="p-fluid">
    <div class="field">
      <label for="nome">Nome</label>
      <input pInputText id="nome" formControlName="nome" />
      <small class="p-error" *ngIf="form.get('nome')?.invalid && form.get('nome')?.touched">Nome obrigatório</small>
    </div>
    <div class="field">
      <label for="email">E-mail</label>
      <input pInputText id="email" formControlName="email" />
      <small class="p-error" *ngIf="form.get('email')?.invalid && form.get('email')?.touched">E-mail inválido</small>
    </div>
    <div class="field">
      <label for="dataNascimento">Data Nascimento</label>
      <p-calendar id="dataNascimento" formControlName="dataNascimento" dateFormat="dd/mm/yy" [showIcon]="true"></p-calendar>
      <small class="p-error" *ngIf="form.get('dataNascimento')?.invalid && form.get('dataNascimento')?.touched">Data inválida (18-75 anos)</small>
    </div>
    <div class="field">
      <label for="secretaria">Secretaria</label>
      <p-dropdown id="secretaria" formControlName="secretaria" [options]="secretarias" optionLabel="nome" [showClear]="true"></p-dropdown>
      <small class="p-error" *ngIf="form.get('secretaria')?.invalid && form.get('secretaria')?.touched">Secretaria obrigatória</small>
    </div>
  </div>
  <div class="flex justify-content-end mt-3">
    <button pButton type="button" label="Cancelar" class="p-button-secondary mr-2" (click)="cancel()"></button>
    <button pButton type="submit" label="Salvar" [disabled]="form.invalid"></button>
  </div>
</form>
/frontend/src/app/servidor-form/servidor-form.component.ts
typescriptimport { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Servidor } from '../models/servidor';
import { Secretaria } from '../models/secretaria';
import { ServidorService } from '../services/servidor.service';
import { SecretariaService } from '../services/secretaria.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-servidor-form',
  templateUrl: './servidor-form.component.html',
  styleUrls: ['./servidor-form.component.scss']
})
export class ServidorFormComponent implements OnInit {
  @Input() servidor: Servidor | null = null;
  @Output() onSave = new EventEmitter();
  @Output() onCancel = new EventEmitter();

  form!: FormGroup;
  secretarias: Secretaria[] = [];

  constructor(
    private fb: FormBuilder,
    private servidorService: ServidorService,
    private secretariaService: SecretariaService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      id: [null],
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      dataNascimento: ['', Validators.required],
      secretaria: [null, Validators.required]
    });

    this.secretariaService.getAll().subscribe({
      next: (data) => this.secretarias = data,
      error: (err) => this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Erro ao carregar secretarias'})
    });

    if (this.servidor) {
      this.form.patchValue({
        ...this.servidor,
        dataNascimento: new Date(this.servidor.dataNascimento)
      });
    }
  }

  save() {
    if (this.form.valid) {
      const data = this.form.value;
      data.dataNascimento = data.dataNascimento.toISOString().split('T')[0]; // Convert to YYYY-MM-DD for backend
      if (this.servidor) {
        this.servidorService.update(data).subscribe({
          next: () => {
            this.messageService.add({severity: 'success', summary: 'Sucesso', detail: 'Servidor atualizado com sucesso'});
            this.onSave.emit();
          },
          error: (err) => {
            const errorMsg = err.error ? Object.values(err.error).join(', ') : err.message;
            this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Erro ao atualizar: ' + errorMsg});
          }
        });
      } else {
        this.servidorService.create(data).subscribe({
          next: () => {
            this.messageService.add({severity: 'success', summary: 'Sucesso', detail: 'Servidor cadastrado com sucesso'});
            this.onSave.emit();
          },
          error: (err) => {
            const errorMsg = err.error ? Object.values(err.error).join(', ') : err.message;
            this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Erro ao cadastrar: ' + errorMsg});
          }
        });
      }
    }
  }

  cancel() {
    this.onCancel.emit();
  }
}
/frontend/src/app/secretaria-list/secretaria-list.component.html
html<div class="card">
  <p-progressSpinner *ngIf="loading"></p-progressSpinner>
  <p-table [value]="secretarias" [tableStyle]="{ 'min-width': '50rem' }" [exportFilename]="'secretarias'">
    <ng-template pTemplate="caption">
      <div class="flex justify-content-between">
        <span>Listagem de Secretarias</span>
        <button pButton icon="pi pi-plus" label="Novo" (click)="showForm = true; selectedSecretaria = null"></button>
      </div>
    </ng-template>
    <ng-template pTemplate="header">
      <tr>
        <th>Nome</th>
        <th>Sigla</th>
        <th>Ações</th>
      </tr>
    </ng-template>
    <ng-template pTemplate="body" let-secretaria>
      <tr>
        <td>{{ secretaria.nome }}</td>
        <td>{{ secretaria.sigla }}</td>
        <td>
          <button pButton icon="pi pi-pencil" class="p-button-rounded p-button-success mr-2" (click)="edit(secretaria)"></button>
          <button pButton icon="pi pi-trash" class="p-button-rounded p-button-danger" (click)="confirmDelete(secretaria.id)"></button>
        </td>
      </tr>
    </ng-template>
    <ng-template pTemplate="summary">
      <div class="flex align-items-center justify-content-between">
        Total de secretarias: {{secretarias ? secretarias.length : 0 }}
        <p-button icon="pi pi-file-excel" label="Exportar XLS" (click)="exportExcel()"></p-button>
        <p-button icon="pi pi-file" label="Exportar CSV" (click)="exportCsv()"></p-button>
      </div>
    </ng-template>
  </p-table>
</div>

<p-dialog header="{{ selectedSecretaria ? 'Editar Secretaria' : 'Cadastrar Secretaria' }}" [(visible)]="showForm" [modal]="true" [style]="{width: '50vw'}">
  <app-secretaria-form [secretaria]="selectedSecretaria" (onSave)="loadSecretarias()" (onCancel)="showForm = false"></app-secretaria-form>
</p-dialog>

<p-confirmDialog [style]="{width: '50vw'}"></p-confirmDialog>
/frontend/src/app/secretaria-list/secretaria-list.component.ts
typescriptimport { Component, OnInit } from '@angular/core';
import { Secretaria } from '../models/secretaria';
import { SecretariaService } from '../services/secretaria.service';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-secretaria-list',
  templateUrl: './secretaria-list.component.html',
  styleUrls: ['./secretaria-list.component.scss'],
  providers: [MessageService, ConfirmationService]
})
export class SecretariaListComponent implements OnInit {
  secretarias: Secretaria[] = [];
  showForm = false;
  selectedSecretaria: Secretaria | null = null;
  loading = false;

  constructor(
    private service: SecretariaService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.loadSecretarias();
  }

  loadSecretarias() {
    this.loading = true;
    this.service.getAll().subscribe({
      next: (data) => {
        this.secretarias = data;
        this.loading = false;
      },
      error: (err) => {
        this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Erro ao carregar secretarias: ' + err.message});
        this.loading = false;
      }
    });
  }

  edit(secretaria: Secretaria) {
    this.selectedSecretaria = { ...secretaria };
    this.showForm = true;
  }

  confirmDelete(id: number) {
    this.confirmationService.confirm({
      message: 'Confirma a exclusão?',
      accept: () => {
        this.delete(id);
      }
    });
  }

  delete(id: number) {
    this.service.delete(id).subscribe({
      next: () => {
        this.messageService.add({severity: 'success', summary: 'Sucesso', detail: 'Secretaria excluída com sucesso'});
        this.loadSecretarias();
      },
      error: (err) => this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Erro ao excluir: ' + err.message})
    });
  }

  exportCsv() {
    const table = document.querySelector('p-table') as any;
    table.exportCSV();
  }

  exportExcel() {
    import('xlsx').then(xlsx => {
      const worksheet = xlsx.utils.json_to_sheet(this.secretarias);
      const workbook = { Sheets: { 'data': worksheet }, SheetNames: ['data'] };
      const excelBuffer: any = xlsx.write(workbook, { bookType: 'xlsx', type: 'array' });
      this.saveAsExcelFile(excelBuffer, 'secretarias');
    });
  }

  private saveAsExcelFile(buffer: any, fileName: string): void {
    import('file-saver').then(FileSaver => {
      let EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
      let EXCEL_EXTENSION = '.xlsx';
      const data: Blob = new Blob([buffer], {
        type: EXCEL_TYPE
      });
      FileSaver.saveAs(data, fileName + '_export_' + new Date().getTime() + EXCEL_EXTENSION);
    });
  }
}
/frontend/src/app/secretaria-form/secretaria-form.component.html
html<form [formGroup]="form" (ngSubmit)="save()">
  <div class="p-fluid">
    <div class="field">
      <label for="nome">Nome</label>
      <input pInputText id="nome" formControlName="nome" />
      <small class="p-error" *ngIf="form.get('nome')?.invalid && form.get('nome')?.touched">Nome obrigatório</small>
    </div>
    <div class="field">
      <label for="sigla">Sigla</label>
      <input pInputText id="sigla" formControlName="sigla" />
      <small class="p-error" *ngIf="form.get('sigla')?.invalid && form.get('sigla')?.touched">Sigla obrigatória</small>
    </div>
  </div>
  <div class="flex justify-content-end mt-3">
    <button pButton type="button" label="Cancelar" class="p-button-secondary mr-2" (click)="cancel()"></button>
    <button pButton type="submit" label="Salvar" [disabled]="form.invalid"></button>
  </div>
</form>
/frontend/src/app/secretaria-form/secretaria-form.component.ts
typescriptimport { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Secretaria } from '../models/secretaria';
import { SecretariaService } from '../services/secretaria.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-secretaria-form',
  templateUrl: './secretaria-form.component.html',
  styleUrls: ['./secretaria-form.component.scss']
})
export class SecretariaFormComponent implements OnInit {
  @Input() secretaria: Secretaria | null = null;
  @Output() onSave = new EventEmitter();
  @Output() onCancel = new EventEmitter();

  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private service: SecretariaService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      id: [null],
      nome: ['', Validators.required],
      sigla: ['', Validators.required]
    });

    if (this.secretaria) {
      this.form.patchValue(this.secretaria);
    }
  }

  save() {
    if (this.form.valid) {
      const data = this.form.value;
      if (this.secretaria) {
        this.service.update(data).subscribe({
          next: () => {
            this.messageService.add({severity: 'success', summary: 'Sucesso', detail: 'Secretaria atualizada com sucesso'});
            this.onSave.emit();
          },
          error: (err) => {
            const errorMsg = err.error ? Object.values(err.error).join(', ') : err.message;
            this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Erro ao atualizar: ' + errorMsg});
          }
        });
      } else {
        this.service.create(data).subscribe({
          next: () => {
            this.messageService.add({severity: 'success', summary: 'Sucesso', detail: 'Secretaria cadastrada com sucesso'});
            this.onSave.emit();
          },
          error: (err) => {
            const errorMsg = err.error ? Object.values(err.error).join(', ') : err.message;
            this.messageService.add({severity: 'error', summary: 'Erro', detail: 'Erro ao cadastrar: ' + errorMsg});
          }
        });
      }
    }
  }

  cancel() {
    this.onCancel.emit();
  }
}
/frontend/src/app/models/servidor.ts
typescriptimport { Secretaria } from './secretaria';

export interface Servidor {
  id?: number;
  nome: string;
  email: string;
  dataNascimento: string | Date;
  secretaria: Secretaria;
}
/frontend/src/app/models/secretaria.ts
typescriptexport interface Secretaria {
  id?: number;
  nome: string;
  sigla: string;
}
/frontend/src/app/services/servidor.service.ts
typescriptimport { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Servidor } from '../models/servidor';

@Injectable({
  providedIn: 'root'
})
export class ServidorService {
  private apiUrl = 'http://localhost:8080/servidores';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Servidor[]> {
    return this.http.get<Servidor[]>(this.apiUrl);
  }

  create(servidor: Servidor): Observable<Servidor> {
    return this.http.post<Servidor>(this.apiUrl, servidor);
  }

  update(servidor: Servidor): Observable<Servidor> {
    return this.http.put<Servidor>(this.apiUrl, servidor);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
/frontend/src/app/services/secretaria.service.ts
typescriptimport { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Secretaria } from '../models/secretaria';

@Injectable({
  providedIn: 'root'
})
export class SecretariaService {
  private apiUrl = 'http://localhost:8080/secretarias';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Secretaria[]> {
    return this.http.get<Secretaria[]>(this.apiUrl);
  }

  create(secretaria: Secretaria): Observable<Secretaria> {
    return this.http.post<Secretaria>(this.apiUrl, secretaria);
  }

  update(secretaria: Secretaria): Observable<Secretaria> {
    return this.http.put<Secretaria>(this.apiUrl, secretaria);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
/frontend/src/styles.scss
scssbody {
  margin: 0;
  font-family: var(--font-family);
}4.6sExpert
