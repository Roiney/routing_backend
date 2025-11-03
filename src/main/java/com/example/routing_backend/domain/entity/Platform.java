package com.example.routing_backend.domain.entity;

import com.example.routing_backend.domain.enums.PlatformType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Platform (Plataforma de Entrega) Entity
 * Representa uma plataforma de entrega onde o entregador trabalha.
 *
 * Tabela: platforms
 * Relacionamentos:
 * - N:1 com Driver (muitas plataformas para um entregador)
 * - 1:N com Delivery
 * - 1:N com FinanceRecord
 */
@Entity
@Table(
    name = "platforms",
    indexes = {
        @Index(name = "idx_platforms_driver_id", columnList = "driver_id"),
        @Index(name = "idx_platforms_type", columnList = "platform_type")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_driver_platform_type",
            columnNames = {"driver_id", "platform_type"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Relacionamento N:1 com Driver
     * Muitas plataformas podem pertencer a um entregador
     */
    @NotNull(message = "Driver é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "driver_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_platform_driver")
    )
    @ToString.Exclude // Evita lazy loading no toString
    private Driver driver;

    @NotNull(message = "Tipo de plataforma é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "platform_type", nullable = false, length = 30)
    private PlatformType platformType;

    @NotNull(message = "Status ativo é obrigatório")
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Size(max = 100, message = "Identificador da conta deve ter no máximo 100 caracteres")
    @Column(name = "account_identifier", length = 100)
    private String accountIdentifier;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // =================================================================
    // Relacionamentos (preparados para quando outras entidades existirem)
    // =================================================================

    // @OneToMany(mappedBy = "platform", cascade = CascadeType.ALL)
    // private List<Delivery> deliveries = new ArrayList<>();

    // @OneToMany(mappedBy = "platform", cascade = CascadeType.ALL)
    // private List<FinanceRecord> financeRecords = new ArrayList<>();

    // =================================================================
    // Métodos auxiliares
    // =================================================================

    /**
     * Ativa a plataforma
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Desativa a plataforma
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Verifica se a plataforma está ativa
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(this.isActive);
    }

    /**
     * Retorna o nome amigável da plataforma
     */
    public String getPlatformName() {
        return switch (platformType) {
            case LOGGI -> "Loggi";
            case MERCADO_LIVRE_FLEX -> "Mercado Livre Flex";
            case AMAZON_FLEX -> "Amazon Flex";
            case LALAMOVE -> "Lalamove";
            case SHOPEE_XPRESS -> "Shopee Xpress";
            case RAPPI -> "Rappi";
            case IFOOD -> "iFood";
            case UBER_FLASH -> "Uber Flash";
            case OTHER -> "Outra";
        };
    }

    @PrePersist
    protected void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
    }
}
