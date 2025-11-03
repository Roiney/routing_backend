package com.example.routing_backend.domain.entity;

import com.example.routing_backend.domain.enums.AccountStatus;
import com.example.routing_backend.domain.enums.NavigationApp;
import com.example.routing_backend.domain.enums.SubscriptionType;
import com.example.routing_backend.domain.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Driver (Entregador) Entity
 * Representa o utilizador entregador autônomo que utiliza a plataforma.
 *
 * Tabela: condutores
 * Relacionamentos:
 * - 1:N com Platform
 * - 1:N com Route
 * - 1:N com Delivery
 * - 1:N com FinanceRecord
 */
@Entity
@Table(
    name = "drivers",
    indexes = {
        @Index(name = "idx_drivers_email", columnList = "email"),
        @Index(name = "idx_drivers_phone", columnList = "phone"),
        @Index(name = "idx_drivers_subscription_type", columnList = "subscription_type"),
        @Index(name = "idx_drivers_account_status", columnList = "account_status")
    }
)
@SQLDelete(sql = "UPDATE drivers SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"passwordHash"}) // Não exibir senha em logs
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(
        regexp = "^\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$",
        message = "Telefone deve seguir formato brasileiro (ex: (11) 98765-4321)"
    )
    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 60, max = 255, message = "Hash da senha inválido")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @NotNull(message = "Tipo de veículo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 20)
    private VehicleType vehicleType;

    @Size(max = 100, message = "Região deve ter no máximo 100 caracteres")
    @Column(name = "region", length = 100)
    private String region;

    @DecimalMin(value = "0.01", message = "Custo do combustível deve ser maior que zero")
    @Digits(integer = 3, fraction = 2, message = "Custo do combustível deve ter no máximo 3 dígitos inteiros e 2 decimais")
    @Column(name = "fuel_cost_per_liter", precision = 5, scale = 2)
    private BigDecimal fuelCostPerLiter;

    @DecimalMin(value = "0.01", message = "Consumo do veículo deve ser maior que zero")
    @Digits(integer = 3, fraction = 2, message = "Consumo deve ter no máximo 3 dígitos inteiros e 2 decimais")
    @Column(name = "vehicle_consumption", precision = 5, scale = 2)
    private BigDecimal vehicleConsumption;

    /**
     * Localização padrão de início das rotas
     * Formato: {"lat": -23.5505, "lng": -46.6333, "address": "Rua ABC, 123"}
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "default_start_location", columnDefinition = "jsonb")
    private Map<String, Object> defaultStartLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_navigation_app", length = 20)
    private NavigationApp preferredNavigationApp;

    @NotNull(message = "Status da conta é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 20)
    @Builder.Default
    private AccountStatus accountStatus = AccountStatus.PENDING;

    @NotNull(message = "Tipo de assinatura é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type", nullable = false, length = 20)
    @Builder.Default
    private SubscriptionType subscriptionType = SubscriptionType.FREE;

    @Column(name = "subscription_expires_at")
    private LocalDateTime subscriptionExpiresAt;

    @NotNull(message = "Flag de email verificado é obrigatória")
    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Soft delete: quando não nulo, indica que o registro foi excluído
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // =================================================================
    // Relacionamentos (preparados para quando outras entidades existirem)
    // =================================================================

    // @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Platform> platforms = new ArrayList<>();

    // @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Route> routes = new ArrayList<>();

    // @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Delivery> deliveries = new ArrayList<>();

    // @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<FinanceRecord> financeRecords = new ArrayList<>();

    // =================================================================
    // Métodos auxiliares
    // =================================================================

    /**
     * Verifica se o entregador tem assinatura Premium ativa
     */
    public boolean isPremium() {
        return subscriptionType == SubscriptionType.PREMIUM
            && (subscriptionExpiresAt == null || subscriptionExpiresAt.isAfter(LocalDateTime.now()));
    }

    /**
     * Verifica se a assinatura está expirada
     */
    public boolean isSubscriptionExpired() {
        return subscriptionExpiresAt != null && subscriptionExpiresAt.isBefore(LocalDateTime.now());
    }

    /**
     * Verifica se a conta está ativa
     */
    public boolean isActive() {
        return accountStatus == AccountStatus.ACTIVE;
    }

    /**
     * Marca o email como verificado
     */
    public void verifyEmail() {
        this.emailVerified = true;
        this.emailVerifiedAt = LocalDateTime.now();
    }

    /**
     * Ativa a assinatura Premium por um período em dias
     */
    public void activatePremium(int daysValid) {
        this.subscriptionType = SubscriptionType.PREMIUM;
        this.subscriptionExpiresAt = LocalDateTime.now().plusDays(daysValid);
    }

    /**
     * Cancela a assinatura Premium (volta para FREE)
     */
    public void cancelPremium() {
        this.subscriptionType = SubscriptionType.FREE;
        this.subscriptionExpiresAt = null;
    }

    @PrePersist
    protected void prePersist() {
        if (accountStatus == null) {
            accountStatus = AccountStatus.PENDING;
        }
        if (subscriptionType == null) {
            subscriptionType = SubscriptionType.FREE;
        }
        if (emailVerified == null) {
            emailVerified = false;
        }
    }
}
