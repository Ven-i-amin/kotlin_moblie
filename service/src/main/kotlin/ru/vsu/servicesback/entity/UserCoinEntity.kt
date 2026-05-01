package ru.vsu.servicesback.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "user_coins",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "currency_id"])],
)
class UserCoinEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "currency_id", nullable = false)
    var currencyId: String = "",
    @Column(nullable = false)
    var name: String = "",
    @Column(nullable = false)
    var amount: Double = 0.0,
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity = UserEntity(),
)
