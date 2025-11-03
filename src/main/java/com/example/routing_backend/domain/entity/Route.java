package com.example.routing_backend.domain.entity;

import com.example.routing_backend.domain.enums.OptimizationType;
import com.example.routing_backend.domain.enums.RouteStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Route (Rota Otimizada) Entity
 * Representa uma rota otimizada gerada pelo sistema contendo múltiplas entregas.
 *
 * Tabela: routes
 * Relacionamentos:
 * - N:1 com Driver (muitas rotas para um entregador)
 * - 1:N com Delivery (uma rota contém múltiplas entregas)
 */
@Entity
@Table(
    name = "routes",
    indexes = {
        @Index(name = "idx_routes_driver_id", columnList = "driver_id"),
        @Index(name = "idx_routes_status", columnList = "status"),
        @Index(name = "idx_routes_created_at", columnList = "created_at"),
        @Index(name = "idx_routes_driver_status", columnList = "driver_id, status")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Relacionamento N:1 com Driver
     * Muitas rotas podem pertencer a um entregador
     */
    @NotNull(message = "Driver é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "driver_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_route_driver")
    )
    @ToString.Exclude
    private Driver driver;

    @Size(max = 100, message = "Nome da rota deve ter no máximo 100 caracteres")
    @Column(name = "name", length = 100)
    private String name;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private RouteStatus status = RouteStatus.PENDING;

    @NotNull(message = "Tipo de otimização é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "optimization_type", nullable = false, length = 30)
    private OptimizationType optimizationType;

    /**
     * Ponto de partida da rota
     * Formato: {"lat": -23.5505, "lng": -46.6333, "address": "Rua ABC, 123"}
     */
    @NotNull(message = "Localização de início é obrigatória")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "start_location", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> startLocation;

    /**
     * Ponto final da rota (se diferente do início)
     * Formato: {"lat": -23.5505, "lng": -46.6333, "address": "Rua ABC, 123"}
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "end_location", columnDefinition = "jsonb")
    private Map<String, Object> endLocation;

    @DecimalMin(value = "0.0", message = "Distância total deve ser maior ou igual a zero")
    @Digits(integer = 6, fraction = 2, message = "Distância deve ter no máximo 6 dígitos inteiros e 2 decimais")
    @Column(name = "total_distance_km", precision = 8, scale = 2)
    private BigDecimal totalDistanceKm;

    @Min(value = 0, message = "Duração total deve ser maior ou igual a zero")
    @Column(name = "total_duration_minutes")
    private Integer totalDurationMinutes;

    @DecimalMin(value = "0.0", message = "Custo estimado deve ser maior ou igual a zero")
    @Digits(integer = 6, fraction = 2, message = "Custo deve ter no máximo 6 dígitos inteiros e 2 decimais")
    @Column(name = "estimated_fuel_cost", precision = 8, scale = 2)
    private BigDecimal estimatedFuelCost;

    @DecimalMin(value = "0.0", message = "Receita estimada deve ser maior ou igual a zero")
    @Digits(integer = 6, fraction = 2, message = "Receita deve ter no máximo 6 dígitos inteiros e 2 decimais")
    @Column(name = "estimated_revenue", precision = 8, scale = 2)
    private BigDecimal estimatedRevenue;

    @DecimalMin(value = "0.0", message = "Distância real deve ser maior ou igual a zero")
    @Digits(integer = 6, fraction = 2, message = "Distância deve ter no máximo 6 dígitos inteiros e 2 decimais")
    @Column(name = "actual_distance_km", precision = 8, scale = 2)
    private BigDecimal actualDistanceKm;

    @Min(value = 0, message = "Duração real deve ser maior ou igual a zero")
    @Column(name = "actual_duration_minutes")
    private Integer actualDurationMinutes;

    @DecimalMin(value = "0.0", message = "Custo real deve ser maior ou igual a zero")
    @Digits(integer = 6, fraction = 2, message = "Custo deve ter no máximo 6 dígitos inteiros e 2 decimais")
    @Column(name = "actual_fuel_cost", precision = 8, scale = 2)
    private BigDecimal actualFuelCost;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * Metadados da otimização (API usada, parâmetros, etc.)
     * Formato livre JSONB para flexibilidade
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "optimization_metadata", columnDefinition = "jsonb")
    private Map<String, Object> optimizationMetadata;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // =================================================================
    // Relacionamentos (preparados para quando Delivery existir)
    // =================================================================

    // @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    // @OrderBy("sequence_order ASC")
    // private List<Delivery> deliveries = new ArrayList<>();

    // =================================================================
    // Métodos auxiliares
    // =================================================================

    /**
     * Inicia a rota
     */
    public void start() {
        if (this.status != RouteStatus.PENDING) {
            throw new IllegalStateException("Rota só pode ser iniciada se estiver PENDING");
        }
        this.status = RouteStatus.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
    }

    /**
     * Completa a rota
     */
    public void complete() {
        if (this.status != RouteStatus.IN_PROGRESS) {
            throw new IllegalStateException("Rota só pode ser completada se estiver IN_PROGRESS");
        }
        this.status = RouteStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    /**
     * Cancela a rota
     */
    public void cancel() {
        if (this.status == RouteStatus.COMPLETED) {
            throw new IllegalStateException("Rota completada não pode ser cancelada");
        }
        this.status = RouteStatus.CANCELLED;
    }

    /**
     * Verifica se a rota está em andamento
     */
    public boolean isInProgress() {
        return this.status == RouteStatus.IN_PROGRESS;
    }

    /**
     * Verifica se a rota foi completada
     */
    public boolean isCompleted() {
        return this.status == RouteStatus.COMPLETED;
    }

    /**
     * Verifica se a rota foi cancelada
     */
    public boolean isCancelled() {
        return this.status == RouteStatus.CANCELLED;
    }

    /**
     * Calcula a economia de combustível (estimado - real)
     * Retorna null se os dados reais não estiverem disponíveis
     */
    public BigDecimal getFuelCostSavings() {
        if (estimatedFuelCost == null || actualFuelCost == null) {
            return null;
        }
        return estimatedFuelCost.subtract(actualFuelCost);
    }

    /**
     * Calcula o lucro líquido estimado (receita - custo combustível)
     * Retorna null se os dados não estiverem disponíveis
     */
    public BigDecimal getEstimatedNetProfit() {
        if (estimatedRevenue == null || estimatedFuelCost == null) {
            return null;
        }
        return estimatedRevenue.subtract(estimatedFuelCost);
    }

    /**
     * Calcula o lucro líquido real (receita estimada - custo real)
     * Retorna null se os dados não estiverem disponíveis
     */
    public BigDecimal getActualNetProfit() {
        if (estimatedRevenue == null || actualFuelCost == null) {
            return null;
        }
        return estimatedRevenue.subtract(actualFuelCost);
    }

    /**
     * Calcula a diferença de distância (real - estimado)
     * Retorna null se os dados reais não estiverem disponíveis
     */
    public BigDecimal getDistanceDifference() {
        if (totalDistanceKm == null || actualDistanceKm == null) {
            return null;
        }
        return actualDistanceKm.subtract(totalDistanceKm);
    }

    /**
     * Calcula a diferença de tempo (real - estimado) em minutos
     * Retorna null se os dados reais não estiverem disponíveis
     */
    public Integer getTimeDifference() {
        if (totalDurationMinutes == null || actualDurationMinutes == null) {
            return null;
        }
        return actualDurationMinutes - totalDurationMinutes;
    }

    /**
     * Verifica se tem o mesmo ponto de início e fim (rota circular)
     */
    public boolean isCircularRoute() {
        return endLocation == null || endLocation.equals(startLocation);
    }

    @PrePersist
    protected void prePersist() {
        if (status == null) {
            status = RouteStatus.PENDING;
        }
    }
}
