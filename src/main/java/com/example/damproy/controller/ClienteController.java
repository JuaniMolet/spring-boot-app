package com.example.damproy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.damproy.domain.Cliente;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private static Logger log = (Logger) LoggerFactory.getLogger(ClienteController.class);
    private static final List<Cliente> listaClientes = new ArrayList<>();
    private static Integer ID_GEN = 1;

    @GetMapping(path = "/{id}")
    public ResponseEntity<Cliente> clientePorId(@PathVariable Integer id) {

        Optional<Cliente> c = listaClientes
                .stream()
                .filter(unCli -> unCli.getId().equals(id)).findFirst();
        return ResponseEntity.of(c);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> todos(@RequestParam(required = false) String nombre) {
        log.info("Buscar cliente por nombre que empiece con {}", nombre);
        List<Cliente> resultado = listaClientes;
        if (StringUtils.hasText(nombre)) {
            resultado = listaClientes.stream().filter(c -> c.getNombre().startsWith(nombre))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente nuevo) {
        log.info("Se tiene que crear un cliente {}", nuevo);
        nuevo.setId(ID_GEN++);
        listaClientes.add(nuevo);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Cliente> actualizar(@RequestBody Cliente nuevo, @PathVariable Integer id) {
        OptionalInt indexOpt = IntStream.range(0, listaClientes.size())
                .filter(i -> listaClientes
                        .get(i)
                        .getId()
                        .equals(id))
                .findFirst();

        if (indexOpt.isPresent()) {
            listaClientes.set(indexOpt.getAsInt(), nuevo);
            return ResponseEntity.ok(nuevo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Cliente> borrar(@PathVariable Integer id) {
        OptionalInt indexOpt = IntStream.range(0, listaClientes.size())
                .filter(i -> listaClientes
                        .get(i)
                        .getId()
                        .equals(id))
                .findFirst();

        if (indexOpt.isPresent()) {
            listaClientes.remove(indexOpt.getAsInt());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
