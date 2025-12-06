package com.Fuad.BankApplicationSimulation.Entity;



import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "customer")
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

    @Column(name = "phone_number")
    private Integer phoneNumber;

    private String address;


    //orphanRemoval = true — удаляет счета, если они удалены из коллекции.
    //cascade = CascadeType.ALL — автоматически сохраняет/удаляет счета при сохранении/удалении клиента.
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Collection<Account> accounts = new ArrayList<>();
}