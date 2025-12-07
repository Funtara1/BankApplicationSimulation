package com.Fuad.BankApplicationSimulation.Entity;



import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @Column(name = "phone_number",unique = true)
    private String phoneNumber;

    private String address;

    @Column(unique = true)
    private String fin;

    //orphanRemoval = true — удаляет счета, если они удалены из коллекции.
    //cascade = CascadeType.ALL — автоматически сохраняет/удаляет счета при сохранении/удалении клиента.
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Account> accounts = new ArrayList<>();

    public void setFin(String fin) {
        this.fin = fin == null ? null : fin.trim().toUpperCase();
    }

}